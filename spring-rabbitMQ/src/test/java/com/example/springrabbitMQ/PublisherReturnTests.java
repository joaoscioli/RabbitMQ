package com.example.springrabbitMQ;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(OutputCaptureExtension.class)
class PublisherReturnTests {
    @Test
    void configuredReturnCallbackLogsSafeDiagnosticsWithoutRepublishing(CapturedOutput output) {
        var connectionFactory = mock(ConnectionFactory.class);
        var template = new SenderConfig().rabbitTemplate(connectionFactory);
        var callback = (RabbitTemplate.ReturnsCallback) new DirectFieldAccessor(template)
                .getPropertyValue("returnsCallback");
        var body = "private-customer@example.com\nFORGED".getBytes(StandardCharsets.UTF_8);
        var properties = new MessageProperties();
        properties.setMessageId("private-id");

        callback.returnedMessage(new ReturnedMessage(new Message(body, properties), 312,
                "private-reply", "private-exchange", "private-routing-key"));

        assertThat(output.getAll()).contains("RabbitMQ publish returned replyCode=312 payloadBytes=" + body.length)
                .doesNotContain("private-", "FORGED");
        verifyNoInteractions(connectionFactory);
    }
}
