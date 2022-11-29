package nu.peg.svmeal.converter;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nu.peg.svmeal.model.PriceDto;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * TODO Short summary
 *
 * @author Joel Messerli @26.02.2017
 */
@Slf4j
@Component
public class ElementsToPriceDtoConverter implements Converter<Elements, PriceDto> {
  private static final List<String> INTERNAL_PRICE_DESC_STRS = Arrays.asList("INT", "CHF", "M.FH");

  @Override
  public PriceDto convert(Elements from) {
    double intPrice = 0;
    double extPrice = 0;

    Elements priceSpans = from.select(".price");
    for (Element priceSpan : priceSpans) {
      String desc = priceSpan.select(".desc").text();
      double value;
      try {
        value = Double.parseDouble(priceSpan.select(".val").text());
      } catch (NumberFormatException nfe) {
        log.info("Error when parsing price value", nfe);
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
