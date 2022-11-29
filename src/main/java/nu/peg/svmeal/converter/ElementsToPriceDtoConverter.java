package nu.peg.svmeal.converter;

import java.util.Arrays;
import java.util.List;
import nu.peg.svmeal.model.PriceDto;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * TODO Short summary
 *
 * @author Joel Messerli @26.02.2017
 */
@Component
public class ElementsToPriceDtoConverter implements Converter<Elements, PriceDto> {
  private static Logger LOGGER = LoggerFactory.getLogger(ElementsToPriceDtoConverter.class);
  private static List<String> INTERNAL_PRICE_DESC_STRS = Arrays.asList("INT", "CHF");

  @Override
  public PriceDto convert(Elements from) {
    double intPrice = 0, extPrice = 0;

    Elements priceSpans = from.select(".price");
    for (Element priceSpan : priceSpans) {
      String desc = priceSpan.select(".desc").text();
      double value;
      try {
        value = Double.parseDouble(priceSpan.select(".val").text());
      } catch (NumberFormatException nfe) {
        LOGGER.info("Error when parsing price value", nfe);
        continue;
      }

      if (INTERNAL_PRICE_DESC_STRS.contains(desc)) {
        intPrice = value;
      } else {
        extPrice = value;
      }
    }

    return new PriceDto(intPrice, extPrice);
  }
}
