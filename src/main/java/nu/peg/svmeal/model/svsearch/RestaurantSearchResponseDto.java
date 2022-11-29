package nu.peg.svmeal.model.svsearch;

import lombok.Data;

@Data
public class RestaurantSearchResponseDto {
  private EmptyObject empty;

  @Data
  public static class EmptyObject {
    private String callbackfunc;
  }
}
