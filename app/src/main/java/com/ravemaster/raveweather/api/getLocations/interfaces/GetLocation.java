package com.ravemaster.raveweather.api.getLocations.interfaces;

import com.ravemaster.raveweather.api.getLocations.models.LocationsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GetLocation {
    @GET("forward")
    @Headers({
            "x-rapidapi-key: 7e3d2f10bdmsh70e6fefa71835adp16c240jsnb41f1b9c1073",
            "x-rapidapi-host: forward-reverse-geocoding.p.rapidapi.com"
    })
    Call<ArrayList<LocationsResponse>> location(
            @Query("city") String city
    );
}
