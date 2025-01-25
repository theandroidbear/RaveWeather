package com.ravemaster.raveweather.api.getLocations.models;

import com.google.gson.annotations.SerializedName;

public class Address {
    public String road;
    public String state;
    public String borough;
    @SerializedName("ISO3166-2-lvl4")
    public String iSO3166_2_lvl4;
    public String county;
    public String house_number;
    public String city;
    public String country_code;
    public String country;
    public String neighbourhood;
    public String amenity;
    public String postcode;
}
