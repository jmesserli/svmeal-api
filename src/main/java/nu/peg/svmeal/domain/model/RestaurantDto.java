package nu.peg.svmeal.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public record RestaurantDto(
    String name, String link, String shortcut, @JsonProperty("public") boolean isPublic)
    implements Serializable {}
