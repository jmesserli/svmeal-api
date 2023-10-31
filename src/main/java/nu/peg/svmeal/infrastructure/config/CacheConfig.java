package nu.peg.svmeal.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {
  private final CacheProperties cacheProperties;

  @Bean
  public CaffeineCache restaurantDtos() {
    return new CaffeineCache(
        CacheNames.RESTAURANT_DTOS,
        Caffeine.newBuilder()
            .maximumSize(cacheProperties.restaurantDtos().maxSize())
            .expireAfterWrite(cacheProperties.restaurantDtos().expireAfterWrite())
            .build());
  }

  @Bean
  public CaffeineCache mealplan() {
    return new CaffeineCache(
        CacheNames.MEAL_PLAN,
        Caffeine.newBuilder()
            .maximumSize(cacheProperties.mealPlan().maxSize())
            .expireAfterWrite(cacheProperties.mealPlan().expireAfterWrite())
            .build());
  }
}
