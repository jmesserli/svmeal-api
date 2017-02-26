package nu.peg.svmeal.service;

import nu.peg.svmeal.model.RestaurantDto;
import nu.peg.svmeal.model.SvRestaurant;

import java.util.List;

/**
 * TODO Short summary
 *
 * @author Joel Messerli @26.02.2017
 */
public interface RestaurantService {
    List<SvRestaurant> getRestaurants();
    List<RestaurantDto> getRestaurantDtos();
}
