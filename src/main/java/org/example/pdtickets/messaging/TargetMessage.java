package org.example.pdtickets.messaging;

import java.time.Instant;

public record TargetMessage(
        String message,
        String user,
        Instant send_time,
        Instant processed_at,
        String category
) {
}
