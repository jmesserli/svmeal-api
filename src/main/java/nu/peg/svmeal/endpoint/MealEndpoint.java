package nu.peg.svmeal.endpoint;

import nu.peg.svmeal.model.AvailabilityDto;
import nu.peg.svmeal.model.MealPlanDto;
import nu.peg.svmeal.model.Response;
import nu.peg.svmeal.model.SvRestaurant;
import nu.peg.svmeal.service.MealService;
import nu.peg.svmeal.service.RestaurantService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Component
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class MealEndpoint {
    private final MealService mealService;
    private final RestaurantService restaurantService;

    @Inject
    public MealEndpoint(MealService mealService, RestaurantService restaurantService) {
        this.mealService = mealService;
        this.restaurantService = restaurantService;
    }

    @GET
    @Path("/restaurant/{restaurant}/meal")
    public Response<MealPlanDto> getRestaurantMeal(@PathParam("restaurant") String restaurantString) {
        return getRestaurantMealOffset(restaurantString, 0);
    }

    @GET
    @Path("/restaurant/{restaurant}/meal/{dayOffset}")
    public Response<MealPlanDto> getRestaurantMealOffset(@PathParam("restaurant") String restaurantString, @PathParam("dayOffset") int dayOffset) {
        Optional<SvRestaurant> restaurant = findRestaurant(restaurantString);

        if (restaurant.isPresent()) {
            return mealService.getMealPlan(dayOffset, restaurant.get());
        } else {
            return new Response<>("Invalid restaurant");
        }
    }

    @GET
    @Path("/restaurant/{restaurant}/meal/{dayOffset}/available")
    public Response<AvailabilityDto> getMealPlanAvailability(@PathParam("restaurant") String restaurantString, @PathParam("dayOffset") int dayOffset) {
        Optional<SvRestaurant> restaurant = findRestaurant(restaurantString);

        if (restaurant.isPresent()) {
            return mealService.getAvailability(dayOffset, restaurant.get());
        } else {
            return new Response<>("Invalid restaurant");
        }
    }

    private Optional<SvRestaurant> findRestaurant(String shortcut) {
        List<SvRestaurant> restaurants = restaurantService.getRestaurants();
        return restaurants.stream().filter(rest -> rest.getLink().contains(shortcut)).findFirst();
    }
}
