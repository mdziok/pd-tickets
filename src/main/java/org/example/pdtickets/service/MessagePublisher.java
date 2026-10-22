package org.example.pdtickets.service;

import org.example.pdtickets.api.MessageRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String incomingQueue;

    public MessagePublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${app.rabbitmq.incoming-queue}") String incomingQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.incomingQueue = incomingQueue;
    }

    public void publish(MessageRequest request) {
        rabbitTemplate.convertAndSend(incomingQueue, request);
    }
}
