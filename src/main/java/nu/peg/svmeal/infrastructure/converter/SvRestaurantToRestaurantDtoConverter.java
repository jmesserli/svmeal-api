package nu.peg.svmeal.infrastructure.converter;

import nu.peg.svmeal.domain.model.RestaurantDto;
import nu.peg.svmeal.infrastructure.model.SvRestaurant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SvRestaurantToRestaurantDtoConverter
    implements Converter<SvRestaurant, RestaurantDto> {
  public static final String PUBLIC_TYPE = "öffentlich";

  @Override
  public RestaurantDto convert(SvRestaurant from) {
    String link = from.getLink();
    boolean isPublic = from.getType().equals(PUBLIC_TYPE);

    return new RestaurantDto(from.getName(), link, from.getShortcut(), isPublic);
  }
}
