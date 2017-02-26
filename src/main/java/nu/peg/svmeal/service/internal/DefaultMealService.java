package nu.peg.svmeal.service.internal;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import nu.peg.svmeal.converter.Converter;
import nu.peg.svmeal.converter.DocumentToMealPlanDtoConverter;
import nu.peg.svmeal.model.*;
import nu.peg.svmeal.service.MealService;
import nu.peg.svmeal.util.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.logging.Level;

import static nu.peg.svmeal.AppInitializer.logger;

@Service
public class DefaultMealService implements MealService {
    private final Converter<Document, MealPlanDto> docToPlan;
    private final MealPlanResponseCache cache;
    private final String NO_MEALPLAN_AVAILABLE_ERROR = "No meal plan available for this date";

    public DefaultMealService() {
        this.docToPlan = new DocumentToMealPlanDtoConverter();

        // values are cached for 5 minutes if not otherwise specified
        int cacheSeconds = 5 * 60;

        try {
            String timeoutString = System.getProperty("cacheTimeout");
            if (timeoutString != null && !timeoutString.isEmpty()) {
                cacheSeconds = Integer.parseInt(timeoutString);
            }
        } catch (NumberFormatException nfe) {
            logger.log(Level.WARNING, "Illegal cache timeout in seconds passed", nfe);
        }

        this.cache = new MealPlanResponseCache(cacheSeconds);
    }

    /**
     * Checks if a meal plan is available for the given dayOffset and restaurant
     *
     * @see #getMealPlan(int, SvRestaurant)
     */
    @Override
    public Response<AvailabilityDto> getAvailability(int dayOffset, SvRestaurant restaurant) {
        MealPlanResponse response = getMealPlanCached(dayOffset, restaurant);

        boolean available = response.getStatus() != Response.Status.Error || !response.getError()
                .equals(NO_MEALPLAN_AVAILABLE_ERROR);
        return new Response<>(new AvailabilityDto(available));
    }

    /**
     * Scrapes the menu plan from an SV-Group website and parses it into a {@link MealPlanDto}
     *
     * @param dayOffset  Offset in days into the future. E.g. if the sv-group website currently
     *                   displays monday on the start page, an offset of 2 will return the meal plan
     *                   for wednesday
     * @param restaurant Which restaurant website to scrape the meal plan from
     * @return The scraped {@link MealPlanDto}
     */
    @Override
    @SuppressWarnings("WeakerAccess")
    public MealPlanResponse getMealPlan(int dayOffset, SvRestaurant restaurant) {
        logger.fine(String.format("Scraping meal plan for %d@%s", dayOffset, restaurant));

        HttpResponse<String> response;
        try {
            String restaurantLink = HttpUtil.followRedirectsAndGetUrl(restaurant.getLink());

            response = Unirest.get(restaurantLink)
                    .queryString("addGP[shift]", dayOffset)
                    .asString();
        } catch (UnirestException e) {
            logger.warning("Exception while requesting meal plan");
            return new MealPlanResponse("Internal Server Error: UnirestException");
        }

        if (response.getStatus() == 200) {
            Document document = Jsoup.parse(response.getBody());
            MealPlanDto dto = docToPlan.convert(document);

            if (dto == null) {
                return new MealPlanResponse(NO_MEALPLAN_AVAILABLE_ERROR);
            } else {
                return new MealPlanResponse(dto);
            }
        } else {
            return new MealPlanResponse("Internal Server Error: Request failed");
        }
    }

    /**
     * The same as {@link #getMealPlan(int, SvRestaurant)}, but with a cache
     *
     * @see #getMealPlan(int, SvRestaurant)
     */
    public MealPlanResponse getMealPlanCached(int dayOffset, SvRestaurant restaurant) {
        MealPlanResponse response = cache.get(dayOffset, restaurant);

        if (response == null) {
            logger.fine(String.format("Value not cached: %d@%s, scraping", dayOffset, restaurant));
            MealPlanResponse newResponse = getMealPlan(dayOffset, restaurant);
            cache.add(dayOffset, restaurant, newResponse);

            return newResponse;
        } else {
            logger.finer(String.format("Returning cached response for %d@%s: %s", dayOffset, restaurant, response));
            return response;
        }
    }
}
