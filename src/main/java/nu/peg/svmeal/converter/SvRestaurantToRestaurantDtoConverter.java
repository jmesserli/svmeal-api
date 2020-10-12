package nu.peg.svmeal.converter;

import nu.peg.svmeal.model.RestaurantDto;
import nu.peg.svmeal.model.SvRestaurant;
import org.springframework.stereotype.Component;

@Component
public class SvRestaurantToRestaurantDtoConverter implements Converter<SvRestaurant, RestaurantDto> {
    public static final String PUBLIC_TYPE = "\u00f6ffentlich";

    @Override
    public RestaurantDto convert(SvRestaurant from) {
        String link = from.getLink();
        boolean isPublic = from.getType().equals(PUBLIC_TYPE);

        return new RestaurantDto(from.getName(), link, from.getShortcut(), isPublic);
    }
}
