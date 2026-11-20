package org.example.pdtickets.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

/**
 * Włącza cache oparty o Redis (typ cache i TTL: application.properties).
 *
 * Redis jest tylko optymalizacją: gdy jest niedostępny, błąd cache jest logowany,
 * a aplikacja czyta dane bezpośrednio z PostgreSQL zamiast zwracać błąd 500.
 */
@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

    public static final String TICKETS_CACHE = "tickets";

    private static final Log log = LogFactory.getLog(CacheConfig.class);

    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                log.warn("Redis: błąd odczytu cache '" + cache.getName() + "' key=" + key + " -> fallback do bazy", e);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                log.warn("Redis: błąd zapisu cache '" + cache.getName() + "' key=" + key, e);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.warn("Redis: błąd usuwania z cache '" + cache.getName() + "' key=" + key
                        + " (wpis wygaśnie po TTL)", e);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.warn("Redis: błąd czyszczenia cache '" + cache.getName() + "'", e);
            }
        };
    }
}
