package com.ravemaster.raveweather.api;

import android.content.Context;

import com.ravemaster.raveweather.api.getLocations.interfaces.GetLocation;
import com.ravemaster.raveweather.api.getLocations.interfaces.LocationListener;
import com.ravemaster.raveweather.api.getLocations.models.LocationsResponse;
import com.ravemaster.raveweather.api.getWeather.interfaces.GetWeatherData;
import com.ravemaster.raveweather.api.getWeather.interfaces.GetWeatherListener;
import com.ravemaster.raveweather.api.getWeather.models.WeatherDataResponse;
import com.ravemaster.raveweather.api.getforecast.interfaces.ForecastListener;
import com.ravemaster.raveweather.api.getforecast.interfaces.GetForecast;
import com.ravemaster.raveweather.api.getforecast.models.ForecastResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager {
    Context context;

    public RequestManager(Context context) {
        this.context = context;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit retrofit2 = new Retrofit.Builder()
            .baseUrl("https://forward-reverse-geocoding.p.rapidapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getWeatherData(GetWeatherListener listener, String lat, String lon){
        listener.onLoading(true);
        GetWeatherData getWeatherData = retrofit.create(GetWeatherData.class);
        Call<WeatherDataResponse> call = getWeatherData.getWeatherData(lat,lon,"cfe577b09f43deea2722462eea76e473");
        call.enqueue(new Callback<WeatherDataResponse>() {
            @Override
            public void onResponse(Call<WeatherDataResponse> call, Response<WeatherDataResponse> response) {
                listener.onLoading(false);
                if (!response.isSuccessful()){
                    listener.onError(String.valueOf(response.code())+" from onResponse");
                    return;
                }
                listener.onResponse(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<WeatherDataResponse> call, Throwable throwable) {
                listener.onLoading(false);
                listener.onError(throwable.getMessage()+ " from onFailure");
            }
        });
    }

    public void getForecastData(ForecastListener listener, String lat, String lon,String count){
        listener.onLoading(true);
        GetForecast getForecast = retrofit.create(GetForecast.class);
        Call<ForecastResponse> call = getForecast.getForecastData(lat,lon,"cfe577b09f43deea2722462eea76e473",count);
        call.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                listener.onLoading(false);
                if (!response.isSuccessful()){
                    listener.onError(String.valueOf(response.code())+" from onResponse");
                    return;
                }
                listener.onResponse(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable throwable) {
                listener.onLoading(false);
                listener.onError(throwable.getMessage()+ " from onFailure");
            }
        });
    }

    public void getLocation(LocationListener listener, String city){
        listener.onLoading(true);
        GetLocation getLocation = retrofit2.create(GetLocation.class);
        Call<ArrayList<LocationsResponse>> call = getLocation.location(city);
        call.enqueue(new Callback<ArrayList<LocationsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LocationsResponse>> call, Response<ArrayList<LocationsResponse>> response) {
                listener.onLoading(false);
                if (!response.isSuccessful()){
                    listener.onError(String.valueOf(response.code())+" from onResponse");
                    return;
                }
                listener.onResponse(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<ArrayList<LocationsResponse>> call, Throwable throwable) {
                listener.onLoading(false);
                listener.onError(throwable.getMessage()+ " from onFailure");
            }
        });
    }
}
