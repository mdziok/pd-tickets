package org.example.pdtickets.service;


import org.example.pdtickets.persistence.Ticket;
import org.example.pdtickets.persistence.TicketRepository;
import org.example.pdtickets.persistence.TicketStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.example.pdtickets.persistence.TicketPriority;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.example.pdtickets.api.TicketDto;
import org.example.pdtickets.cache.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.List;


@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Ticket changeStatus(Long id, TicketStatus status) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Ticket not found"
                ));

        ticket.changeStatus(status);

        return ticket;
    }

    @Transactional
    public Ticket changePriority(Long id, TicketPriority priority) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Ticket not found"
                ));

        ticket.changePriority(priority);

        return ticket;
    }

    /**
     * Lista zgłoszeń użytkownika. Wynik ląduje w Redisie pod kluczem
     * "pd-tickets::tickets::{username}". Ciało metody wykonuje się tylko przy
     * CACHE MISS - przy HIT Spring zwraca wartość z Redisa bez dotykania bazy.
     */
    @Cacheable(cacheNames = CacheConfig.TICKETS_CACHE, key = "#username")
    public List<TicketDto> getTickets(String username) {
        return new ArrayList<>(ticketRepository.findByUsername(username).stream()
                .map(TicketDto::from)
                .toList());
    }
}
