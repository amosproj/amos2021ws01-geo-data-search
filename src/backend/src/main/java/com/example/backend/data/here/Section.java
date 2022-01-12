package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Section implements HereApiElement {

    @SerializedName("id")
    public String id;
    @SerializedName("type")
    public String type;
    @SerializedName("departure")
    public RoutePoint departure;
    @SerializedName("arrival")
    public RoutePoint arrival;
    @SerializedName("summary")
    public Summary summary;
    @SerializedName("transport")
    public Transport transport;
    @SerializedName("turnByTurnActions")
    public List<Action> turnByTurnActions;
    @SerializedName("polyline")
    public String polyline;

    public Section(String id, String type, RoutePoint departure, RoutePoint arrival, Summary summary, Transport transport, List<Action> turnByTurnActions, String polyline) {
        this.id = id;
        this.type = type;
        this.departure = departure;
        this.arrival = arrival;
        this.summary = summary;
        this.transport = transport;
        this.turnByTurnActions = turnByTurnActions;
        this.polyline = polyline;
    }

    public String toString(String tab) {
        return "\n" + tab + "Section{" +
                "\n" + tab + "\tid = \"" + id + "\"" +
                "\n" + tab + "\ttype = \"" + type + "\"" +
                "\n" + tab + "\tdeparture = " + print(departure, tab) +
                "\n" + tab + "\tarrival = " + print(arrival, tab) +
                "\n" + tab + "\tsummary = " + print(summary, tab) +
                "\n" + tab + "\ttransport = " + print(transport, tab) +
                "\n" + tab + "\tturnByTurnActions = " + "NO PRINT NOW" /*createListAsString(turnByTurnActions, tab + "\t\t") */ +
                "\n" + tab + "\tpolyline = \"" + "DO NOT PRINT THIS SUPER LONG STRING" + "\"" +
                "\n" + tab + "\t}";
    }

    private String print(HereApiElement element, String tab) {
        if (element != null) {
            return element.toString(tab + "\t\t");
        } else {
            return "null";
        }
    }

    private String createListAsString(List<Action> sections, String tab) {
        if (sections != null && !sections.isEmpty()) {
            StringBuilder returnString = new StringBuilder();
            for (Action section : sections) {
                returnString.append(section.toString(tab));
                returnString.append("\n");
            }
            return returnString.toString();
        } else {
            return "null";
        }
    }
}