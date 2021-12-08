package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

public class Place implements HereApiElement {

    @SerializedName("type")
    public String type;
    @SerializedName("location")
    public Position location;
    @SerializedName("originalLocation")
    public Position originalLocation;

    public Place(String type, Position location, Position originalLocation) {
        this.type = type;
        this.location = location;
        this.originalLocation = originalLocation;
    }

    public String toString(String tab) {
        return "\n" + tab + "Place{" +
                "\n" + tab + "\ttype = \"" + type + "\"" +
                "\n" + tab + "\tlocation = " + print(location, tab) +
                "\n" + tab + "\toriginalLocation = " + print(originalLocation, tab) +
                "\n" + tab + "\t}";
    }

    private String print(HereApiElement element, String tab) {
        if (element != null) {
            return element.toString(tab + "\t\t");
        } else {
            return "null";
        }
    }
}