package nu.peg.svmeal.controller;

import java.util.List;
import nu.peg.svmeal.exceptions.UnknownRestaurantException;
import nu.peg.svmeal.model.AvailabilityDto;
import nu.peg.svmeal.model.MealPlanDto;
import nu.peg.svmeal.model.MealPlansDto;
import nu.peg.svmeal.model.SvRestaurant;
import nu.peg.svmeal.service.MealService;
import nu.peg.svmeal.service.RestaurantService;
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

  private SvRestaurant findRestaurant(String shortcut) {
    List<SvRestaurant> restaurants = restaurantService.getRestaurants();
    return restaurants.stream()
        .filter(rest -> rest.getShortcut().equalsIgnoreCase(shortcut))
        .findFirst()
        .orElseThrow(
            () ->
                new UnknownRestaurantException(String.format("Unknown restaurant: %s", shortcut)));
  }
}
