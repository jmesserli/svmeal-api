package nu.peg.svmeal.domain.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nu.peg.svmeal.domain.exceptions.ExternalException;
import nu.peg.svmeal.domain.exceptions.MealPlanParsingException;
import nu.peg.svmeal.domain.model.AvailabilityDto;
import nu.peg.svmeal.domain.model.MealPlanDto;
import nu.peg.svmeal.domain.model.MealPlansDto;
import nu.peg.svmeal.domain.model.RestaurantDto;
import nu.peg.svmeal.domain.repository.MealPlanRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealService {
  private static final String NO_MEAL_PLAN_FOR_DAY = "No meal plan available for this day";

  private final MealPlanRepository repository;

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
    final var mealPlans = repository.getMealPlans(restaurant);

    final LocalDate offsetDate =
        mealPlans.getPlans().keySet().stream()
            .sorted()
            .skip(dayOffset)
            .findFirst()
            .orElseThrow(() -> new MealPlanParsingException(NO_MEAL_PLAN_FOR_DAY));

    return mealPlans.getPlans().get(offsetDate);
  }

  public MealPlansDto getMealPlans(RestaurantDto restaurant) {
    return repository.getMealPlans(restaurant);
  }
}
