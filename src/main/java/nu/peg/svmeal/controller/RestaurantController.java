package nu.peg.svmeal.controller;

import nu.peg.svmeal.model.Response;
import nu.peg.svmeal.model.RestaurantDto;
import nu.peg.svmeal.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("")
    public Response<List<RestaurantDto>> getRestaurants() {
        return new Response<>(restaurantService.getRestaurantDtos());
    }
}
