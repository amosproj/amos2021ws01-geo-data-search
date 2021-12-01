package com.example.backend.data.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeTag {
    @JsonProperty("addr:city")
    String city;
    @JsonProperty("addr:country")
    String country;
    @JsonProperty("addr:housenumber")
    String houseNumber;
    @JsonProperty("addr:street")
    String street;
    @JsonProperty("addr:postcode")
    String postCode;
    @JsonProperty("addr:suburb")
    String suburb;
    String amenity;
    String name;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NodeTag{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", street='" + street + '\'' +
                ", postCode='" + postCode + '\'' +
                ", suburb='" + suburb + '\'' +
                ", amenity='" + amenity + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
