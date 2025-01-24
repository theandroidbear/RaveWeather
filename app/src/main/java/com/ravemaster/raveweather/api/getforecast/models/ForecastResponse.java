package com.ravemaster.raveweather.api.getforecast.models;

import java.util.ArrayList;

public class ForecastResponse {
    public String cod;
    public int message;
    public int cnt;
    public ArrayList<List> list;
    public City city;
}
