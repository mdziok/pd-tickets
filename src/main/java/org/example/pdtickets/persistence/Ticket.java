package org.example.pdtickets.persistence;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Getter
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "send_time", nullable = false)
    private Instant sendTime;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    @Column(name = "category", length = 100)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status = TicketStatus.NEW;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TicketPriority priority = TicketPriority.MEDIUM;

    protected Ticket() {
    }

    public Ticket(String message, String user,
                  Instant sendTime, Instant processedAt,
                   String category) {
        this.message = message;
        this.sendTime = sendTime;
        this.username = user;
        this.category = category;
        this.processedAt = processedAt;
    }

    public void changeStatus(TicketStatus status) {
        this.status = status;
    }

    public void changePriority(TicketPriority priority) {
        this.priority = priority;
    }

}
