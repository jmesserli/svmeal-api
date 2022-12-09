package nu.peg.svmeal.service;

import nu.peg.svmeal.model.AvailabilityDto;
import nu.peg.svmeal.model.MealPlanDto;
import nu.peg.svmeal.model.MealPlansDto;
import nu.peg.svmeal.model.SvRestaurant;

public interface MealService {
  AvailabilityDto getAvailability(int dayOffset, SvRestaurant restaurant);

  MealPlanDto getMealPlan(int dayOffset, SvRestaurant restaurant);

  MealPlansDto getMealPlans(SvRestaurant restaurant);
}
