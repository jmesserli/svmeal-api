package nu.peg.svmeal.service.internal;

import nu.peg.svmeal.converter.DocumentMealPlanParser;
import nu.peg.svmeal.model.*;
import nu.peg.svmeal.service.MealService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static nu.peg.svmeal.config.CacheRegistry.MEAL_PLAN;

@Service
public class DefaultMealService implements MealService {
    private static final String NO_MEALPLAN_AVAILABLE_ERROR = "No meal plan available for this date";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMealService.class);

    private final DocumentMealPlanParser docParser;
    private final RestTemplate restTemplate;

    @Autowired
    public DefaultMealService(DocumentMealPlanParser docParser, RestTemplate restTemplate) {
        this.docParser = docParser;
        this.restTemplate = restTemplate;
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

        ResponseEntity<String> response = restTemplate.getForEntity(restaurant.getLink(), String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
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
