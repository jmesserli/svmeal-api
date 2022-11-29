package nu.peg.svmeal.service;

import java.util.List;
import nu.peg.svmeal.model.RestaurantDto;
import nu.peg.svmeal.model.SvRestaurant;

public interface RestaurantService {
  List<SvRestaurant> getRestaurants();

  List<RestaurantDto> getRestaurantDtos();
}
