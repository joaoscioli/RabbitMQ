package com.example.springrabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.stereotype.Component;


//consome a mensagem
@Component
public class QueueConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueConsumer.class);

    @RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String fileBody,
                        @Header(name = AmqpHeaders.MESSAGE_ID, required = false) String messageId) {
        if (fileBody == null || fileBody.isBlank()) {
            throw new AmqpRejectAndDontRequeueException("message payload must not be blank");
        }
        LOGGER.info("Received RabbitMQ message payloadCharacters={} messageId={}",
                fileBody.length(), safeMessageId(messageId));
    }

    private String safeMessageId(String messageId) {
        if (messageId == null || messageId.isBlank()) {
            return "[missing]";
        }
        // IDs are operational metadata, never free-form text or private content.
        return messageId.length() <= 128 && messageId.matches("[A-Za-z0-9._:-]+")
                ? messageId : "[invalid]";
    }
}
