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
    void overlappingDeliveriesWithTheSameIdKeepIndependentRetryBudgets() throws Exception {
        var entered = new java.util.concurrent.CountDownLatch(2);
        var first = new Message(new byte[] {1}, new MessageProperties());
        var second = new Message(new byte[] {2}, new MessageProperties());
        first.getMessageProperties().setMessageId("shared-event");
        second.getMessageProperties().setMessageId("shared-event");
        var firstAttempts = new AtomicInteger();
        var secondAttempts = new AtomicInteger();
        var listener = advised((channel, message) -> {
            var attempts = message == first ? firstAttempts : secondAttempts;
            if (attempts.incrementAndGet() == 1) {
                entered.countDown();
                try {
                    assertThat(entered.await(5, java.util.concurrent.TimeUnit.SECONDS)).isTrue();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(exception);
                }
                throw new IllegalStateException("transient failure after overlap");
            }
            if (message == first) {
                throw new IllegalStateException("first delivery never recovers");
            }
        });
        var executor = java.util.concurrent.Executors.newFixedThreadPool(2);
        try {
            var rejected = executor.submit(() -> assertThatThrownBy(() -> listener.invoke(null, first))
                    .hasCauseInstanceOf(AmqpRejectAndDontRequeueException.class));
            var recovered = executor.submit(() -> listener.invoke(null, second));
            rejected.get(10, java.util.concurrent.TimeUnit.SECONDS);
            recovered.get(10, java.util.concurrent.TimeUnit.SECONDS);
            assertThat(firstAttempts).hasValue(3);
            assertThat(secondAttempts).hasValue(2);
        } finally {
            executor.shutdownNow();
            assertThat(executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)).isTrue();
        }
    }

    @Test
    void replayWithTheSameEventIdGetsANewRetryBudgetAndIsNotDeduplicated() {
        var firstAttempts = new AtomicInteger();
        var replayAttempts = new AtomicInteger();
        var firstProperties = new MessageProperties();
        firstProperties.setMessageId("event-replayed");
        var replayProperties = new MessageProperties();
        replayProperties.setMessageId("event-replayed");
        var first = new Message(new byte[] {1}, firstProperties);
        var replay = new Message(new byte[] {1}, replayProperties);
        var listener = advised((channel, message) -> {
            assertThat(message.getMessageProperties().getMessageId()).isEqualTo("event-replayed");
            var attempts = message == first ? firstAttempts : replayAttempts;
            if (attempts.incrementAndGet() < 3) {
                throw new IllegalStateException("transient handler failure");
            }
        });

        listener.invoke(null, first);
        listener.invoke(null, replay);

        // Stateless retry tracks an invocation, not an event ID or completed business work.
        assertThat(firstAttempts).hasValue(3);
        assertThat(replayAttempts).hasValue(3);
    }

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
            consumer.receive(" ", "event-123");
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
