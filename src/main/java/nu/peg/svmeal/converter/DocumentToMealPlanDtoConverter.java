package nu.peg.svmeal.converter;

import nu.peg.svmeal.model.MealPlanDto;
import nu.peg.svmeal.model.MenuOfferDto;
import nu.peg.svmeal.model.PriceDto;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class DocumentToMealPlanDtoConverter implements Converter<Document, MealPlanDto> {

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
            List<MenuOfferDto> offerDtos = offers.stream().map(offer -> new MenuOfferDto(
                    offer.select(".offer-description").text(),
                    offer.select(".title").text(),
                    Arrays.stream(offer.select(".trimmings").html().split("<br>"))
                          .map(String::trim).collect(Collectors.toList()),
                    offer.select(".sidedish").text(),
                    PriceDto.fromElements(offer.select(".price-item")),
                    offer.select(".provenance").text()
            )).collect(Collectors.toList());

            return new MealPlanDto(localDate, offerDtos);
        } else {
            return null;
        }
    }
}
