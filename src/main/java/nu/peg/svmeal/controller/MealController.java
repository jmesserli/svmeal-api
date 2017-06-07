package nu.peg.svmeal.controller;

import nu.peg.svmeal.model.AvailabilityDto;
import nu.peg.svmeal.model.MealPlanDto;
import nu.peg.svmeal.model.Response;
import nu.peg.svmeal.model.SvRestaurant;
import nu.peg.svmeal.service.MealService;
import nu.peg.svmeal.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public Response<MealPlanDto> getRestaurantMeal(@PathVariable("restaurant") String restaurantString) {
        return getRestaurantMealOffset(restaurantString, 0);
    }

    @GetMapping("/{restaurant}/meal/{dayOffset}")
    public Response<MealPlanDto> getRestaurantMealOffset(@PathVariable("restaurant") String restaurantString, @PathVariable("dayOffset") int dayOffset) {
        Optional<SvRestaurant> restaurant = findRestaurant(restaurantString);

        if (restaurant.isPresent()) {
            return mealService.getMealPlan(dayOffset, restaurant.get());
        } else {
            return new Response<>("Invalid restaurant");
        }
    }

    @GetMapping("/{restaurant}/meal/{dayOffset}/available")
    public Response<AvailabilityDto> getMealPlanAvailability(@PathVariable("restaurant") String restaurantString, @PathVariable("dayOffset") int dayOffset) {
        Optional<SvRestaurant> restaurant = findRestaurant(restaurantString);

        if (restaurant.isPresent()) {
            return mealService.getAvailability(dayOffset, restaurant.get());
        } else {
            return new Response<>("Invalid restaurant");
        }
    }

    private Optional<SvRestaurant> findRestaurant(String shortcut) {
        List<SvRestaurant> restaurants = restaurantService.getRestaurants();
        return restaurants.stream().filter(rest -> rest.getShortcut().equalsIgnoreCase(shortcut)).findFirst();
    }
}
