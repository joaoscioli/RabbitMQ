package com.example.springrabbitMQ;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePostProcessor;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@ExtendWith(MockitoExtension.class)
class QueueSenderTests {
    @Mock
    private RabbitTemplate rabbitTemplate;

    @ParameterizedTest
    @ValueSource(strings = {"event with spaces", "event\nforged", "café", "event/123"})
    void rejectsUnsafeEventIdsBeforePublishing(String messageId) {
        var sender = new QueueSender(rabbitTemplate, new Queue("orders.test"));
        assertThrowsExactly(IllegalArgumentException.class, () -> sender.send("order", messageId));
        verifyNoInteractions(rabbitTemplate);
    }

    @Test
    void assigningReplayIdentityPreservesTraceHeadersAndExistingMessageEnvelope() {
        var sender = new QueueSender(rabbitTemplate, new Queue("orders.test"));
        sender.send("order-123", "event-replay");
        var processor = ArgumentCaptor.forClass(MessagePostProcessor.class);
        verify(rabbitTemplate).convertAndSend(eq("orders.test"), eq((Object) "order-123"), processor.capture());
        var properties = new MessageProperties();
        properties.setHeader("traceparent", "00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01");
        properties.setCorrelationId("request-123");
        properties.setContentType("application/json");
        properties.setContentEncoding("UTF-8");
        properties.setExpiration("60000");
        properties.setMessageId("old-id");
        properties.setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.NON_PERSISTENT);
        var message = new Message(new byte[] {1, 2, 3}, properties);

