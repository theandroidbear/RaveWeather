package com.ravemaster.raveweather.db.models;

public class ModelOne {
    private String cityName, date, icon, windSpeed, pressure, temperature, humidity, description, timeStamp;

    public ModelOne(String cityName, String date, String icon, String windSpeed, String pressure, String temperature, String humidity, String description, String timeStamp) {
        this.cityName = cityName;
        this.date = date;
        this.icon = icon;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.temperature = temperature;
        this.humidity = humidity;
        this.description = description;
        this.timeStamp = timeStamp;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDate() {
        return date;
    }

    public String getIcon() {
        return icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getPressure() {
        return pressure;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
