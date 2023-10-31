package nu.peg.svmeal.domain.service;

import static nu.peg.svmeal.infrastructure.config.CacheNames.MEAL_PLAN;
import static nu.peg.svmeal.infrastructure.config.CircuitBreakers.SV_MENU;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import nu.peg.svmeal.domain.exceptions.ExternalException;
import nu.peg.svmeal.domain.exceptions.MealPlanParsingException;
import nu.peg.svmeal.domain.model.AvailabilityDto;
import nu.peg.svmeal.domain.model.MealPlanDto;
import nu.peg.svmeal.domain.model.MealPlansDto;
import nu.peg.svmeal.domain.model.RestaurantDto;
import nu.peg.svmeal.infrastructure.converter.DocumentMealPlanParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class MealService {
  private static final String NO_MEALPLAN_AVAILABLE_ERROR = "No meal plan available for this date";

  private final DocumentMealPlanParser docParser;
  private final RestTemplate restTemplate;

  @Autowired
  public MealService(DocumentMealPlanParser docParser, RestTemplate restTemplate) {
    this.docParser = docParser;
    this.restTemplate = restTemplate;
  }

  /**
   * Checks if a meal plan is available for the given dayOffset and restaurant
   *
   * @see #getMealPlan(int, RestaurantDto)
   */
  public AvailabilityDto getAvailability(int dayOffset, RestaurantDto restaurant) {
    try {
      getMealPlan(dayOffset, restaurant);

      return new AvailabilityDto(true);
    } catch (ExternalException | MealPlanParsingException e) {
      log.warn(
          "Meal plan unavailable for restaurant {} with dayOffset {}",
          restaurant.getShortcut(),
          dayOffset);
      log.warn("Exception: ", e);
    }

    return new AvailabilityDto(false);
  }

  /**
   * Scrapes the menu plan from an SV-Group website and parses it into a {@link MealPlanDto}
   *
   * @param dayOffset Offset in days into the future. E.g. if the sv-group website currently
   *     displays monday on the start page, an offset of 2 will return the meal plan for wednesday
   * @param restaurant Which restaurant website to scrape the meal plan from
   * @return The scraped {@link MealPlanDto}
   */
  public MealPlanDto getMealPlan(int dayOffset, RestaurantDto restaurant) {
    final MealPlansDto mealPlans = getMealPlans(restaurant);

    final LocalDate offsetDate =
        mealPlans.getPlans().keySet().stream()
            .sorted()
            .skip(dayOffset)
            .findFirst()
            .orElseThrow(() -> new MealPlanParsingException(NO_MEALPLAN_AVAILABLE_ERROR));

    return mealPlans.getPlans().get(offsetDate);
  }

  /**
   * Scrapes all available meal plans from an SV-Group website and parses them into a {@link
   * MealPlansDto}
   *
   * @param restaurant Which restaurant website to scrape the meal plan from
   * @return The scraped {@link MealPlanDto}
   */
  @Cacheable(MEAL_PLAN)
  @CircuitBreaker(name = SV_MENU)
  public MealPlansDto getMealPlans(RestaurantDto restaurant) {
    log.debug("Scraping meal plan for restaurant {}", restaurant);

    ResponseEntity<String> response = restTemplate.getForEntity(restaurant.getLink(), String.class);

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
