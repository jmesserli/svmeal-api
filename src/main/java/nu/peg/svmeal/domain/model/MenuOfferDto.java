package nu.peg.svmeal.domain.model;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;

@Builder
public record MenuOfferDto(
    String title,
    List<String> trimmings,
    PriceDto price,
    String provenance,
    DietaryRestriction dietaryRestriction)
    implements Serializable {}
