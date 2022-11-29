package nu.peg.svmeal.model.svsearch;

import java.util.List;
import lombok.Data;
import nu.peg.svmeal.model.SvRestaurant;

@Data
public class RestaurantSearchResponseCallbackDto {
  private List<SvRestaurant> list;
}
