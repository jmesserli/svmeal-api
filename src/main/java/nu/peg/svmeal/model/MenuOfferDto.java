package nu.peg.svmeal.model;

import java.util.List;

public class MenuOfferDto {
    public String description;

    public String title;
    public List<String> trimmings;
    public String sidedish;

    public PriceDto price;
    public String provenance;

    public MenuOfferDto() {
    }

    public MenuOfferDto(String description, String title, List<String> trimmings, String sidedish, PriceDto price, String provenance) {
        this.description = description;
        this.title = title;
        this.trimmings = trimmings;
        this.sidedish = sidedish;
        this.price = price;
        this.provenance = provenance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getSidedish() {
        return sidedish;
    }

    public void setSidedish(String sidedish) {
        this.sidedish = sidedish;
    }

    public PriceDto getPrice() {
        return price;
    }

    public void setPrice(PriceDto price) {
        this.price = price;
    }
}
