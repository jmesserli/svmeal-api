package nu.peg.svmeal.model;

import java.util.Optional;

/**
 * Created by joel on 06.08.16.
 */
public enum Restaurant {
    BIT("http://bit.sv-restaurant.ch"),
    MOBILIAR("http://mobiliar.sv-restaurant.ch");

    String baseUrl;

    Restaurant(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public static Optional<Restaurant> fromString(String restaurantString) {

        restaurantString = restaurantString.toUpperCase();

        for (Restaurant restaurant : values()) {
            if (restaurant.name().equals(restaurantString))
                return Optional.of(restaurant);
        }

        return Optional.empty();
    }
}
