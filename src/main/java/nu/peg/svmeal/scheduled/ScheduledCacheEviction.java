package nu.peg.svmeal.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static nu.peg.svmeal.util.CacheRegistry.*;

/**
 * TODO Short summary
 *
 * @author Joel Messerli @26.02.2017
 */
@Component
public class ScheduledCacheEviction {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledCacheEviction.class);

    @Scheduled(cron = "0 0 0 * * *")
    @CacheEvict(
            cacheNames = {RESTAURANT_DTOS, RESTAURANTS, MEAL_PLAN},
            allEntries = true
    )
    public void evictCaches() {
        LOGGER.info("Evicting all caches");
    }
}