        assertSame(message, processor.getValue().postProcessMessage(message));
        assertSame(properties, message.getMessageProperties());
        assertEquals("event-replay", properties.getMessageId());
        assertEquals(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT, properties.getDeliveryMode());
        assertEquals("00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01", properties.getHeader("traceparent"));
        assertEquals("request-123", properties.getCorrelationId());
        assertEquals("application/json", properties.getContentType());
        assertEquals("UTF-8", properties.getContentEncoding());
        assertEquals("60000", properties.getExpiration());
        assertArrayEquals(new byte[] {1, 2, 3}, message.getBody());
        verifyNoMoreInteractions(rabbitTemplate);
    }

    @Test
    void acceptsTheFullSafeAlphabetAtTheLengthLimitAndRejectsOneMoreCharacter() {
        var sender = new QueueSender(rabbitTemplate, new Queue("orders.test"));
        var messageId = "AZaz09._:-" + "x".repeat(118);
        assertEquals(128, messageId.length());
        sender.send("order", messageId);
        var processor = ArgumentCaptor.forClass(MessagePostProcessor.class);
        verify(rabbitTemplate).convertAndSend(eq("orders.test"), eq((Object) "order"), processor.capture());
        var message = processor.getValue().postProcessMessage(new Message(new byte[0], new MessageProperties()));
        assertEquals(messageId, message.getMessageProperties().getMessageId());
        assertThrowsExactly(IllegalArgumentException.class, () -> sender.send("order", messageId + "x"));
        verifyNoMoreInteractions(rabbitTemplate);
    }

    @Test
    void explicitRetryAfterConnectionFailurePreservesTheOriginalEventIdAndPayload() {
        var sender = new QueueSender(rabbitTemplate, new Queue("orders.test"));
        var failure = new AmqpConnectException(new java.net.ConnectException("broker unavailable"));
        org.mockito.Mockito.doThrow(failure).doNothing().when(rabbitTemplate)
                .convertAndSend(eq("orders.test"), eq((Object) "order-123"), any(MessagePostProcessor.class));

        assertSame(failure, assertThrowsExactly(AmqpConnectException.class,
                () -> sender.send("order-123", "event-original")));
        verify(rabbitTemplate).convertAndSend(eq("orders.test"), eq((Object) "order-123"),
                any(MessagePostProcessor.class));
        verifyNoMoreInteractions(rabbitTemplate);

        // Publishing may have succeeded before a connection failure: retain the event identity.
        sender.send("order-123", "event-original");

        var processors = ArgumentCaptor.forClass(MessagePostProcessor.class);
        verify(rabbitTemplate, org.mockito.Mockito.times(2))
                .convertAndSend(eq("orders.test"), eq((Object) "order-123"), processors.capture());
        for (var processor : processors.getAllValues()) {
            var message = new Message("order-123".getBytes(java.nio.charset.StandardCharsets.UTF_8), new MessageProperties());
            processor.postProcessMessage(message);
            assertEquals("event-original", message.getMessageProperties().getMessageId());
            assertEquals(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT,
                    message.getMessageProperties().getDeliveryMode());
            assertArrayEquals("order-123".getBytes(java.nio.charset.StandardCharsets.UTF_8), message.getBody());
        }
        verifyNoMoreInteractions(rabbitTemplate);
    }

    @Test
    void sendsTheOriginalPayloadToTheConfiguredQueue() {
        QueueSender sender = new QueueSender(rabbitTemplate, new Queue("orders.test"));
        String payload = "{\"orderId\":\"order-123\"}";

        sender.send(payload);

        var processor = ArgumentCaptor.forClass(MessagePostProcessor.class);
        verify(rabbitTemplate).convertAndSend(eq("orders.test"), eq((Object) payload), processor.capture());
        var message = new Message(payload.getBytes(java.nio.charset.StandardCharsets.UTF_8), new MessageProperties());
        message.getMessageProperties().setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.NON_PERSISTENT);
        assertSame(message, processor.getValue().postProcessMessage(message));
        assertEquals(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT,
                message.getMessageProperties().getDeliveryMode());
        java.util.UUID.fromString(message.getMessageProperties().getMessageId());
        assertArrayEquals(payload.getBytes(java.nio.charset.StandardCharsets.UTF_8), message.getBody());
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

    @Test
    void exposesConnectionFailureWithoutImplicitlyPublishingTwice() {
        var sender = new QueueSender(rabbitTemplate, new Queue("orders.test"));
        var payload = "{\"orderId\":\"order-123\"}";
        var failure = new AmqpConnectException(new java.net.ConnectException("broker unavailable"));
        doThrow(failure).when(rabbitTemplate).convertAndSend(eq("orders.test"), eq((Object) payload), any(MessagePostProcessor.class));

        assertSame(failure, assertThrowsExactly(AmqpConnectException.class, () -> sender.send(payload)));

        verify(rabbitTemplate).convertAndSend(eq("orders.test"), eq((Object) payload), any(MessagePostProcessor.class));
        verifyNoMoreInteractions(rabbitTemplate);
    }

    @Test
    void explicitEventIdSurvivesRepeatedPublishCallsForReplay() {
        var sender = new QueueSender(rabbitTemplate, new Queue("orders.test"));
        sender.send("order-123", "event-456");
        sender.send("order-123", "event-456");

        var processors = ArgumentCaptor.forClass(MessagePostProcessor.class);
        verify(rabbitTemplate, org.mockito.Mockito.times(2))
                .convertAndSend(eq("orders.test"), eq((Object) "order-123"), processors.capture());
        for (var processor : processors.getAllValues()) {
            var message = new Message(new byte[] {1, 2}, new MessageProperties());
            message.getMessageProperties().setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.NON_PERSISTENT);
            assertSame(message, processor.postProcessMessage(message));
            assertEquals(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT,
                    message.getMessageProperties().getDeliveryMode());
            assertEquals("event-456", message.getMessageProperties().getMessageId());
            assertArrayEquals(new byte[] {1, 2}, message.getBody());
        }
        verifyNoMoreInteractions(rabbitTemplate);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void rejectsBlankEventIdsBeforePublishing(String messageId) {
        var sender = new QueueSender(rabbitTemplate, new Queue("orders.test"));
        var exception = assertThrowsExactly(IllegalArgumentException.class,
                () -> sender.send("order-123", messageId));
        assertEquals("messageId must not be blank", exception.getMessage());
        verifyNoInteractions(rabbitTemplate);
    }
}
