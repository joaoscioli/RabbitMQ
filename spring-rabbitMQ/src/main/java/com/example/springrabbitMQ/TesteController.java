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
