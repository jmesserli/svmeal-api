package nu.peg.svmeal.model;

import java.io.Serializable;
import java.util.List;

public class MenuOfferDto implements Serializable {
  public String title;
  public List<String> trimmings;

  public PriceDto price;
  public String provenance;

  public MenuOfferDto() {}

  public MenuOfferDto(String title, List<String> trimmings, PriceDto price, String provenance) {
    this.title = title;
    this.trimmings = trimmings;
    this.price = price;
    this.provenance = provenance;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<String> getTrimmings() {
    return trimmings;
  }

  public void setTrimmings(List<String> trimmings) {
    this.trimmings = trimmings;
  }

  public PriceDto getPrice() {
    return price;
  }

  public void setPrice(PriceDto price) {
    this.price = price;
  }
}
