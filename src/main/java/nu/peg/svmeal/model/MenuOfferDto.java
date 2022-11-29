package nu.peg.svmeal.model;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuOfferDto implements Serializable {
  private String title;
  private List<String> trimmings;

  private PriceDto price;
  private String provenance;
}
