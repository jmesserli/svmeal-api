package nu.peg.svmeal.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SvRestaurant {
    private final static Pattern linkShortcutPattern = Pattern.compile("^https?://(.*)\\.sv-restaurant\\.ch.*$");

    private String id, name, address, type, distance, distanceRender, lat, lng, link, linkLabel;
    private boolean rendered;

    private transient String shortcut;

    public SvRestaurant() {
    }

    public SvRestaurant(String id, String name, String address, String type, String distance, String distanceRender, String lat, String lng, String link, String linkLabel, boolean rendered) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.distance = distance;
        this.distanceRender = distanceRender;
        this.lat = lat;
        this.lng = lng;
        this.link = link;
        this.linkLabel = linkLabel;
        this.rendered = rendered;
    }

    public String getShortcut() {
        if (shortcut != null) {
            return shortcut;
        }

        Matcher matcher = linkShortcutPattern.matcher(link);
        if (!matcher.matches()) {
            return (shortcut = (id + "-" + name).replaceAll("[ ]*", ""));
        }

        return (shortcut = matcher.group(1));
    }

    //region Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistanceRender() {
        return distanceRender;
    }

    public void setDistanceRender(String distanceRender) {
        this.distanceRender = distanceRender;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkLabel() {
        return linkLabel;
    }

    public void setLinkLabel(String linkLabel) {
        this.linkLabel = linkLabel;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }
    //endregion
}
