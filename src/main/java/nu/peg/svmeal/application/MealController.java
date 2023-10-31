package nu.peg.svmeal.application;

import nu.peg.svmeal.domain.exceptions.UnknownRestaurantException;
import nu.peg.svmeal.domain.model.AvailabilityDto;
import nu.peg.svmeal.domain.model.MealPlanDto;
import nu.peg.svmeal.domain.model.MealPlansDto;
import nu.peg.svmeal.domain.model.RestaurantDto;
import nu.peg.svmeal.domain.service.MealService;
import nu.peg.svmeal.domain.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/restaurant")
public class MealController {
  private final MealService mealService;
  private final RestaurantService restaurantService;

  @Autowired
  public MealController(MealService mealService, RestaurantService restaurantService) {
    this.mealService = mealService;
    this.restaurantService = restaurantService;
  }

  @GetMapping("/{restaurant}/meal")
  public MealPlansDto getRestaurantMeal(@PathVariable("restaurant") String restaurantString) {
    return mealService.getMealPlans(findRestaurant(restaurantString));
  }

  @GetMapping("/{restaurant}/meal/{dayOffset}")
  public MealPlanDto getRestaurantMealOffset(
      @PathVariable("restaurant") String restaurantString,
      @PathVariable("dayOffset") int dayOffset) {

    return mealService.getMealPlan(dayOffset, findRestaurant(restaurantString));
  }

  @GetMapping("/{restaurant}/meal/{dayOffset}/available")
  public AvailabilityDto getMealPlanAvailability(
      @PathVariable("restaurant") String restaurantString,
      @PathVariable("dayOffset") int dayOffset) {

    return mealService.getAvailability(dayOffset, findRestaurant(restaurantString));
  }

  private RestaurantDto findRestaurant(String shortcut) {
    return restaurantService.getRestaurantDtos().stream()
        .filter(rest -> rest.getShortcut().equalsIgnoreCase(shortcut))
        .findFirst()
        .orElseThrow(
            () ->
                new UnknownRestaurantException(String.format("Unknown restaurant: %s", shortcut)));
  }
}
