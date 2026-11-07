package org.example.pdtickets.api;

import java.time.Instant;

public record TicketMessage(
        String message,
        String user,
        Instant send_time
) {
}