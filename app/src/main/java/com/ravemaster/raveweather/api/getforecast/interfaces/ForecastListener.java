package com.ravemaster.raveweather.api.getforecast.interfaces;

import com.ravemaster.raveweather.api.getWeather.models.WeatherDataResponse;
import com.ravemaster.raveweather.api.getforecast.models.ForecastResponse;

public interface ForecastListener {
    void onResponse(ForecastResponse response, String message);
    void onError( String message);
    void onLoading( boolean isLoading);
}
