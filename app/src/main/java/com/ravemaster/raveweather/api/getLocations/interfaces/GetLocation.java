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
            "x-rapidapi-key: 3179c2d1b2msh5a5901fddaf61c0p1596ddjsn5068b30a6a26",
            "x-rapidapi-host: forward-reverse-geocoding.p.rapidapi.com"
    })
    Call<ArrayList<LocationsResponse>> location(
            @Query("city") String city
    );
}
