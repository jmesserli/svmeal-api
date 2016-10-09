package nu.peg.svmeal.endpoint;

import nu.peg.svmeal.controller.MealController;
import nu.peg.svmeal.controller.RestaurantController;
import nu.peg.svmeal.model.*;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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

    private Optional<SvRestaurant> findRestaurant(String shortcut) {
        List<SvRestaurant> restaurants = restaurantController.getRestaurantsCached();
        return restaurants.stream().filter(rest -> rest.getLink().contains(shortcut)).findFirst();
    }
}
