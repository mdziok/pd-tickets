package org.example.pdtickets.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record MessageRequest(
        @NotBlank String message,
        @NotBlank String user,
        @NotNull Instant send_time
        ) {
}
