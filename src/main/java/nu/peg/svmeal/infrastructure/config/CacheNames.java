package nu.peg.svmeal.infrastructure.config;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class CacheNames {
  public static final String RESTAURANTS = "restaurants";
  public static final String RESTAURANT_DTOS = "restaurantDtos";
  public static final String MEAL_PLAN = "mealPlan";
}
