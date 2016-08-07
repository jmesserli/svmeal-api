package nu.peg.svmeal.endpoint;

import nu.peg.svmeal.controller.MealController;
import nu.peg.svmeal.model.MealPlanDto;
import nu.peg.svmeal.model.Response;
import nu.peg.svmeal.model.Restaurant;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class MealEndpoint {

    private MealController controller;

    public MealEndpoint() {
        controller = new MealController();
    }

    @GET
    @Path("/restaurant/{restaurant}/meal")
    public Response<MealPlanDto> getRestaurantMeal(@PathParam("restaurant") String restaurantString) {
        return getRestaurantMealOffset(restaurantString, 0);
    }

    @GET
    @Path("/restaurant/{restaurant}/meal/{dayOffset}")
    public Response<MealPlanDto> getRestaurantMealOffset(@PathParam("restaurant") String restaurantString, @PathParam("dayOffset") int dayOffset) {
        Optional<Restaurant> restaurant = Restaurant.fromString(restaurantString);

        if (restaurant.isPresent())
            return controller.getMealPlanCached(dayOffset, restaurant.get());
        else
            return new Response<>("Invalid restaurant");
    }
}
