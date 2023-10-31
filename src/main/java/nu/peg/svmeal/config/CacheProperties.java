package nu.peg.svmeal.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "svmeal.cache")
public record CacheProperties(
    NamedCacheProperties restaurants,
    NamedCacheProperties restaurantDtos,
    NamedCacheProperties mealPlan) {

  public record NamedCacheProperties(int maxSize, Duration expireAfterWrite) {}
}
