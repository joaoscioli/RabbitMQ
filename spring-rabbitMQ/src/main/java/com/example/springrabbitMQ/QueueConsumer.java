package com.example.springrabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


//consome a mensagem
@Component
public class QueueConsumer {
    @RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String fileBody) {
        System.out.println("Message " + fileBody);
    }
}
