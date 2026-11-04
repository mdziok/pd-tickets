package org.example.pdtickets.api;

import jakarta.validation.constraints.NotBlank;

public record MessageRequest(
        @NotBlank String message
        ) {
}
