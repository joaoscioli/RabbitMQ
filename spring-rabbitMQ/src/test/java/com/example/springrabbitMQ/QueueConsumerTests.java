package com.example.springrabbitMQ;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(OutputCaptureExtension.class)
class QueueConsumerTests {
    private final QueueConsumer consumer = new QueueConsumer();

    @Test
    void rejectedDeliveryDoesNotPreventTheNextValidMessage(CapturedOutput output) {
        assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> consumer.receive("   ", "rejected-event"));
        consumer.receive("valid payload", "next-event");

        assertThat(output.getAll()).contains("payloadCharacters=13 messageId=next-event")
                .doesNotContain("messageId=rejected-event");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void rejectsBlankPayloadsWithoutRequestingRedelivery(String payload) {
        var exception = assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> consumer.receive(payload, "event-123"));

        assertEquals("message payload must not be blank", exception.getMessage());
    }

    @Test
    void acceptsNonBlankPayloads() {
        assertDoesNotThrow(() -> consumer.receive("{\"orderId\":\"order-123\"}", null));
    }

    @Test
    void logsPayloadSizeWithoutPrivateContentOrInjectedLogLines(CapturedOutput output) {
        var payload = "private-customer@example.com\nFORGED audit entry";

        consumer.receive(payload, "event-123");

        assertThat(output.getAll())
                .contains("Received RabbitMQ message payloadCharacters=" + payload.length(), "messageId=event-123")
                .doesNotContain("private-customer@example.com", "FORGED audit entry");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "private@example.com\nFORGED audit entry", "event with spaces"})
    void missingOrUnsafeIdsDoNotRejectOtherwiseValidMessages(String messageId, CapturedOutput output) {
        consumer.receive("valid payload", messageId);

        assertThat(output.getAll()).contains(messageId == null || messageId.isBlank()
                ? "messageId=[missing]" : "messageId=[invalid]")
                .doesNotContain("private@example.com", "FORGED audit entry", "event with spaces");
    }

    @Test
    void boundsMessageIdLengthInLogs(CapturedOutput output) {
        var boundary = "x".repeat(128);
        consumer.receive("valid", boundary);
        consumer.receive("valid", boundary + "x");

        assertThat(output.getAll()).contains("messageId=" + boundary, "messageId=[invalid]")
                .doesNotContain(boundary + "x");
    }
}
