package com.example.springrabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


//consome a mensagem
@Component
public class QueueConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueConsumer.class);

    @RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String fileBody) {
        LOGGER.info("Received RabbitMQ message payload={}", fileBody);
    }
}
