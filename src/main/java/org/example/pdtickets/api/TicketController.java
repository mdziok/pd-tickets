package org.example.pdtickets.api;

import org.example.pdtickets.persistence.TicketRepository;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.example.pdtickets.persistence.Ticket;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import org.example.pdtickets.service.TicketService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketRepository ticketRepository;
    private final TicketService ticketService;

    public TicketController(
            TicketRepository ticketRepository,
            TicketService ticketService) {
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
    }

    @GetMapping
    public List<Ticket> getTickets(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        return ticketRepository.findByUsername(username);
    }

    @GetMapping("/all")
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @PatchMapping("/{id}/status")
    public Ticket updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketStatusRequest request) {

        return ticketService.changeStatus(id, request.status());
    }

    @PatchMapping("/{id}/priority")
    public Ticket updatePriority(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketPriorityRequest request) {

        return ticketService.changePriority(id, request.priority());
    }
}