package org.example.pdtickets.api;

import jakarta.validation.Valid;
import org.example.pdtickets.service.MessagePublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;
import java.time.Instant;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessagePublisher publisher;

    public MessageController(MessagePublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> postMessage(
            @Valid @RequestBody MessageRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        String username = jwt.getClaimAsString("preferred_username");

        TicketMessage requestWithUser = new TicketMessage(
                request.message(),
                username,
                Instant.now()
        );
        
        publisher.publish(requestWithUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of(
                        "status", "accepted",
                        "message", "Zgłoszenie przekazano do aplikacji"
                ));
    }
}
