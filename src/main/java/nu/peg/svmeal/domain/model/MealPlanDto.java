package nu.peg.svmeal.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record MealPlanDto(
    @JsonSerialize(using = ToStringSerializer.class) LocalDate date, List<MenuOfferDto> offers)
    implements Serializable {}
