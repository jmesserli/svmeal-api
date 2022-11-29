package nu.peg.svmeal.service;

import nu.peg.svmeal.model.AvailabilityDto;
import nu.peg.svmeal.model.MealPlanResponse;
import nu.peg.svmeal.model.Response;
import nu.peg.svmeal.model.SvRestaurant;

public interface MealService {
  Response<AvailabilityDto> getAvailability(int dayOffset, SvRestaurant restaurant);

  MealPlanResponse getMealPlan(int dayOffset, SvRestaurant restaurant);
}
