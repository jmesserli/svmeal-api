package nu.peg.svmeal.infrastructure.converter;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nu.peg.svmeal.domain.model.PriceDto;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/** Converts a list of price elements to an internal and external price */
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
