package nu.peg.svmeal.domain.model;

import java.io.Serializable;

public record PriceDto(double internalPrice, double externalPrice) implements Serializable {}
