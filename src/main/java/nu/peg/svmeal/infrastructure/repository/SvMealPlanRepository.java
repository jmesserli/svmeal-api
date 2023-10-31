package nu.peg.svmeal.infrastructure.repository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nu.peg.svmeal.domain.exceptions.ExternalException;
import nu.peg.svmeal.domain.exceptions.MealPlanParsingException;
import nu.peg.svmeal.domain.model.MealPlansDto;
import nu.peg.svmeal.domain.model.RestaurantDto;
import nu.peg.svmeal.domain.repository.MealPlanRepository;
import nu.peg.svmeal.infrastructure.converter.DocumentMealPlanParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static nu.peg.svmeal.infrastructure.config.CacheNames.MEAL_PLAN;
import static nu.peg.svmeal.infrastructure.config.CircuitBreakers.SV_MENU;

@Component
@Slf4j
@RequiredArgsConstructor
public class SvMealPlanRepository implements MealPlanRepository {
  private static final String NO_MEALPLAN_AVAILABLE_ERROR = "No meal plan available";

  private final RestTemplate restTemplate;
  private final DocumentMealPlanParser docParser;

  @Override
  @Cacheable(MEAL_PLAN)
  @CircuitBreaker(name = SV_MENU)
  public MealPlansDto getMealPlans(RestaurantDto restaurant) {
    log.debug("Scraping meal plan for restaurant {}", restaurant);

    ResponseEntity<String> response = restTemplate.getForEntity(restaurant.link(), String.class);

    if (response.getStatusCode() != HttpStatus.OK) {
      throw new ExternalException("Error while fetching meal plan");
    }

    Document document = Jsoup.parse(response.getBody());
    MealPlansDto dto = docParser.convert(document);

    if (dto == null) {
      throw new MealPlanParsingException(NO_MEALPLAN_AVAILABLE_ERROR);
    }

    return dto;
  }
}
