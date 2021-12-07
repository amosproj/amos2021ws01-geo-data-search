package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

public class Action implements HereApiElement {

    @SerializedName("action")
    public String action;
    @SerializedName("duration")
    public Integer duration;
    @SerializedName("length")
    public Integer length;
    @SerializedName("offset")
    public Integer offset;
    @SerializedName("nextRoad")
    public Road nextRoad;
    @SerializedName("currentRoad")
    public Road currentRoad;
    @SerializedName("severity")
    public String severity;
    @SerializedName("direction")
    public String direction;

    public Action(String action, Integer duration, Integer length, Integer offset, Road nextRoad, Road currentRoad, String severity, String direction) {
        this.action = action;
        this.duration = duration;
        this.length = length;
        this.offset = offset;
        this.nextRoad = nextRoad;
        this.currentRoad = currentRoad;
        this.severity = severity;
        this.direction = direction;
    }

    public String toString(String tab) {
        return "\n" + tab + "Action{" +
                "\n" + tab + "\taction = \"" + action + "\"" +
                "\n" + tab + "\tduration = " + duration +
                "\n" + tab + "\tlength = " + length +
                "\n" + tab + "\toffset = " + offset +
                "\n" + tab + "\tnextRoad = " + print(nextRoad, tab) +
                "\n" + tab + "\tcurrentRoad = " + print(currentRoad, tab) +
                "\n" + tab + "\tseverity = \"" + severity + "\"" +
                "\n" + tab + "\tdirection = \"" + direction + "\"" +
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