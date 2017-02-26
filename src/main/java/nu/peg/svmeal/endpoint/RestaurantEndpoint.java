package nu.peg.svmeal.endpoint;

import nu.peg.svmeal.model.Response;
import nu.peg.svmeal.model.RestaurantDto;
import nu.peg.svmeal.service.RestaurantService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class RestaurantEndpoint {
    private final RestaurantService restaurantService;

    @Inject
    public RestaurantEndpoint(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GET
    @Path("/restaurant")
    public Response<List<RestaurantDto>> getRestaurants() {
        List<RestaurantDto> restaurantDtos;
        try {
            restaurantDtos = restaurantService.getRestaurantDtos();
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>("Unable to retrieve restaurants at this time.");
        }

        return new Response<>(restaurantDtos);
    }
}
