package nu.peg.svmeal.model.svsearch;

import nu.peg.svmeal.model.SvRestaurant;

import javax.validation.constraints.NotNull;

public class RestaurantSearchResponseCallbackDto {
    @NotNull
    public SvRestaurant[] list;
}
