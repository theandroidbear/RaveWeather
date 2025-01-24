package com.ravemaster.raveweather.api.getWeather.interfaces;

import com.ravemaster.raveweather.api.getWeather.models.WeatherDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetWeatherData {
    @GET("weather")
    Call<WeatherDataResponse> getWeatherData(
            @Query("lat") String lat ,
            @Query("lon") String lon,
            @Query("appid") String appId
    );
}
