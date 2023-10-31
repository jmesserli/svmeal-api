package nu.peg.svmeal.domain.repository;

import nu.peg.svmeal.domain.model.MealPlansDto;
import nu.peg.svmeal.domain.model.RestaurantDto;

public interface MealPlanRepository {
  MealPlansDto getMealPlans(RestaurantDto restaurant);
}
