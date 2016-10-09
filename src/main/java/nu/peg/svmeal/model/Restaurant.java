package nu.peg.svmeal.model;

import java.util.Optional;

public enum Restaurant {
    BIT("http://bit.sv-restaurant.ch"),
    MOBILIAR("http://mobiliar.sv-restaurant.ch");

    final String baseUrl;

    Restaurant(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static Optional<Restaurant> fromString(String restaurantString) {
        restaurantString = restaurantString.toUpperCase();

        for (Restaurant restaurant : values()) {
            if (restaurant.name().equals(restaurantString))
                return Optional.of(restaurant);
        }

        return Optional.empty();
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
