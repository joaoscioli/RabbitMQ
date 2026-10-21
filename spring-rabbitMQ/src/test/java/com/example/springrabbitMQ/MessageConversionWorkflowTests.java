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
