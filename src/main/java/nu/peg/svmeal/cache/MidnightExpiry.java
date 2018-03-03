package nu.peg.svmeal.cache;

import org.ehcache.expiry.ExpiryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class MidnightExpiry implements ExpiryPolicy {
    private final Logger LOGGER = LoggerFactory.getLogger(MidnightExpiry.class);

    @Override
    public Duration getExpiryForCreation(Object key, Object value) {
        LocalDateTime expirationTime = LocalDate.now().plusDays(1).atStartOfDay();
        LOGGER.info("Expiring new entry at " + expirationTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return Duration.between(LocalDateTime.now(), expirationTime);
    }

    @Override
    public Duration getExpiryForAccess(Object key, Supplier value) {
        return null;
    }

    @Override
    public Duration getExpiryForUpdate(Object key, Supplier oldValue, Object newValue) {
        return null;
    }
}