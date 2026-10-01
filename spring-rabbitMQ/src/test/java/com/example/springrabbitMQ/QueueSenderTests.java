package com.example.springrabbitMQ;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class QueueSenderTests {
    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void sendsTheOriginalPayloadToTheConfiguredQueue() {
        QueueSender sender = new QueueSender(rabbitTemplate, new Queue("orders.test"));
        String payload = "{\"orderId\":\"order-123\"}";

        sender.send(payload);

        verify(rabbitTemplate).convertAndSend("orders.test", payload);
        verifyNoMoreInteractions(rabbitTemplate);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void rejectsEmptyMessagesWithoutPublishing(String payload) {
        QueueSender sender = new QueueSender(rabbitTemplate, new Queue("orders.test"));

        var exception = assertThrows(IllegalArgumentException.class, () -> sender.send(payload));

        assertEquals("order must not be blank", exception.getMessage());
        verifyNoInteractions(rabbitTemplate);
    }
}
