package com.example.springrabbitMQ;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/** Real sender/converter/consumer with an in-memory replacement for broker transport. */
@ExtendWith(OutputCaptureExtension.class)
class MessageConversionWorkflowTests {
    @org.junit.jupiter.api.Test
    void httpReplayPreservesTheEventIdentityThroughTheRealSenderAndConverter() throws Exception {
        var transport = mock(RabbitTemplate.class);
        var converter = new SimpleMessageConverter();
        var published = new java.util.ArrayList<org.springframework.amqp.core.Message>();
        doAnswer(invocation -> {
            var message = converter.toMessage(invocation.getArgument(1), new MessageProperties());
            MessagePostProcessor processor = invocation.getArgument(2);
            published.add(processor.postProcessMessage(message));
            return null;
        }).when(transport).convertAndSend(eq("orders.http"), any(Object.class), any(MessagePostProcessor.class));
        var sender = new QueueSender(transport, new Queue("orders.http"));
        var mvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
                .standaloneSetup(new MessageController(sender)).build();

        for (int attempt = 0; attempt < 2; attempt++) {
            mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/messages")
                            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                            .content("{\"payload\":\"order-123\",\"messageId\":\"event-http-123\"}"))
                    .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isAccepted());
        }

        assertThat(published).hasSize(2);
        assertThat(published.get(0)).isNotSameAs(published.get(1));
        for (var message : published) {
            assertThat(converter.fromMessage(message)).isEqualTo("order-123");
            assertThat(message.getMessageProperties().getMessageId()).isEqualTo("event-http-123");
            assertThat(message.getMessageProperties().getDeliveryMode()).isEqualTo(MessageDeliveryMode.PERSISTENT);
        }
        verify(transport, times(2)).convertAndSend(eq("orders.http"), eq((Object) "order-123"), any(MessagePostProcessor.class));
        verifyNoMoreInteractions(transport);
        // Replays publish again with a stable ID; durable consumer deduplication remains separate.
    }

    @org.junit.jupiter.api.Test
    void redeliveryOfTheSameEventStillReachesTheConsumerTwice(CapturedOutput output) {
        var converter = new SimpleMessageConverter();
        var consumer = new QueueConsumer();
        var properties = new MessageProperties();
        properties.setMessageId("event-redelivery");
        var original = converter.toMessage("private-order-body", properties);
        var redelivery = new org.springframework.amqp.core.Message(
                original.getBody().clone(), org.springframework.amqp.core.MessagePropertiesBuilder
                        .fromClonedProperties(properties).setRedelivered(true).build());

        for (var message : java.util.List.of(original, redelivery)) {
            assertThat(converter.fromMessage(message)).isEqualTo("private-order-body");
            consumer.receive((String) converter.fromMessage(message), message.getMessageProperties().getMessageId());
        }

        assertThat(redelivery.getMessageProperties().isRedelivered()).isTrue();
        assertThat(output.getOut().lines().filter(line -> line.contains("messageId=event-redelivery")).count())
                .isEqualTo(2);
        assertThat(output.getAll()).doesNotContain("private-order-body");
        // Message IDs support correlation; this consumer has no durable deduplication store.
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"customer\":\"João\",\"item\":\"♟\"}", "private-order\nsecond-line"})
    void producerMetadataAndUtf8PayloadSurviveConversionWithoutPayloadLogging(String payload, CapturedOutput output) {
        var transport = mock(RabbitTemplate.class);
        var converter = new SimpleMessageConverter();
        var consumer = new QueueConsumer();
        doAnswer(invocation -> {
            var message = converter.toMessage(invocation.getArgument(1), new MessageProperties());
            MessagePostProcessor processor = invocation.getArgument(2);
            assertThat(processor.postProcessMessage(message)).isSameAs(message);
            assertThat(message.getBody()).isEqualTo(payload.getBytes(StandardCharsets.UTF_8));
            assertThat(message.getMessageProperties().getContentType()).isEqualTo("text/plain");
            assertThat(message.getMessageProperties().getContentEncoding()).isEqualTo("UTF-8");
            assertThat(message.getMessageProperties().getMessageId()).isEqualTo("event-conversion-123");
            assertThat(message.getMessageProperties().getDeliveryMode()).isEqualTo(MessageDeliveryMode.PERSISTENT);
            var decoded = (String) converter.fromMessage(message);
            assertThat(decoded).isEqualTo(payload);
            consumer.receive(decoded, message.getMessageProperties().getMessageId());
            return null;
        }).when(transport).convertAndSend(eq("orders.workflow"), eq((Object) payload), any(MessagePostProcessor.class));

        new QueueSender(transport, new Queue("orders.workflow")).send(payload, "event-conversion-123");

        verify(transport).convertAndSend(eq("orders.workflow"), eq((Object) payload), any(MessagePostProcessor.class));
        verifyNoMoreInteractions(transport);
        assertThat(output.getOut()).contains("payloadCharacters=" + payload.length(), "messageId=event-conversion-123")
                .doesNotContain(payload, "private-order", "customer");
    }
}
