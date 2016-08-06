package nu.peg.svmeal.endpoint;

import nu.peg.svmeal.controller.MealController;
import nu.peg.svmeal.model.MealPlanDto;
import nu.peg.svmeal.model.MealPlanResponse;
import nu.peg.svmeal.model.Response;
import nu.peg.svmeal.model.Restaurant;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

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
        Optional<Restaurant> restaurant = Restaurant.fromString(restaurantString);

        if (restaurant.isPresent())
            return controller.getMealPlan(0, restaurant.get());
        else
            return new Response<>(Response.Status.Error, "Invalid restaurant");
    }

    @GET
    @Path("/restaurant/{restaurant}/meal/{dayOffset}")
    public Response<MealPlanDto> getRestaurantMealOffset(@PathParam("restaurant") String restaurantString, @PathParam("dayOffset") int dayOffset) {
        Optional<Restaurant> restaurant = Restaurant.fromString(restaurantString);


        if (restaurant.isPresent())
            return controller.getMealPlan(dayOffset, restaurant.get());
        else
            return new Response<>(Response.Status.Error, "Invalid restaurant");
    }
}
