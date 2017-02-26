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
public class DocumentToMealPlanDtoConverter implements Converter<Document, MealPlanDto> {
    private final Converter<Elements, PriceDto> priceDtoConverter;

    @Autowired
    public DocumentToMealPlanDtoConverter(Converter<Elements, PriceDto> priceDtoConverter) {
        this.priceDtoConverter = priceDtoConverter;
    }

    /**
     * Converts a website, parsed into a {@link Document}, to a {@link MealPlanDto}
     *
     * @param document The scraped website, parsed into a {@link Document}
     * @return The {@link Document} converted to a {@link MealPlanDto}
     */
    @Override
    public MealPlanDto convert(Document document) {
        Elements missingContent = document.select(".date-missing-content");
        if (missingContent.isEmpty()) {
            // Menu plan available here

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
        } else {
            return null;
        }
    }
}
