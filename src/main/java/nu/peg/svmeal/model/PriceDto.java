package nu.peg.svmeal.model;

import java.io.Serializable;

public class PriceDto implements Serializable {
    public double internalPrice;
    public double externalPrice;

    public PriceDto() {
    }

    public PriceDto(double internalPrice, double externalPrice) {
        this.internalPrice = internalPrice;
        this.externalPrice = externalPrice;
    }

    public double getInternalPrice() {
        return internalPrice;
    }

    public void setInternalPrice(double internalPrice) {
        this.internalPrice = internalPrice;
    }

    public double getExternalPrice() {
        return externalPrice;
    }

    public void setExternalPrice(double externalPrice) {
        this.externalPrice = externalPrice;
    }
}
