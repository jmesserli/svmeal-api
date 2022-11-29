package nu.peg.svmeal.model;

public class MealPlanResponse extends Response<MealPlanDto> {

  public MealPlanResponse() {}

  public MealPlanResponse(MealPlanDto data) {
    super(data);
  }

  public MealPlanResponse(String error) {
    super(error);
  }

  public MealPlanResponse(Status status, MealPlanDto data, String error) {
    super(status, data, error);
  }
}
