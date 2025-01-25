package com.ravemaster.raveweather.api.getforecast.interfaces;

import com.ravemaster.raveweather.api.getforecast.models.ForecastResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetForecast {
    @GET("forecast")
    Call<ForecastResponse> getForecastData(
            @Query("lat") String lat ,
            @Query("lon") String lon,
            @Query("appid") String appId,
            @Query("cnt") String count
    );
}
