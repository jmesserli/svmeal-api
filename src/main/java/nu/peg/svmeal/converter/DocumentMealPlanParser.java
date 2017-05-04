package nu.peg.svmeal.converter;

import nu.peg.svmeal.model.MealPlanDto;
import nu.peg.svmeal.model.MenuOfferDto;
import nu.peg.svmeal.model.PriceDto;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocumentMealPlanParser {
    public static final String MENU_PLAN_TABS_CLASS = ".menu-plan-tabs";
    public static final String MENU_PLAN_TAB_FORMAT = ".menu-plan-tab%d";

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
        Elements menuPlanTabs = document.select(MENU_PLAN_TABS_CLASS);
        Elements menuPlanTab = menuPlanTabs.select(String.format(MENU_PLAN_TAB_FORMAT, dayOffset + 1));
        if (menuPlanTab.isEmpty()) {
            return null;
        }

        String date = document.select(".date h2").text().split(",")[1].trim();
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        Elements offers = document.select(".offer");
        List<MenuOfferDto> offerDtos = offers.stream()
                .map(offer -> new MenuOfferDto(
                        offer.select(".offer-description").text(),
                        offer.select(".title").text(),
                        Arrays.stream(offer.select(".trimmings").html().split("<br>"))
                                .map(String::trim)
                                .collect(Collectors.toList()),
                        offer.select(".sidedish").text(),
                        priceDtoConverter.convert(offer.select(".price-item")),
                        offer.select(".provenance").text()
                )).collect(Collectors.toList());

        return new MealPlanDto(localDate, offerDtos);
    }



}
