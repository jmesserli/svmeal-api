package nu.peg.svmeal.controller;

import com.google.gson.Gson;

import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;

import nu.peg.svmeal.AppInitializer;
import nu.peg.svmeal.converter.Converter;
import nu.peg.svmeal.converter.SvRestaurantToRestaurantDtoConverter;
import nu.peg.svmeal.model.RestaurantDto;
import nu.peg.svmeal.model.SvRestaurant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class RestaurantController {
    private Gson gson;
    private Converter<SvRestaurant, RestaurantDto> restaurantConverter;

    public RestaurantController() {
        this.gson = new Gson();
        this.restaurantConverter = new SvRestaurantToRestaurantDtoConverter();
    }

    private List<SvRestaurant> getRestaurants() {
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

    public List<SvRestaurant> getRestaurantsCached() {
        if (AppInitializer.restaurants == null)
            AppInitializer.restaurants = getRestaurants();

        return AppInitializer.restaurants;
    }

    private List<RestaurantDto> getRestaurantDtos() {
        return getRestaurantsCached().stream().map(restaurantConverter::convert).collect(Collectors.toList());
    }

    public List<RestaurantDto> getRestaurantDtosCached() {
        if (AppInitializer.restaurantDtos == null)
            AppInitializer.restaurantDtos = getRestaurantDtos();

        return AppInitializer.restaurantDtos;
    }
}
