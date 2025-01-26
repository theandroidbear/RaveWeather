package com.ravemaster.raveweather.db.models;

public class ModelThree {
    private String group1, group2, group3, group4, group5;

    public ModelThree(String group1, String group2, String group3, String group4, String group5) {
        this.group1 = group1;
        this.group2 = group2;
        this.group3 = group3;
        this.group4 = group4;
        this.group5 = group5;
    }

    public String getGroup1() {
        return group1;
    }

    public String getGroup2() {
        return group2;
    }

    public String getGroup3() {
        return group3;
    }

    public String getGroup4() {
        return group4;
    }

    public String getGroup5() {
        return group5;
    }
}
