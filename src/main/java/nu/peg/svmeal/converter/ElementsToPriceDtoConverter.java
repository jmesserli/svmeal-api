package nu.peg.svmeal.converter;

import nu.peg.svmeal.model.PriceDto;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * TODO Short summary
 *
 * @author Joel Messerli @26.02.2017
 */
@Component
public class ElementsToPriceDtoConverter implements Converter<Elements, PriceDto> {
    private static Logger LOGGER = LoggerFactory.getLogger(ElementsToPriceDtoConverter.class);

    @Override
    public PriceDto convert(Elements from) {
        final double[] intPrice = new double[1];
        final double[] extPrice = new double[1];

        from.stream().map(Element::text).forEach(elementString -> {
            double price = Double.parseDouble(elementString.split(" ")[1]);

            if (elementString.startsWith("INT")) {
                intPrice[0] = price;
            } else if (elementString.startsWith("EXT")) {
                extPrice[0] = price;
            } else if (elementString.startsWith("CHF")) { // For our derpy people in Zollikofen
                intPrice[0] = price;
            } else {
                LOGGER.warn(String.format("Found non-matching price tag: `%s`", elementString));
            }
        });

        return new PriceDto(intPrice[0], extPrice[0]);
    }
}
