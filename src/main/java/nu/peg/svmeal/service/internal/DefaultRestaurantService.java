package nu.peg.svmeal.service.internal;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import nu.peg.svmeal.converter.Converter;
import nu.peg.svmeal.model.RestaurantDto;
import nu.peg.svmeal.model.SvRestaurant;
import nu.peg.svmeal.service.RestaurantService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultRestaurantService implements RestaurantService {
    private final Gson gson;
    private final Converter<SvRestaurant, RestaurantDto> restaurantConverter;

    @Inject
    public DefaultRestaurantService(Gson gson, Converter<SvRestaurant, RestaurantDto> restaurantConverter) {
        this.gson = gson;
        this.restaurantConverter = restaurantConverter;
    }

    @Override
    @Cacheable("restaurants")
    public List<SvRestaurant> getRestaurants() {
        Map<String, Object> formData = new HashMap<>();
        formData.put("searchfield", "");
        formData.put("typeofrestaurant", 1);
        formData.put("entranceregulation", 0);

        HttpResponse<JsonNode> jsonResponse;
        try {
            jsonResponse = Unirest.post("http://www.sv-restaurant.ch/de/personalrestaurants/restaurant-suche/?type=8700")
                    .fields(formData).asJson();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

        JSONObject rootObject = jsonResponse.getBody().getObject();
        JSONObject empty = rootObject.getJSONObject("empty");
        String callbackFunc = empty.getString("callbackfunc");

        String callbackData = callbackFunc.substring(36, callbackFunc.length() - 8);
        JSONObject callbackDataObject = new JSONObject(callbackData);
        JSONArray restaurantList = callbackDataObject.getJSONArray("list");

        SvRestaurant[] restaurants = gson.fromJson(restaurantList.toString(), SvRestaurant[].class);
        return Arrays.asList(restaurants).stream()
                .filter(rest -> !rest.getLink().contains("sv-group") && !rest.getLink().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable("restaurantDtos")
    public List<RestaurantDto> getRestaurantDtos() {
        return this.getRestaurants().stream().map(restaurantConverter::convert).collect(Collectors.toList());
    }
}
