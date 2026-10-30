package com.example.springrabbitMQ;

//Publica a mensagem


import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SenderConfig {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SenderConfig.class);

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(returned -> LOGGER.warn(
                "RabbitMQ publish returned replyCode={} payloadBytes={}",
                returned.getReplyCode(), returned.getMessage().getBody().length));
        return rabbitTemplate;
    }
}
