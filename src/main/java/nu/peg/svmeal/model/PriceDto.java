package nu.peg.svmeal.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceDto {
  private double internalPrice;
  private double externalPrice;
}
