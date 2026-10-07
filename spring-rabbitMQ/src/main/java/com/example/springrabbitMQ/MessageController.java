package com.example.springrabbitMQ;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MessageController {
    private final QueueSender sender;

    public MessageController(QueueSender sender) {
        this.sender = sender;
    }

    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void publish(@RequestBody PublishMessage request) {
        if (request.payload == null || request.payload.isBlank()
                || request.messageId == null || request.messageId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "payload and messageId must not be blank");
        }
        sender.send(request.payload, request.messageId);
    }

    public static class PublishMessage {
        public String payload;
        public String messageId;
    }
}
