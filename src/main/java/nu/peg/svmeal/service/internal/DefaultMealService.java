package nu.peg.svmeal.service.internal;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import nu.peg.svmeal.converter.DocumentMealPlanParser;
import nu.peg.svmeal.model.*;
import nu.peg.svmeal.service.MealService;
import nu.peg.svmeal.util.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static nu.peg.svmeal.util.CacheRegistry.MEAL_PLAN;

@Service
public class DefaultMealService implements MealService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMealService.class);

    private final DocumentMealPlanParser docParser;
    private final String NO_MEALPLAN_AVAILABLE_ERROR = "No meal plan available for this date";

    @Autowired
    public DefaultMealService(DocumentMealPlanParser docParser) {
        this.docParser = docParser;
    }

    /**
     * Checks if a meal plan is available for the given dayOffset and restaurant
     *
     * @see #getMealPlan(int, SvRestaurant)
     */
    @Override
    public Response<AvailabilityDto> getAvailability(int dayOffset, SvRestaurant restaurant) {
        MealPlanResponse response = getMealPlan(dayOffset, restaurant);

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
    @Cacheable(MEAL_PLAN)
    public MealPlanResponse getMealPlan(int dayOffset, SvRestaurant restaurant) {
        LOGGER.debug("Scraping meal plan for {}@{}", dayOffset, restaurant);

        HttpResponse<String> response;
        try {
            String restaurantLink = HttpUtil.followRedirectsAndGetUrl(restaurant.getLink());

            response = Unirest.get(restaurantLink).asString();
        } catch (UnirestException e) {
            LOGGER.warn("Exception while requesting meal plan", e);
            return new MealPlanResponse("Internal Server Error: UnirestException");
        }

        if (response.getStatus() == 200) {
            Document document = Jsoup.parse(response.getBody());
            MealPlanDto dto = docParser.convert(document, dayOffset);

            if (dto == null) {
                return new MealPlanResponse(NO_MEALPLAN_AVAILABLE_ERROR);
            } else {
                return new MealPlanResponse(dto);
            }
        } else {
            return new MealPlanResponse("Internal Server Error: Request failed");
        }
    }
}
