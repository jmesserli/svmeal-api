package nu.peg.svmeal.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MealPlanDto implements Serializable {
  @JsonSerialize(using = ToStringSerializer.class)
  private LocalDate date;

  private List<MenuOfferDto> offers;
}
