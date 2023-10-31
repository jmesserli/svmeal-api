package nu.peg.svmeal.domain.model;

import java.time.LocalDate;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MealPlansDto {
  private Map<LocalDate, MealPlanDto> plans;
}
