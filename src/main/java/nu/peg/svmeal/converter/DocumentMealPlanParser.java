package nu.peg.svmeal.converter;

import nu.peg.svmeal.model.MealPlanDto;
import nu.peg.svmeal.model.MenuOfferDto;
import nu.peg.svmeal.model.PriceDto;
import nu.peg.svmeal.util.DateUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocumentMealPlanParser {
    public static final String MENU_PLAN_TAB_FORMAT = "#menu-plan-tab%d";

    private final Converter<Elements, PriceDto> priceDtoConverter;

    @Autowired
    public DocumentMealPlanParser(Converter<Elements, PriceDto> priceDtoConverter) {
        this.priceDtoConverter = priceDtoConverter;
    }

    /**
     * Converts a website, parsed into a {@link Document}, to a {@link MealPlanDto}
     *
     * @param document  The scraped website, parsed into a {@link Document}
     * @param dayOffset The day offset of the requested meal plan
     * @return The {@link Document} converted to a {@link MealPlanDto}
     */
    public MealPlanDto convert(Document document, int dayOffset) {
        Elements menuPlanTab = document.select(String.format(MENU_PLAN_TAB_FORMAT, dayOffset + 1));
        if (menuPlanTab.isEmpty()) {
            return null;
        }

        List<String> dateStrings = document.select(".day-nav ul li label span.date").stream()
                .map(Element::text)
                .collect(Collectors.toList());
        LocalDate localDate = DateUtil.tryParseDateFromRange(dateStrings, dayOffset);

        Elements offers = menuPlanTab.select(".menu-item .item-content");
        List<MenuOfferDto> offerDtos = offers.stream()
                .map(offer -> new MenuOfferDto(
                        offer.select(".menu-title").text(),
                        Arrays.stream(offer.select(".menu-description").html().split("<br>"))
                                .map(String::trim)
                                .collect(Collectors.toList()),
                        priceDtoConverter.convert(offer.select(".menu-prices")),
                        offer.select(".menu-provenance").text()
                )).collect(Collectors.toList());

        return new MealPlanDto(localDate, offerDtos);
    }
}
