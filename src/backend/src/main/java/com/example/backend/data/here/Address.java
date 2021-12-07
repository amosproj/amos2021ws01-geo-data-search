package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

public class Address implements HereApiElement{

    @SerializedName("label")
    public String label;
    @SerializedName("countryCode")
    public String countryCode;
    @SerializedName("countryName")
    public String countryName;
    @SerializedName("stateCode")
    public String stateCode;
    @SerializedName("state")
    public String state;
    @SerializedName("countyCode")
    public String countyCode;
    @SerializedName("county")
    public String county;
    @SerializedName("city")
    public String city;
    @SerializedName("street")
    public String street;

    public Address(String label, String countryCode, String countryName, String stateCode, String state, String countyCode, String county, String city, String street) {
        this.label = label;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.stateCode = stateCode;
        this.state = state;
        this.countyCode = countyCode;
        this.county = county;
        this.city = city;
        this.street = street;
    }

    public String toString(String tab) {
        return "\n" + tab + "Address{" +
                "\n" + tab + "\tlabel = \"" + label + "\"" +
                "\n" + tab + "\tcountryCode = \"" + countryCode + "\"" +
                "\n" + tab + "\tcountryName = " + countryName + "\"" +
                "\n" + tab + "\tstateCode = \"" + stateCode + "\"" +
                "\n" + tab + "\tstate = \"" + state + "\"" +
                "\n" + tab + "\tcountyCode = " + countyCode + "\"" +
                "\n" + tab + "\tcounty = \"" + county + "\"" +
                "\n" + tab + "\tcity = \"" + city + "\"" +
                "\n" + tab + "\tstreet = " + street + "\"" +
                "\n" + tab + "\t}";
    }
}