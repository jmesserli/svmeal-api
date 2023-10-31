package nu.peg.svmeal.domain.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

public record MealPlansDto(Map<LocalDate, MealPlanDto> plans) implements Serializable {}
