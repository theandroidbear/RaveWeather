package com.ravemaster.raveweather.api.getLocations.interfaces;

import com.ravemaster.raveweather.api.getLocations.models.LocationsResponse;

import java.util.ArrayList;


public interface LocationListener {
    void onResponse(ArrayList<LocationsResponse> responses, String message);
    void onError( String message);
    void onLoading( boolean isLoading);
}
