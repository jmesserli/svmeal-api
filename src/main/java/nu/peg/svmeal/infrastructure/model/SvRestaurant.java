package nu.peg.svmeal.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.Getter;

@Data
public class SvRestaurant {
  private static final Pattern linkShortcutPattern =
      Pattern.compile("^https?://(.*)\\.sv-restaurant\\.ch.*$");

  private String id;
  private String name;
  private String address;
  private String type;
  private String distance;
  private String distanceRender;
  private String lat;
  private String lng;
  private String link;
  private String linkLabel;
  private boolean rendered;

  @JsonIgnore
  @Getter(lazy = true)
  private final String shortcut = makeShortcut();

  private String makeShortcut() {
    Matcher matcher = linkShortcutPattern.matcher(link);
    if (!matcher.matches()) {
      return (id + "-" + name).replaceAll(" *", "");
    } else {
      return matcher.group(1);
    }
  }
}
