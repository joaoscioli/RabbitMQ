package com.example.springrabbitMQ;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Compatibility demo routes share the configured, persistent publisher. */
@RestController
public class TesteController {
    private final QueueSender sender;

    public TesteController(QueueSender sender) {
        this.sender = sender;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.amqp.AmqpException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE)
    public String unavailable() {
        return "Message publishing is unavailable";
    }

    @GetMapping
    public String sends() {
        sender.send("test message");
        return "ok. done";
    }

    @GetMapping("/teste")
    public String send() {
        sender.send("test message");
        return "ok. foi";
    }
}
