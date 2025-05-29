package com.ravemaster.raveweather.activities;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.Map;

public class LocationManager {
    Activity context;
    FusedLocationProviderClient providerClient;

    public LocationManager(Activity context) {
        this.context = context;
        providerClient = LocationServices.getFusedLocationProviderClient(context);
    }



    @SuppressLint("MissingPermission")
    public void getLocation(CoordinateInterface coordinateInterface){
        Map<String, Double> coordinates = new HashMap<>();
        providerClient.getLastLocation()
                .addOnSuccessListener( context, location -> {
                    if (location != null){
                        coordinates.put("Latitude",location.getLatitude());
                        coordinates.put("Longitude", location.getLongitude());
                        coordinateInterface.onSuccess(coordinates);
                    } else {
                        coordinateInterface.onFailure("Unable to get location");
                    }
                })
                .addOnFailureListener( context, e -> coordinateInterface.onFailure(e.getMessage()));
    }

}
