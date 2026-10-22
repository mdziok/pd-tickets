package org.example.pdtickets.messaging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.pdtickets.persistence.Ticket;
import org.example.pdtickets.persistence.TicketRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TargetListener {

    private static final Log log = LogFactory.getLog(TargetListener.class);
    private final TicketRepository repository;

    public TargetListener(TicketRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "${app.rabbitmq.target-queue}")
    @Transactional
    public void receive(TargetMessage message) {
        if (message.message() == null || message.message().isBlank()) {
            throw new IllegalArgumentException("Pole message nie może być puste");
        }
        if (message.send_time() == null) {
            throw new IllegalArgumentException("Pole send_time jest wymagane");
        }
        if (message.user() == null || message.user().isBlank()) {
            throw new IllegalArgumentException("Pole user nie może być puste");
        }

        Ticket entity = new Ticket(
                message.message(),
                message.user(),
                message.send_time(),
                message.processed_at(),
                message.category()
        );
        repository.save(entity);
    }
}
