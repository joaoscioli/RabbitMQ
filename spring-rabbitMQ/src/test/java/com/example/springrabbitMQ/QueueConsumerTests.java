package com.example.springrabbitMQ;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QueueConsumerTests {
    private final QueueConsumer consumer = new QueueConsumer();

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void rejectsBlankPayloadsWithoutRequestingRedelivery(String payload) {
        var exception = assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> consumer.receive(payload));

        assertEquals("message payload must not be blank", exception.getMessage());
    }

    @Test
    void acceptsNonBlankPayloads() {
        assertDoesNotThrow(() -> consumer.receive("{\"orderId\":\"order-123\"}"));
    }
}
