package com.example.springrabbitMQ;

//Controller para enviar a mensagem

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TesteController {
    @Autowired
    private QueueSender queueSenders;

    @GetMapping
    public String sends(){
        queueSenders.send("test message");
        return "ok. done";
    }
    public TesteController(AmqpTemplate queueSender) {
        this.queueSender = queueSender;
    }

    private final AmqpTemplate queueSender;

    @GetMapping("/teste")
    public String send(){
        queueSender.convertAndSend("teste-exchange", "routing-key-teste", "test message");
        return "ok. foi";
    }
}
