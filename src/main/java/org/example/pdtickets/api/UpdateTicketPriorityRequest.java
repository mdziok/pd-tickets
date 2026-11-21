package org.example.pdtickets.api;

import jakarta.validation.constraints.NotNull;
import org.example.pdtickets.persistence.TicketPriority;

public record UpdateTicketPriorityRequest(
        @NotNull TicketPriority priority
) {
}