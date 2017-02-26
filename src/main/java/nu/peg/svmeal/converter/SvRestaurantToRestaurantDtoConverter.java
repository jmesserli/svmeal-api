package nu.peg.svmeal.converter;

import nu.peg.svmeal.model.RestaurantDto;
import nu.peg.svmeal.model.SvRestaurant;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SvRestaurantToRestaurantDtoConverter implements Converter<SvRestaurant, RestaurantDto> {
    private final Pattern linkShortcutPattern = Pattern.compile("^https?://(.*)\\.sv-restaurant\\.ch.*$");

    @Override
    public RestaurantDto convert(SvRestaurant from) {
        String link = from.getLink();

        String shortcut;
        try {
            Matcher matcher = linkShortcutPattern.matcher(link);
            //noinspection ResultOfMethodCallIgnored
            matcher.matches();
            shortcut = matcher.group(1);
        } catch (Exception e) {
            throw new RuntimeException(link, e);
        }
        //noinspection SpellCheckingInspection
        boolean _public = from.getType().equals("\u00f6ffentlich");

        return new RestaurantDto(from.getName(), link, shortcut, _public);
    }
}
