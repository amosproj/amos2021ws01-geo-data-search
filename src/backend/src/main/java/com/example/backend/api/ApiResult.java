package com.example.backend.api;

public interface ApiResult {

    String getType();

    void setType(String type);

    int getId();

    String getLat();

    String getLon();

    String getName();

    String getPolyline();

    String getApi();
}