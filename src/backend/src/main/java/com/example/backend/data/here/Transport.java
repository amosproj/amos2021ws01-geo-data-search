package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

public class Transport implements HereApiElement{

    @SerializedName("mode")
    public final String mode;

    public Transport(String mode) {
        this.mode = mode;
    }

    public String toString(String tab) {
        return "\n" + tab + "Transport{" +
                "\n" + tab + "\tmode = \"" + mode + "\"" +
                "\n" + tab + "\t}";
    }
}