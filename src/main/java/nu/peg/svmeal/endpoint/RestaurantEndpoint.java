package nu.peg.svmeal.endpoint;

import nu.peg.svmeal.controller.RestaurantController;
import nu.peg.svmeal.model.Response;
import nu.peg.svmeal.model.RestaurantDto;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
