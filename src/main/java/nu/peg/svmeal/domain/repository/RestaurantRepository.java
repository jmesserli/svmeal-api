package nu.peg.svmeal.domain.repository;

import nu.peg.svmeal.domain.model.RestaurantDto;

import java.util.List;

public interface RestaurantRepository {
  List<RestaurantDto> getAllRestaurants();
}
