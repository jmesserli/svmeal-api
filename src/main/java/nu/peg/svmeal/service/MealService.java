package nu.peg.svmeal.service;

import nu.peg.svmeal.model.AvailabilityDto;
import nu.peg.svmeal.model.MealPlanResponse;
import nu.peg.svmeal.model.Response;
import nu.peg.svmeal.model.SvRestaurant;

/**
 * TODO Short summary
 *
 * @author Joel Messerli @26.02.2017
 */
public interface MealService {
    Response<AvailabilityDto> getAvailability(int dayOffset, SvRestaurant restaurant);
    MealPlanResponse getMealPlan(int dayOffset, SvRestaurant restaurant);
}
