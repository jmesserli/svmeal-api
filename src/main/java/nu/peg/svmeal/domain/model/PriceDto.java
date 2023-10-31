package nu.peg.svmeal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceDto {
  private double internalPrice;
  private double externalPrice;
}
