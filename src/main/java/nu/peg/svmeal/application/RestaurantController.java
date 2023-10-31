package nu.peg.svmeal.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nu.peg.svmeal.domain.model.RestaurantDto;
import nu.peg.svmeal.domain.service.RestaurantService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
  private final RestaurantService restaurantService;

  @GetMapping("")
  public List<RestaurantDto> getRestaurants() {
    return restaurantService.getRestaurantDtos();
  }
}
