package nu.peg.svmeal.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import nu.peg.svmeal.converter.Converter;
import nu.peg.svmeal.converter.DocumentToMealPlanDtoConverter;
import nu.peg.svmeal.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MealController {

    private Converter<Document, MealPlanDto> docToPlan;
    private MealPlanResponseCache cache;

    public MealController() {
        this.docToPlan = new DocumentToMealPlanDtoConverter();

        // values are cached for 5 minutes
        this.cache = new MealPlanResponseCache(5 * 60);
    }

    /**
     * Scrapes the menu plan from an SV-Group website and parses it into a {@link MealPlanDto}
     *
     * @param dayOffset  Offset in days into the future. E.g. if the sv-group website currently displays monday on the start page, an offset of 2 will return the meal plan for wednesday
     * @param restaurant Which restaurant website to scrape the meal plan from
     * @return The scraped {@link MealPlanDto}
     */
    @SuppressWarnings("WeakerAccess")
    public MealPlanResponse getMealPlan(int dayOffset, Restaurant restaurant) {
        System.err.printf("Scraping meal plan for %d@%s%n", dayOffset, restaurant);

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
            MealPlanDto dto = docToPlan.convert(document);

            if (dto == null) {
                return new MealPlanResponse(Response.Status.Error, "No meal plan available for this date");
            } else {
                return new MealPlanResponse(dto);
            }
        } else {
            return new MealPlanResponse(Response.Status.Error, "Internal Server Error: Request failed");
        }
    }

    /**
     * The same as {@link #getMealPlan(int, Restaurant)}, but with a cache
     *
     * @see #getMealPlan(int, Restaurant)
     */
    public MealPlanResponse getMealPlanCached(int dayOffset, Restaurant restaurant) {
        MealPlanResponse response = cache.get(dayOffset, restaurant);

        if (response == null) {
            MealPlanResponse newResponse = getMealPlan(dayOffset, restaurant);
            cache.add(dayOffset, restaurant, newResponse);

            return newResponse;
        } else {
            return response;
        }
    }
}
