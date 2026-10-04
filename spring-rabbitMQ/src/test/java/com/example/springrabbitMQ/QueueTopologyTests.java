package com.example.springrabbitMQ;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = {
        "queue.name=portfolio-orders",
        "spring.rabbitmq.listener.simple.auto-startup=false",
        "spring.rabbitmq.listener.direct.auto-startup=false"
})
class QueueTopologyTests {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private QueueSender sender;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void customQueueNameDefinesProducerDestinationAndDeadLetterRoute() {
        var queues = context.getBeansOfType(Queue.class);
        assertThat(queues).containsOnlyKeys("testeQueue", "deadLetterQueue");
        var queue = queues.get("testeQueue");
        assertThat(queue.getName()).isEqualTo("portfolio-orders");
        assertThat(queue.isDurable()).isTrue();
        assertThat(queue.getArguments())
                .containsEntry("x-dead-letter-exchange", "")
                .containsEntry("x-dead-letter-routing-key", "portfolio-orders.dlq");
        var deadLetterQueue = queues.get("deadLetterQueue");
        assertThat(deadLetterQueue.getName()).isEqualTo("portfolio-orders.dlq");
        assertThat(deadLetterQueue.isDurable()).isTrue();
        assertThat(deadLetterQueue.getArguments()).isEmpty();
        assertThat(deadLetterQueue.isExclusive()).isFalse();
        assertThat(deadLetterQueue.isAutoDelete()).isFalse();

        var binding = context.getBean(Binding.class);
        assertThat(binding.getDestination()).isEqualTo(queue.getName());
        assertThat(binding.getExchange()).isEqualTo("direct-exchange");
        assertThat(binding.getRoutingKey()).isEqualTo("teste-routing-key");

        sender.send("order-123");
        verify(rabbitTemplate).convertAndSend("portfolio-orders", "order-123");
    }
}
