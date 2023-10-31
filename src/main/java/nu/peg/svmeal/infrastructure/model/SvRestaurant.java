package nu.peg.svmeal.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.Value;
import lombok.With;

@Value
public class SvRestaurant {
  private static final Pattern linkShortcutPattern =
      Pattern.compile("^https?://(.*)\\.sv-restaurant\\.ch.*$");

  String id;
  String name;
  String address;
  String type;
  String distance;
  String distanceRender;
  String lat;
  String lng;
  @With String link;
  String linkLabel;
  boolean rendered;

  @JsonIgnore
  @Getter(lazy = true)
  String shortcut = makeShortcut();

  private String makeShortcut() {
    Matcher matcher = linkShortcutPattern.matcher(link);
    if (!matcher.matches()) {
      return (id + "-" + name).replaceAll(" *", "");
    } else {
      return matcher.group(1);
    }
  }
}
