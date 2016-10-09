package nu.peg.svmeal.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Since15")
public class MealPlanDto implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    public LocalDate date;
    public List<MenuOfferDto> offers;

    public MealPlanDto() {
        offers = new ArrayList<>();
    }

    public MealPlanDto(LocalDate date, List<MenuOfferDto> offers) {
        this.date = date;
        this.offers = offers;
    }

    public void addOffer(MenuOfferDto offer) {
        offers.add(offer);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<MenuOfferDto> getOffers() {
        return offers;
    }

    public void setOffers(List<MenuOfferDto> offers) {
        this.offers = offers;
    }
}
