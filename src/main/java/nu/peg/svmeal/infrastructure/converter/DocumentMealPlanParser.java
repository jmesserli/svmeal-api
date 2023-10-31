package nu.peg.svmeal.infrastructure.converter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nu.peg.svmeal.domain.model.*;
import nu.peg.svmeal.infrastructure.util.DateUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component
public class DocumentMealPlanParser {
  public static final String MENU_PLAN_TAB_FORMAT = "#menu-plan-tab%d";

  private final ConversionService conversionService;

  @Autowired
  public DocumentMealPlanParser(ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  /**
   * Converts a website, parsed into a {@link Document}, to a {@link MealPlansDto} containing all
   * available meal plans
   *
   * @param document The scraped website, parsed into a {@link Document}
   * @return The {@link Document} converted to a {@link MealPlansDto}
   */
  public MealPlansDto convert(Document document) {
    Map<LocalDate, MealPlanDto> plans = new HashMap<>();

    List<String> dateStrings =
        document.select(".day-nav ul li label span.date").stream()
            .map(Element::text)
            .collect(Collectors.toList());

    for (int dayOffset = 0; dayOffset < 7; dayOffset++) {
      Elements menuPlanTab = document.select(String.format(MENU_PLAN_TAB_FORMAT, dayOffset + 1));
      if (menuPlanTab.isEmpty()) {
        break;
      }

      LocalDate localDate = DateUtil.tryParseDateFromRange(dateStrings, dayOffset);

      Elements offers = menuPlanTab.select(".menu-item .item-content");
      List<MenuOfferDto> offerDtos =
          offers.stream()
              .map(
                  offer ->
                      MenuOfferDto.builder()
                          .title(offer.select(".menu-title").text())
                          .trimmings(
                              Arrays.stream(offer.select(".menu-description").html().split("<br>"))
                                  .map(String::trim)
                                  .collect(Collectors.toList()))
                          .price(
                              conversionService.convert(
                                  offer.select(".menu-prices"), PriceDto.class))
                          .provenance(offer.select(".menu-provenance").text())
                          .dietaryRestriction(extractDietaryRestriction(offer))
                          .build())
              .collect(Collectors.toList());

      plans.put(localDate, new MealPlanDto(localDate, offerDtos));
    }

    if (plans.isEmpty()) {
      return null;
    }

    return new MealPlansDto(plans);
  }

  private DietaryRestriction extractDietaryRestriction(Element offer) {
    final var labels = offer.selectFirst(".menu-labels");
    if (labels == null) {
      return null;
    }

    for (Element child : labels.children()) {
      final var dietaryRestriction =
          DietaryRestriction.determineDietaryRestrictionByClassNames(child.classNames());

      if (dietaryRestriction != null) {
        return dietaryRestriction;
      }
    }
    return null;
  }
}
