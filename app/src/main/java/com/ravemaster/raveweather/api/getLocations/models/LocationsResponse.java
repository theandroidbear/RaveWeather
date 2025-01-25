package com.ravemaster.raveweather.api.getLocations.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LocationsResponse {
    public double importance;
    public String licence;
    @SerializedName("class")
    public String myclass;
    public Address address;
    public long osm_id;
    public String display_name;
    public String osm_type;
    public String lon;
    public int place_id;
    public ArrayList<String> boundingbox;
    public String lat;
    public String type;
}
