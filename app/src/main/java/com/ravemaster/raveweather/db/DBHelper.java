package com.ravemaster.raveweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "DatabaseFour.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ModelOne(timestamp TEXT,cityname TEXT, date TEXT, icon TEXT, windspeed TEXT, temperature TEXT, pressure TEXT, humidity TEXT, description TEXT)");
        db.execSQL("create table ModelTwo(timestamptwo TEXT,maxtemp TEXT, mintemp TEXT, ground TEXT, sea TEXT, speed TEXT, gust TEXT, direction TEXT, visibility TEXT, clouds TEXT, name TEXT)");
        db.execSQL("create table ModelThree(groupOne TEXT, groupTwo TEXT, groupThree TEXT, groupFour TEXT, groupFive TEXT)");
        db.execSQL("create table NameTable(namename TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists ModelOne");
        db.execSQL("drop table if exists ModelTwo");
        db.execSQL("drop table if exists ModelThree");
        db.execSQL("drop table if exists NameTable");

    }

    public boolean insertModelOne(String timeStamp,String cityName, String date, String icon, String temperature, String windSpeed, String pressure, String humidity, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("timestamp", timeStamp);
        contentValues.put("cityname", cityName);
        contentValues.put("date", date);
        contentValues.put("icon", icon);
        contentValues.put("temperature", temperature);
        contentValues.put("windspeed", windSpeed);
        contentValues.put("pressure", pressure);
        contentValues.put("humidity", humidity);
        contentValues.put("description", description);

        long result = db.insert("ModelOne", null, contentValues);
        return result != 1;
    }

    public boolean insertModelTwo(String timeStamp2,String maxTemp, String minTemp, String ground, String sea, String speed, String gust, String direction, String visibility, String clouds, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("timestamptwo", timeStamp2);
        contentValues.put("maxtemp", maxTemp);
        contentValues.put("mintemp", minTemp);
        contentValues.put("ground", ground);
        contentValues.put("sea", sea);
        contentValues.put("speed", speed);
        contentValues.put("gust", gust);
        contentValues.put("direction", direction);
        contentValues.put("visibility", visibility);
        contentValues.put("clouds", clouds);
        contentValues.put("name", name);

        long result = db.insert("ModelTwo", null, contentValues);
        return result != 1;
    }

    public boolean insertModelThree(String group1, String group2, String group3, String group4, String group5){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupOne", group1);
        contentValues.put("groupTwo", group2);
        contentValues.put("groupThree", group3);
        contentValues.put("groupFour", group4);
        contentValues.put("groupFive", group5);

        long result = db.insert("ModelThree", null, contentValues);
        return result != 1;
    }

    public Cursor getModelOne(){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("Select * from ModelOne",null);
    }

    public boolean insertName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("namename", name);

        long result = db.insert("NameTable", null, contentValues);
        return result != 1;
    }

    public Cursor getName(){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("Select * from NameTable",null);
    }

    public Cursor getModelTwo(){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("Select * from ModelTwo",null);
    }

    public Cursor getModelThree(){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("Select * from ModelThree",null);
    }

    public Boolean deleteModelOne() {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("ModelOne", null, null);
        return result != -1;
    }

    public Boolean deleteModelTwo() {
        SQLiteDatabase DB = this.getWritableDatabase();
        long result = DB.delete("ModelTwo", null, null);
        return result != -1;
    }

    public Boolean deleteModelThree() {
        SQLiteDatabase DB = this.getWritableDatabase();
        long result = DB.delete("ModelThree", null, null);
        return result != -1;
    }

    public Boolean deleteName() {
        SQLiteDatabase DB = this.getWritableDatabase();
        long result = DB.delete("NameTable", null, null);
        return result != -1;
    }
}
