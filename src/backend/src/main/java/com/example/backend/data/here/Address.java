package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("label")
    String label;
    @SerializedName("countryCode")
    String countryCode;
    @SerializedName("countryName")
    String countryName;
    @SerializedName("stateCode")
    String stateCode;
    @SerializedName("state")
    String state;
    @SerializedName("countyCode")
    String countyCode;
    @SerializedName("county")
    String county;
    @SerializedName("city")
    String city;
    @SerializedName("street")
    String street;

    public Address() {

    }

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

    @Override
    public String toString() {
        return "\n\t\t\tAddress{" +
                "\n\t\t\t\tlabel = \"" + label + "\"" +
                "\n\t\t\t\tcountryCode = \"" + countryCode + "\"" +
                "\n\t\t\t\tcountryName = \"" + countryName + "\"" +
                "\n\t\t\t\tstateCode = \"" + stateCode + "\"" +
                "\n\t\t\t\tstate = \"" + state + "\"" +
                "\n\t\t\t\tcountyCode = = \"" + countyCode + "\"" +
                "\n\t\t\t\tcounty = \"" + county + "\"" +
                "\n\t\t\t\tcity = \"" + city + "\"" +
                "\n\t\t\t\tstreet = \"" + street + "\"" +
                "\n\t\t\t\t}";
    }
}