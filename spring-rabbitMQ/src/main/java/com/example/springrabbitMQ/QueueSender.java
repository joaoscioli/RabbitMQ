package com.example.springrabbitMQ;


import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class QueueSender {
    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;

    public QueueSender(RabbitTemplate rabbitTemplate, @Qualifier("testeQueue") Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    public void send(String order) {
        send(order, UUID.randomUUID().toString());
    }

    public void send(String order, String messageId) {
        if (order == null || order.isBlank()) {
            throw new IllegalArgumentException("order must not be blank");
        }
        if (messageId == null || messageId.isBlank()) {
            throw new IllegalArgumentException("messageId must not be blank");
        }
        rabbitTemplate.convertAndSend(this.queue.getName(), order, message -> {
            message.getMessageProperties().setMessageId(messageId);
            return message;
        });
    }
}
