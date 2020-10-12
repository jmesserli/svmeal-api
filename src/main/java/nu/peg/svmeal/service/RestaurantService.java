package nu.peg.svmeal.service;

import nu.peg.svmeal.model.RestaurantDto;
import nu.peg.svmeal.model.SvRestaurant;

import java.util.List;

public interface RestaurantService {
    List<SvRestaurant> getRestaurants();

    List<RestaurantDto> getRestaurantDtos();
}
