package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

public class Section {

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

    public Section(String id, String type, RoutePoint departure, RoutePoint arrival, Summary summary, Transport transport) {
        this.id = id;
        this.type = type;
        this.departure = departure;
        this.arrival = arrival;
        this.summary = summary;
        this.transport = transport;
    }

    public String toString(String tab) {
        return "\n" + tab + "Section{" +
                "\n" + tab + "\tid = \"" + id + "\"" +
                "\n" + tab + "\ttype = \"" + type + "\"" +
                "\n" + tab + "\tdeparture = " + departure.toString(tab + "\t\t") +
                "\n" + tab + "\tarrival = " + arrival.toString(tab + "\t\t") +
                "\n" + tab + "\tsummary = " + summary.toString(tab + "\t\t") +
                "\n" + tab + "\ttransport = " + transport.toString(tab + "\t\t") +
                "\n" + tab + "\t}";
    }
}