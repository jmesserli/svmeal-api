package nu.peg.svmeal.infrastructure.model.svsearch;

import java.util.List;
import lombok.Data;
import nu.peg.svmeal.infrastructure.model.SvRestaurant;

@Data
public class RestaurantSearchResponseCallbackDto {
  private List<SvRestaurant> list;
}
