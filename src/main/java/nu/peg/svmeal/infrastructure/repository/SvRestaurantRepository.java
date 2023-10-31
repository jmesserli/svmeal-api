package nu.peg.svmeal.infrastructure.repository;

import static nu.peg.svmeal.infrastructure.config.CacheNames.RESTAURANT_DTOS;
import static nu.peg.svmeal.infrastructure.config.CircuitBreakers.SV_SEARCH;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nu.peg.svmeal.domain.exceptions.ExternalException;
import nu.peg.svmeal.domain.model.RestaurantDto;
import nu.peg.svmeal.domain.repository.RestaurantRepository;
import nu.peg.svmeal.infrastructure.model.SvRestaurant;
import nu.peg.svmeal.infrastructure.model.svsearch.RestaurantSearchResponseCallbackDto;
import nu.peg.svmeal.infrastructure.model.svsearch.RestaurantSearchResponseDto;
import nu.peg.svmeal.infrastructure.util.HttpUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class SvRestaurantRepository implements RestaurantRepository {
  private static final String RESTAURANT_SEARCH_URL =
      "https://www.sv-restaurant.ch/de/mitarbeiterrestaurants/restaurantsuche-mitarbeiterrestaurants?type=8700";

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;
  private final ConversionService converter;

  @Override
  @Cacheable(RESTAURANT_DTOS)
  @CircuitBreaker(name = SV_SEARCH)
  public List<RestaurantDto> getAllRestaurants() {
    log.info("Fetching restaurant list");
    Map<String, Object> formData = new HashMap<>();
    formData.put("searchfield", "");
    formData.put("typeofrestaurant", 0);
    formData.put("entranceregulation", 0);

    HttpEntity<MultiValueMap<String, String>> formDataEntity = HttpUtil.getPostFormData(formData);
    ResponseEntity<RestaurantSearchResponseDto> searchDtoResponse =
        restTemplate.postForEntity(
            RESTAURANT_SEARCH_URL, formDataEntity, RestaurantSearchResponseDto.class);

    String callbackFunc = searchDtoResponse.getBody().getEmpty().getCallbackfunc();
    String callbackData = callbackFunc.substring(36, callbackFunc.length() - 8);
    RestaurantSearchResponseCallbackDto searchResponseCallback;
    try {
      searchResponseCallback =
          objectMapper.readValue(callbackData, RestaurantSearchResponseCallbackDto.class);
    } catch (JsonProcessingException e) {
      throw new ExternalException("Failed to parse restaurant search response", e);
    }

    return searchResponseCallback.getList().stream()
        .filter(rest -> !rest.getLink().contains("sv-group") && !rest.getLink().isEmpty())
        .peek(SvRestaurantRepository::upgradeRestaurantLinkToHttps)
        .map(restaurant -> converter.convert(restaurant, RestaurantDto.class))
        .toList();
  }

  private static void upgradeRestaurantLinkToHttps(SvRestaurant rest) {
    try {
      URL url = new URI(rest.getLink()).toURL();

      if (url.getProtocol().equals("http")) {
        rest.setLink(String.format("https%s", rest.getLink().substring(4)));
      }
    } catch (Exception e) {
      log.warn("Failed to parse restaurant link {}", rest.getLink());
    }
  }
}
