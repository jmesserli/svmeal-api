package nu.peg.svmeal.application;

import java.util.List;
import nu.peg.svmeal.domain.model.RestaurantDto;
import nu.peg.svmeal.domain.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public List<RestaurantDto> getRestaurants() {
    return restaurantService.getRestaurantDtos();
  }
}
