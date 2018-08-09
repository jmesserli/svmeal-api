package nu.peg.svmeal.service.internal;

import com.google.gson.Gson;
import nu.peg.svmeal.converter.Converter;
import nu.peg.svmeal.model.RestaurantDto;
import nu.peg.svmeal.model.SvRestaurant;
import nu.peg.svmeal.model.svsearch.RestaurantSearchResponseCallbackDto;
import nu.peg.svmeal.model.svsearch.RestaurantSearchResponseDto;
import nu.peg.svmeal.service.RestaurantService;
import nu.peg.svmeal.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nu.peg.svmeal.config.CacheRegistry.RESTAURANTS;
import static nu.peg.svmeal.config.CacheRegistry.RESTAURANT_DTOS;

@Service
public class DefaultRestaurantService implements RestaurantService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRestaurantService.class);
    private static final String RESTAURANT_SEARCH_URL = "http://www.sv-restaurant.ch/de/mitarbeiterrestaurants/restaurant-suche/?type=8700";

    private final Gson gson;
    private final Converter<SvRestaurant, RestaurantDto> restaurantConverter;
    private final RestTemplate restTemplate;

    @Autowired
    public DefaultRestaurantService(Gson gson, Converter<SvRestaurant, RestaurantDto> restaurantConverter, RestTemplate restTemplate) {
        this.gson = gson;
        this.restaurantConverter = restaurantConverter;
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable(RESTAURANTS)
    public List<SvRestaurant> getRestaurants() {
        LOGGER.info("Fetching restaurant list");
        Map<String, Object> formData = new HashMap<>();
        formData.put("searchfield", "");
        formData.put("typeofrestaurant", 1);
        formData.put("entranceregulation", 0);

        HttpEntity<MultiValueMap<String, String>> formDataEntity = HttpUtil.getPostFormData(formData);
        ResponseEntity<RestaurantSearchResponseDto> searchDtoResponse = restTemplate.postForEntity(
                RESTAURANT_SEARCH_URL,
                formDataEntity,
                RestaurantSearchResponseDto.class
        );

        String callbackFunc = searchDtoResponse.getBody().empty.callbackfunc;
        String callbackData = callbackFunc.substring(36, callbackFunc.length() - 8);
        RestaurantSearchResponseCallbackDto searchResponseCallback =
                gson.fromJson(callbackData, RestaurantSearchResponseCallbackDto.class);

        return Arrays.stream(searchResponseCallback.list)
                .filter(rest -> !rest.getLink().contains("sv-group") && !rest.getLink().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(RESTAURANT_DTOS)
    public List<RestaurantDto> getRestaurantDtos() {
        return this.getRestaurants().stream()
                .map(restaurantConverter::convert)
                .collect(Collectors.toList());
    }
}
