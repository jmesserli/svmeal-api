package nu.peg.svmeal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantDto implements Serializable {
  private String name;
  private String link;
  private String shortcut;

  @JsonProperty("public")
  private boolean isPublic;
}
