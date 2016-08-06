package nu.peg.svmeal.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import nu.peg.svmeal.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("Since15")
public class MealController {

    public MealPlanResponse getMealPlan(int dayOffset, Restaurant restaurant) {
        HttpResponse<String> response;
        try {
            response = Unirest.get(restaurant.getBaseUrl() + "/de/menuplan.html")
                              .queryString("addGP[shift]", dayOffset)
                              .asString();
        } catch (UnirestException e) {
            return new MealPlanResponse(Response.Status.Error, "Internal Server Error: UnirestException");
        }

        if (response.getStatus() == 200) {
            Document document = Jsoup.parse(response.getBody());
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

                return new MealPlanResponse(new MealPlanDto(localDate, offerDtos));
            } else {
                return new MealPlanResponse(Response.Status.Error, "No menu plan available for this date");
            }
        } else {
            return new MealPlanResponse(Response.Status.Error, "Internal Server Error: Request failed");
        }

    }

}
