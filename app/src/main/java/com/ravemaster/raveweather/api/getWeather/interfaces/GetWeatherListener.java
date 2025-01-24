package com.ravemaster.raveweather.api.getWeather.interfaces;

import com.ravemaster.raveweather.api.getWeather.models.WeatherDataResponse;

public interface GetWeatherListener {
    void onResponse(WeatherDataResponse response, String message);
    void onError( String message);
    void onLoading( boolean isLoading);
}
