package com.example.springrabbitMQ;

import java.util.concurrent.atomic.AtomicInteger;
import com.rabbitmq.client.Channel;
import org.aopalliance.aop.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {
        "spring.rabbitmq.listener.simple.auto-startup=false",
        "spring.rabbitmq.listener.direct.auto-startup=false",
        "spring.rabbitmq.listener.simple.retry.initial-interval=1ms",
        "spring.rabbitmq.listener.simple.retry.max-interval=2ms"
})
class ConsumerRetryTests {
    @Test
    void configuredContainerLimitsUnacknowledgedDeliveriesDuringRetries() {
        var container = factory.createListenerContainer();

        assertThat(new DirectFieldAccessor(container).getPropertyValue("prefetchCount")).isEqualTo(1);
        assertThat(new DirectFieldAccessor(container).getPropertyValue("acknowledgeMode"))
                .isEqualTo(org.springframework.amqp.core.AcknowledgeMode.AUTO);
    }

    @Test
    void laterDeliveryGetsItsOwnRetryBudgetAfterAnEarlierDeliveryIsRejected() {
        var failedAttempts = new AtomicInteger();
        var recoveryAttempts = new AtomicInteger();
        var failedMessage = new Message(new byte[] {1}, new MessageProperties());
        var nextMessage = new Message(new byte[] {2}, new MessageProperties());
        var listener = advised((channel, message) -> {
            if (message == failedMessage) {
                failedAttempts.incrementAndGet();
                throw new IllegalStateException("first delivery fails permanently");
            }
            if (recoveryAttempts.incrementAndGet() < 3) {
                throw new IllegalStateException("next delivery temporarily unavailable");
            }
        });

        assertThatThrownBy(() -> listener.invoke(null, failedMessage))
                .hasCauseInstanceOf(AmqpRejectAndDontRequeueException.class);
        listener.invoke(null, nextMessage);

        assertThat(failedAttempts).hasValue(3);
        assertThat(recoveryAttempts).hasValue(3);
    }

    @Autowired
    private SimpleRabbitListenerContainerFactory factory;

    // The container advice expects the AMQP Message at argument index one.
    public interface ListenerInvocation {
        void invoke(Channel channel, Message message);
    }

    private ListenerInvocation advised(ListenerInvocation listener) {
        var container = factory.createListenerContainer();
        var advice = (Advice[]) new DirectFieldAccessor(container).getPropertyValue("adviceChain");
        assertThat(advice).hasSize(1);
        var proxy = new ProxyFactory(listener);
        proxy.addAdvice(advice[0]);
        return (ListenerInvocation) proxy.getProxy();
    }

    @Test
    void blankPayloadExhaustsConfiguredRetriesBeforeRejectingForDeadLettering() {
        var attempts = new AtomicInteger();
        var consumer = new QueueConsumer();
        var listener = advised((channel, message) -> {
            attempts.incrementAndGet();
            consumer.receive(" ");
        });

        assertThatThrownBy(() -> listener.invoke(null, new Message(new byte[0], new MessageProperties())))
                .hasCauseInstanceOf(AmqpRejectAndDontRequeueException.class)
                .hasRootCauseMessage("message payload must not be blank");
        assertThat(attempts).hasValue(3);
    }

    @Test
    void exhaustedHandlerFailureRejectsAfterExactlyThreeAttempts() {
        var attempts = new AtomicInteger();
        var listener = advised((channel, message) -> {
            attempts.incrementAndGet();
            throw new IllegalStateException("temporary handler failure");
        });

        assertThatThrownBy(() -> listener.invoke(null, new Message(new byte[0], new MessageProperties())))
                .hasCauseInstanceOf(AmqpRejectAndDontRequeueException.class);
        assertThat(attempts).hasValue(3);
    }

    @Test
    void transientHandlerFailureRecoversOnSecondAttemptWithoutRejection() {
        var attempts = new AtomicInteger();
        var listener = advised((channel, message) -> {
            if (attempts.incrementAndGet() == 1) {
                throw new IllegalStateException("temporary handler failure");
            }
        });

        listener.invoke(null, new Message(new byte[0], new MessageProperties()));
        assertThat(attempts).hasValue(2);
    }
}
