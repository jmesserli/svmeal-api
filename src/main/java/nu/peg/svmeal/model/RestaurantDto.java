package nu.peg.svmeal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RestaurantDto implements Serializable {
    public String name, link, shortcut;

    @JsonProperty("public")
    @SerializedName("public")
    public boolean _public;

    public RestaurantDto() {
    }

    public RestaurantDto(String name, String link, String shortcut, boolean _public) {
        this.name = name;
        this.link = link;
        this.shortcut = shortcut;
        this._public = _public;
    }

    //region Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public boolean isPublic() {
        return _public;
    }

    public void setPublic(boolean _public) {
        this._public = _public;
    }
    //endregion
}
