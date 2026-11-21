package org.example.pdtickets.api;

import org.example.pdtickets.persistence.Ticket;

import java.io.Serializable;
import java.time.Instant;

/**
 * Widok zgłoszenia zwracany przez API i przechowywany w Redisie.
 * Serializable, bo domyślny serializer cache to serializacja Javy.
 * Nazwy pól w JSON są takie same jak w encji Ticket, więc frontend się nie zmienia.
 */
public record TicketDto(
        Long id,
        String message,
        String username,
        Instant sendTime,
        Instant processedAt,
        String category
) implements Serializable {

    public static TicketDto from(Ticket t) {
        return new TicketDto(t.getId(), t.getMessage(), t.getUsername(),
                t.getSendTime(), t.getProcessedAt(), t.getCategory());
    }
}
