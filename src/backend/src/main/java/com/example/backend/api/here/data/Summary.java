package com.example.backend.api.here.data;

import com.example.backend.api.here.HereApiElement;
import com.google.gson.annotations.SerializedName;

public class Summary implements HereApiElement {

    @SerializedName("duration")
    public Integer duration;
    @SerializedName("length")
    public Integer length;
    @SerializedName("baseDuration")
    public Integer baseDuration;

    public Summary(Integer duration, Integer length, Integer baseDuration) {
        this.duration = duration;
        this.length = length;
        this.baseDuration = baseDuration;
    }

    public String toString(String tab) {
        return "\n" + tab + "Summary{" +
                "\n" + tab + "\tduration = " + duration +
                "\n" + tab + "\tlength = " + length +
                "\n" + tab + "\tbaseDuration = " + baseDuration +
                "\n" + tab + "\t}";
    }
}