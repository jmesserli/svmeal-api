package nu.peg.svmeal.endpoint;

import nu.peg.svmeal.controller.MealController;
import nu.peg.svmeal.controller.RestaurantController;
import nu.peg.svmeal.model.AvailabilityDto;
import nu.peg.svmeal.model.MealPlanDto;
import nu.peg.svmeal.model.Response;
import nu.peg.svmeal.model.SvRestaurant;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class MealEndpoint {

    private MealController mealController;
    private RestaurantController restaurantController;

    public MealEndpoint() {
        mealController = new MealController();
        restaurantController = new RestaurantController();
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

        if (restaurant.isPresent())
            return mealController.getMealPlanCached(dayOffset, restaurant.get());
        else
            return new Response<>("Invalid restaurant");
    }

    @GET
    @Path("/restaurant/{restaurant}/meal/{dayOffset}/available")
    public Response<AvailabilityDto> getMealPlanAvailability(@PathParam("restaurant") String restaurantString, @PathParam("dayOffset") int dayOffset) {
        Optional<SvRestaurant> restaurant = findRestaurant(restaurantString);

        if (restaurant.isPresent())
            return mealController.getAvailability(dayOffset, restaurant.get());
        else
            return new Response<>("Invalid restaurant");
    }

    private Optional<SvRestaurant> findRestaurant(String shortcut) {
        List<SvRestaurant> restaurants = restaurantController.getRestaurantsCached();
        return restaurants.stream().filter(rest -> rest.getLink().contains(shortcut)).findFirst();
    }
}
