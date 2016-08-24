package nu.peg.svmeal.model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;

import static nu.peg.svmeal.AppInitializer.logger;

public class PriceDto implements Serializable {

    public double internalPrice;
    public double externalPrice;

    public String comment = "internalPrice is for employees of the enterprise the restaurant belongs to, externalPrice for non-employees";

    public PriceDto() {
    }

    public PriceDto(double internalPrice, double externalPrice) {
        this.internalPrice = internalPrice;
        this.externalPrice = externalPrice;
    }

    public static PriceDto fromElements(final Elements elements) {
        final double[] intPrice = new double[1];
        final double[] extPrice = new double[1];

        elements.stream().map(Element::text).forEach(elementString ->
        {
            double price = Double.parseDouble(elementString.split(" ")[1]);

            if (elementString.startsWith("INT")) {
                intPrice[0] = price;
            } else if (elementString.startsWith("EXT")) {
                extPrice[0] = price;
            } else if (elementString.startsWith("CHF")) { // For our derpy people in Zollikofen
                intPrice[0] = price;
            } else {
                logger.warning(String.format("Found non-matching price tag: `%s`", elementString));
            }
        });

        return new PriceDto(intPrice[0], extPrice[0]);
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
