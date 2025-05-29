package com.ravemaster.raveweather.activities;

import java.util.Map;

public interface CoordinateInterface {
    void onSuccess(Map<String, Double> coordinates);
    void onFailure(String message);
}
