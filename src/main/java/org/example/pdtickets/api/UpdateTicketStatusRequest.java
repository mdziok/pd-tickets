package org.example.pdtickets.api;

import jakarta.validation.constraints.NotNull;
import org.example.pdtickets.persistence.TicketStatus;

public record UpdateTicketStatusRequest(
        @NotNull TicketStatus status
) {
}