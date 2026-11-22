package org.example.pdtickets.messaging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.pdtickets.persistence.Ticket;
import org.example.pdtickets.persistence.TicketRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.example.pdtickets.cache.CacheConfig;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@Component
public class TargetListener {

    private static final Log log = LogFactory.getLog(TargetListener.class);
    private final TicketRepository repository;


    private final CacheManager cacheManager;

    public TargetListener(TicketRepository repository, CacheManager cacheManager) {
        this.repository = repository;
        this.cacheManager = cacheManager;
    }

    // Bez @Transactional na metodzie: repository.save() commituje własną transakcję
    // i dopiero PO niej unieważniamy cache. Dzięki temu równoległy odczyt nie zdąży
    // zapisać w Redisie starej listy (sprzed commita).
//    @Transactional
    @RabbitListener(queues = "${app.rabbitmq.target-queue}")
    public void receive(TargetMessage message) {
        if (message.message() == null || message.message().isBlank()) {
            throw new IllegalArgumentException("Pole message nie może być puste");
        }
        if (message.send_time() == null) {
            throw new IllegalArgumentException("Pole send_time jest wymagane");
        }
        if (message.user() == null || message.user().isBlank()) {
            throw new IllegalArgumentException("Pole user nie może być puste");
        }

        Ticket entity = new Ticket(
                message.message(),
                message.user(),
                message.send_time(),
                message.processed_at(),
                message.category()
        );
        repository.save(entity);

        evictUserTickets(message.user());
    }

    private void evictUserTickets(String user) {
        try {
            Cache cache = cacheManager.getCache(CacheConfig.TICKETS_CACHE);
            if (cache != null) {
                cache.evict(user);
                log.info("CACHE EVICT tickets[" + user + "] po zapisaniu nowego zgłoszenia");
            }
        } catch (RuntimeException e) {
            // zgłoszenie jest już w bazie; nie cofamy go przez awarię Redisa (cache wygaśnie po TTL)
            log.warn("Nie udało się unieważnić cache dla " + user + " (wygaśnie po TTL)", e);
        }
    }
}
