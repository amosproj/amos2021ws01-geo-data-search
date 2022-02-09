package com.example.backend.api.here.data;

import com.example.backend.api.here.HereApiElement;
import com.google.gson.annotations.SerializedName;

public class RoadInfo implements HereApiElement {

    @SerializedName("value")
    public String roadName;
    @SerializedName("language")
    public String language;

    public RoadInfo(String roadName, String language) {
        this.roadName = roadName;
        this.language = language;
    }

    public String toString(String tab) {
        return "\n" + tab + "RoadInfo{" +
                "\n" + tab + "\troadName = \"" + roadName + "\"" +
                "\n" + tab + "\tlanguage = \"" + language + "\"" +
                "\n" + tab + "\t}";
    }
}