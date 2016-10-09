package nu.peg.svmeal.endpoint;

import nu.peg.svmeal.controller.RestaurantController;
import nu.peg.svmeal.model.Response;
import nu.peg.svmeal.model.RestaurantDto;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class RestaurantEndpoint {
    private RestaurantController restaurantController;

    public RestaurantEndpoint() {
        this.restaurantController = new RestaurantController();
    }

    @GET
    @Path("/restaurant")
    public Response<List<RestaurantDto>> getRestaurants() {
        List<RestaurantDto> dtos;
        try {
            dtos = restaurantController.getRestaurantDtosCached();
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>("Unable to retrieve restaurants at this time.");
        }

        return new Response<>(dtos);
    }

}
