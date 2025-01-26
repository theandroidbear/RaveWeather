package com.ravemaster.raveweather.db.models;

public class ModelTwo {
    private String maxTemp, minTemp, ground, sea, speed, gust, direction, visibility, clouds, timeStamp, name;

    public ModelTwo(String maxTemp, String minTemp, String ground, String sea, String speed, String gust, String direction, String visibility, String clouds, String timeStamp, String name) {
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.ground = ground;
        this.sea = sea;
        this.speed = speed;
        this.gust = gust;
        this.direction = direction;
        this.visibility = visibility;
        this.clouds = clouds;
        this.timeStamp = timeStamp;
        this.name = name;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getGround() {
        return ground;
    }

    public String getSea() {
        return sea;
    }

    public String getSpeed() {
        return speed;
    }

    public String getGust() {
        return gust;
    }

    public String getDirection() {
        return direction;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getClouds() {
        return clouds;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
    public String getName() {
        return name;
    }
}
