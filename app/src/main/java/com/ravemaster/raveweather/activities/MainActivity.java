package com.ravemaster.raveweather.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ravemaster.raveweather.GetIcon;
import com.ravemaster.raveweather.R;
import com.ravemaster.raveweather.adapters.DayClickListener;
import com.ravemaster.raveweather.adapters.ForecastAdapter;
import com.ravemaster.raveweather.adapters.LinearLayoutManagerWrapper;
import com.ravemaster.raveweather.api.RequestManager;
import com.ravemaster.raveweather.api.getLocations.interfaces.LocationListener;
import com.ravemaster.raveweather.api.getLocations.models.LocationsResponse;
import com.ravemaster.raveweather.api.getWeather.interfaces.GetWeatherListener;
import com.ravemaster.raveweather.api.getWeather.models.WeatherDataResponse;
import com.ravemaster.raveweather.api.getforecast.interfaces.ForecastListener;
import com.ravemaster.raveweather.api.getforecast.models.ForecastResponse;
import com.ravemaster.raveweather.db.DBHelper;
import com.ravemaster.raveweather.db.models.ModelOne;
import com.ravemaster.raveweather.db.models.ModelTwo;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //declaration of various member objects used in the entire file
    RequestManager requestManager;
    ForecastAdapter forecastAdapter;
    List<com.ravemaster.raveweather.api.getforecast.models.List> group1, group2, group3, group4, group5;
    ForecastResponse response1;
    DBHelper helper;
    String cityName = "";
    String latitude = "";
    String longitude = "";
    String name = "";
    String nameCity = "";
    int selectedTabIndex = 0;
    boolean checkOffline = false;

    // declaration of all view elements used throughout the file
    Dialog permissionsDialog,searchDialog;
    Button request;
    LinearLayout weatherLayout, searchLayout;
    RelativeLayout layout;
    LottieAnimationView lottie;
    TextView txtCity, txtDate, txtTemperature, txtWeatherCode, txtWind, txtPressure, txtHumidity, txtMax, txtMin, txtSea, txtLand, txtSpeed, txtGust, txtDirection, txtVisibility, txtClouds, city, lat, lon;
    ImageView imgWeather, imgWind, imgPressure, imgHumidity;
    SwipeRefreshLayout swipeRefreshLayout;
    ShimmerFrameLayout weatherPlaceHolder;
    FloatingActionButton btnSearch;
    Button search;
    TextInputLayout enter;
    EditText editText;
    MaterialAlertDialogBuilder builder;
    CircularProgressIndicator progressIndicator;
    TabLayout tabLayout;
    RecyclerView forecastRecycler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);

        //views initialized in this method
        initViews();

        //texts displayed in the tabs are set using this method
        setTabTexts();

        //method to create the searching cities dialog
        showSearchDialog();

        //initialization of the member objects
        response1 = new ForecastResponse();
        helper = new DBHelper(MainActivity.this);
        forecastAdapter = new ForecastAdapter(MainActivity.this,dayClickListener);
        group1 = new ArrayList<>();
        group2 = new ArrayList<>();
        group3 = new ArrayList<>();
        group4 = new ArrayList<>();
        group5 = new ArrayList<>();

        //calling weather and forecast endpoints
        requestManager = new RequestManager(this);
        requestManager.getWeatherData(listener,"-1.2921","36.8219");
        requestManager.getForecastData(listener2,"-1.2921","36.8219",getCount());

        //updating data and ui after swipe down to refresh event
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hideAnimation();
                requestManager.getWeatherData(listener,"-1.2921","36.8219");
                requestManager.getForecastData(listener2,"-1.2921","36.8219",getCount());
            }
        });

        //handle selection of tabs
        displayWeatherPerTab();

        //resetting selected tab to the first
        tabLayout.getTabAt(0).select();

        //button for showing the search dialog
        btnSearch.setOnClickListener(v ->{
            searchDialog.show();
        });

        //button for calling geolocation endpoint
        search.setOnClickListener(view ->{
            cityName = editText.getText().toString();
            requestManager.getLocation(locationListener, cityName);
        });


    }

    //show dialog creating method
    private void showSearchDialog(){
        builder = new MaterialAlertDialogBuilder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.search,null);
        city = view.findViewById(R.id.txtCitySearch);
        lat = view.findViewById(R.id.txtLat);
        lon = view.findViewById(R.id.txtLon);
        progressIndicator = view.findViewById(R.id.progress_indicator);
        searchLayout = view.findViewById(R.id.searchLayout);
        TextInputLayout enter = view.findViewById(R.id.enterCity);
        editText = enter.getEditText();
        search = view.findViewById(R.id.btnSearch);
        builder.setView(view)
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchDialog.dismiss();
                    }
                })
                .setPositiveButton("View", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        group1.clear();
                        group2.clear();
                        group3.clear();
                        group4.clear();
                        group5.clear();
                        requestManager.getWeatherData(listener,latitude,longitude);
                        requestManager.getForecastData(listener2,latitude,longitude,getCount());
                        searchDialog.dismiss();
                    }
                });
        searchDialog = builder.create();
    }

    //get location listener
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onResponse(ArrayList<LocationsResponse> responses, String message) {
            searchLayout.setVisibility(View.VISIBLE);
            showCityData(responses);
        }

        @Override
        public void onError(String message) {
            checkOffline = true;
            if (message.contains("Unable to")){
                showSnackBar("Please check your internet connection!");
            } else {
                showSnackBar("An unexpected error occurred, please try again later.");
            }
        }

        @Override
        public void onLoading(boolean isLoading) {
            if (isLoading){
                searchLayout.setVisibility(View.INVISIBLE);
                progressIndicator.setVisibility(View.VISIBLE);
            } else {
                progressIndicator.setVisibility(View.INVISIBLE);
            }
        }
    };

    //show data in the search dialog
    private void showCityData(ArrayList<LocationsResponse> responses) {
        name = responses.get(0).display_name;
        latitude = responses.get(0).lat;
        longitude = responses.get(0).lon;
        city.setText("City: "+name);
        city.setSelected(true);
        lat.setText("Latitude: "+latitude);
        lon.setText("Longitude: "+longitude);
    }

    //get weather data listener
    private final GetWeatherListener listener = new GetWeatherListener() {
        @Override
        public void onResponse(WeatherDataResponse response, String message) {
            swipeRefreshLayout.setRefreshing(false);
            hideAnimation();
            stopShimmer();
            showLayouts();
            showData(response);
            insertData(response);
            deleteName();
            insertName(response.name);
            tabLayout.getTabAt(0).select();
        }

        @Override
        public void onError(String message) {
            swipeRefreshLayout.setRefreshing(false);
            stopShimmer();
            checkOffline = true;
            if (message.contains("Unable to")){
                showSnackBar("Please check your internet connection!");
                showLayouts();
                getFromDb();
            } else if(message.contains("Failed to connect")){
                showSnackBar("Please check your internet connection!");
                showLayouts();
                getFromDb();
            } else  {
                hideLayouts();
                showAnimation();
                showSnackBar("Swipe down to refresh previously loaded data.");
            }
        }

        @Override
        public void onLoading(boolean isLoading) {
            if (isLoading){
                hideLayouts();
                startShimmer();
            } else {
                swipeRefreshLayout.setRefreshing(false);
                stopShimmer();
            }
        }
    };

    //data displayed via views here when online
    private void showData(WeatherDataResponse response) {

        tabLayout.getTabAt(0).select();

        GetIcon getIcon = new GetIcon(MainActivity.this);
        imgWeather.setBackgroundResource(getIcon.getIcon(response.weather.get(0).icon));

        txtCity.setText(response.name);
        txtCity.setSelected(true);
        txtDate.setText(getDate(response.dt));
        txtTemperature.setText(getTemperature(response.main.temp)+"°C");
        txtMax.setText(getTemperature(response.main.temp_max)+"°C (max)");
        txtMin.setText(getTemperature(response.main.temp_min)+"°C (min)");
        txtWeatherCode.setText(response.weather.get(0).description);
        txtWind.setText(String.valueOf(response.wind.speed)+" m/s");
        txtSpeed.setText(String.valueOf(response.wind.speed)+" m/s");
        txtGust.setText(String.valueOf(response.wind.speed)+" m/s");
        txtDirection.setText(String.valueOf(response.wind.deg)+"°");
        txtHumidity.setText(String.valueOf(response.main.humidity)+"%");
        txtPressure.setText(String.valueOf(response.main.pressure)+" hPa");
        txtLand.setText(String.valueOf(response.main.grnd_level)+" hPa (ground)");
        txtSea.setText(String.valueOf(response.main.sea_level)+" hPa (sea)");
        txtVisibility.setText(String.valueOf(response.visibility)+" m");
        txtClouds.setText(String.valueOf(response.clouds.all)+"%");

    }

    private final DayClickListener dayClickListener = new DayClickListener() {
        @Override
        public void onDayClicked(com.ravemaster.raveweather.api.getforecast.models.List list) {
            showDataWhenClick(list);
        }
    };

    //data displayed via views here when online
    private void showDataWhenClick(com.ravemaster.raveweather.api.getforecast.models.List list) {

        GetIcon getIcon = new GetIcon(MainActivity.this);
        imgWeather.setBackgroundResource(getIcon.getIcon(list.weather.get(0).icon));

        txtCity.setText(getName());
        txtCity.setSelected(true);
        txtDate.setText(getDate(list.dt));
        txtTemperature.setText(getTemperature(list.main.temp)+"°C");
        txtMax.setText(getTemperature(list.main.temp_max)+"°C (max)");
        txtMin.setText(getTemperature(list.main.temp_min)+"°C (min)");
        txtWeatherCode.setText(list.weather.get(0).description);
        txtWind.setText(String.valueOf(list.wind.speed)+" m/s");
        txtSpeed.setText(String.valueOf(list.wind.speed)+" m/s");
        txtGust.setText(String.valueOf(list.wind.speed)+" m/s");
        txtDirection.setText(String.valueOf(list.wind.deg)+"°");
        txtHumidity.setText(String.valueOf(list.main.humidity)+"%");
        txtPressure.setText(String.valueOf(list.main.pressure)+" hPa");
        txtLand.setText(String.valueOf(list.main.grnd_level)+" hPa (ground)");
        txtSea.setText(String.valueOf(list.main.sea_level)+" hPa (sea)");
        txtVisibility.setText(String.valueOf(list.visibility)+" m");
        txtClouds.setText(String.valueOf(list.clouds.all)+"%");

    }

    //getting weather data cached in db when offline
    private void getFromDb() {
        Cursor cursor = helper.getModelOne();
        Cursor cursor2 = helper.getModelTwo();
        ModelOne modelOne = null;
        ModelTwo modelTwo = null;
        if (cursor.getCount() == 0){
        } else {
            while (cursor.moveToNext()){
                String timeStamp = cursor.getString(0);
                String cityName = cursor.getString(1);
                String date = cursor.getString(2);
                String icon = cursor.getString(3);
                String windSpeed = cursor.getString(4);
                String temperature = cursor.getString(5);
                String pressure = cursor.getString(6);
                String humidity = cursor.getString(7);
                String description = cursor.getString(8);
                modelOne = new ModelOne(cityName, date, icon, windSpeed, pressure, temperature, humidity, description, timeStamp);
            }
        }
        if (cursor2.getCount() == 0){
        } else {
            while (cursor2.moveToNext()){
                String timeStamp = cursor2.getString(0);
                String maxTemp = cursor2.getString(1);
                String minTemp = cursor2.getString(2);
                String ground = cursor2.getString(3);
                String sea = cursor2.getString(4);
                String speed = cursor2.getString(5);
                String gust = cursor2.getString(6);
                String direction = cursor2.getString(7);
                String visibility = cursor2.getString(8);
                String clouds = cursor2.getString(9);
                nameCity = cursor2.getString(10);
                modelTwo = new ModelTwo(maxTemp, minTemp, ground, sea, speed, gust, direction, visibility, clouds, timeStamp,nameCity);
            }
        }
        showFromDb(modelOne,modelTwo);
    }

    //showing data cached in db when offline
    private void showFromDb(ModelOne modelOne, ModelTwo modelTwo) {
        if (modelOne == null || modelTwo == null){
            hideLayouts();
            showAnimation();
        } else{
            hideAnimation();
            tabLayout.getTabAt(0).select();

            GetIcon getIcon = new GetIcon(MainActivity.this);
            imgWeather.setBackgroundResource(getIcon.getIcon(modelOne.getIcon()));

            txtCity.setText(modelOne.getCityName());
            txtCity.setSelected(true);
            txtDate.setText(modelOne.getTimeStamp()+" (last updated)");
            txtTemperature.setText(modelOne.getTemperature()+"°C");
            txtMax.setText(modelTwo.getMaxTemp()+"°C (max)");
            txtMin.setText(modelTwo.getMinTemp()+"°C (min)");
            txtWeatherCode.setText(modelOne.getDescription());
            txtWind.setText(modelOne.getWindSpeed()+" m/s");
            txtSpeed.setText(modelTwo.getSpeed()+" m/s");
            txtGust.setText(modelTwo.getGust()+" m/s");
            txtDirection.setText(modelTwo.getDirection()+"°");
            txtHumidity.setText(modelOne.getHumidity()+"%");
            txtPressure.setText(modelOne.getPressure()+" hPa");
            txtLand.setText(modelTwo.getGround()+" hPa (ground)");
            txtSea.setText(modelTwo.getSea()+" hPa (sea)");
            txtVisibility.setText(modelTwo.getVisibility()+" m");
            txtClouds.setText(modelTwo.getClouds()+"%");
        }

    }

    //cache data into db for offline purposes
    private void insertData(WeatherDataResponse response) {

        boolean clearModelOne = helper.deleteModelOne();
        boolean clearModelTwo = helper.deleteModelTwo();

        boolean checkInsertModelOne = helper.insertModelOne(
                getTimeStamps(),response.name,getDate(response.dt)
                ,response.weather.get(0).icon,getTemperature(response.main.temp),String.valueOf(response.wind.speed),String.valueOf(response.main.pressure),String.valueOf(response.main.humidity),
                response.weather.get(0).description
        );
        boolean checkInsertModelTwo = helper.insertModelTwo(
                getTimeStamps(),getTemperature(response.main.temp_max),getTemperature(response.main.temp_min),
                String.valueOf(response.main.grnd_level),String.valueOf(response.main.sea_level),String.valueOf(response.wind.speed),
                String.valueOf(response.wind.speed),String.valueOf(response.wind.deg),String.valueOf(response.visibility),String.valueOf(response.clouds.all),
                response.name
        );

    }

    //get time when data was cached
    private String getTimeStamps(){
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format the date and time (optional)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, hh:mm a");

        // Display the current date and time
        return currentDateTime.format(formatter);
    }

    //get forecast data listener
    private final ForecastListener listener2 = new ForecastListener() {
        @Override
        public void onResponse(ForecastResponse response, String message) {
            group1.clear();
            group2.clear();
            group3.clear();
            group4.clear();
            group5.clear();
            swipeRefreshLayout.setRefreshing(false);
            showForecastMain(response);
            response1 = response;
            groupTimeStamps(response.list);
            tabLayout.getTabAt(0).select();
        }

        @Override
        public void onError(String message) {
            group1.clear();
            group2.clear();
            group3.clear();
            group4.clear();
            group5.clear();
            swipeRefreshLayout.setRefreshing(false);
            if (message.contains("Unable to")){
                getArraysFromDb();
                showSnackBar("Please check your internet connection!");
            } else if(message.contains("failed to")) {
                getArraysFromDb();
                showSnackBar("Please check your internet connection!");
            } else {
                hideLayouts();
                showAnimation();
                showSnackBar("Swipe down to refresh last previously data.");
            }

        }
        @Override
        public void onLoading(boolean isLoading) {
            if (isLoading){
                hideAnimation();
//                Toast.makeText(MainActivity.this, "Fetching", Toast.LENGTH_SHORT).show();
            } else {
                swipeRefreshLayout.setRefreshing(false);
//                Toast.makeText(MainActivity.this, "Finished", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //show data for the first day forecast
    private void showForecastMain(ForecastResponse response) {
        tabLayout.getTabAt(0).select();
        forecastAdapter.setForecastList(group1);
        forecastAdapter.setName(response.city.name);
        forecastRecycler.setAdapter(forecastAdapter);
        forecastAdapter.notifyDataSetChanged();
        forecastRecycler.setLayoutManager(new LinearLayoutManagerWrapper(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
        forecastRecycler.setHasFixedSize(true);
    }

    //show data for all days forecast
    private void showForecast(List<com.ravemaster.raveweather.api.getforecast.models.List> list){
        if (list.isEmpty()){
            showAnimation();
            hideLayouts();
        } else {
            forecastAdapter.setName(getName());
            forecastAdapter.setForecastList(list);
            forecastRecycler.setAdapter(forecastAdapter);
            forecastAdapter.notifyDataSetChanged();
            forecastRecycler.setLayoutManager(new LinearLayoutManagerWrapper(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
            forecastRecycler.setHasFixedSize(true);
        }
    }

    //handle selection of tabs
    private void displayWeatherPerTab(){

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabIndex = tab.getPosition();
                switch (tab.getPosition()){
                    case 0:
                        showForecast(group1);
                        break;
                    case 1:
                        showForecast(group2);
                        break;
                    case 2:
                        showForecast(group3);
                        break;
                    case 3:
                        showForecast(group4);
                        break;
                    case 4:
                        showForecast(group5);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    //get cached forecast data from database when offline
    private void getArraysFromDb() {
        Cursor cursor = helper.getModelThree();
        String s1 = null;
        String s2 = null;
        String s3 = null;
        String s4 = null;
        String s5 = null;
        Type type = new TypeToken<List<com.ravemaster.raveweather.api.getforecast.models.List>>() {}.getType();
        Gson gson = new Gson();
         if (cursor.getCount() == 0){
         } else {
             while(cursor.moveToNext()){
                 s1 = cursor.getString(0);
                 s2 = cursor.getString(1);
                 s3 = cursor.getString(2);
                 s4 = cursor.getString(3);
                 s5 = cursor.getString(4);
             }
             group1 = gson.fromJson(s1,type);
             group2 = gson.fromJson(s2,type);
             group3 = gson.fromJson(s3,type);
             group4 = gson.fromJson(s4,type);
             group5 = gson.fromJson(s5,type);
         }
         showArraysFromDb(group1,group2,group3,group4,group5);
    }

    //show cached forecast data from database when offline
    private void showArraysFromDb(List<com.ravemaster.raveweather.api.getforecast.models.List> group1, List<com.ravemaster.raveweather.api.getforecast.models.List> group2, List<com.ravemaster.raveweather.api.getforecast.models.List> group3, List<com.ravemaster.raveweather.api.getforecast.models.List> group4, List<com.ravemaster.raveweather.api.getforecast.models.List> group5){
        if (group1.isEmpty()){
            hideLayouts();
            showAnimation();
        } else {
            forecastAdapter.setName(getName());
            forecastAdapter.setForecastList(group1);
            forecastRecycler.setAdapter(forecastAdapter);
            forecastAdapter.notifyDataSetChanged();
            forecastRecycler.setLayoutManager(new LinearLayoutManagerWrapper(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
            forecastRecycler.setHasFixedSize(true);
        }
    }

    //group forecast data received from endpoint into five arrays for the five days
    private void groupTimeStamps(List<com.ravemaster.raveweather.api.getforecast.models.List> list){
        //get current time in milliseconds
        long currentTimestampMillis = System.currentTimeMillis();

        // Convert to seconds if needed
        long currentTimestamp = currentTimestampMillis / 1000; // Current timestamp
        ZoneId zoneId = ZoneId.of("Africa/Addis_Ababa"); // Specify the time zone (UTC)

        // Convert the current timestamp to LocalDate
        Instant instant = Instant.ofEpochSecond(currentTimestamp);
        ZonedDateTime currentDateTime = instant.atZone(zoneId);
        LocalDate currentDate = currentDateTime.toLocalDate();

        ZonedDateTime targetTimeStamp0 = currentDate.atStartOfDay(zoneId).plusHours(0);
        ZonedDateTime targetTimeStamp1 = currentDate.atStartOfDay(zoneId).plusHours(24);
        ZonedDateTime targetTimeStamp2 = currentDate.atStartOfDay(zoneId).plusHours(48);
        ZonedDateTime targetTimeStamp3 = currentDate.atStartOfDay(zoneId).plusHours(72);
        ZonedDateTime targetTimeStamp4 = currentDate.atStartOfDay(zoneId).plusHours(96);
        ZonedDateTime targetTimeStamp5 = currentDate.atStartOfDay(zoneId).plusHours(120);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).dt > targetTimeStamp0.toEpochSecond() && list.get(i).dt <= targetTimeStamp1.toEpochSecond()){
                group1.add(list.get(i));
            } else if (list.get(i).dt > targetTimeStamp1.toEpochSecond() && list.get(i).dt <= targetTimeStamp2.toEpochSecond()){
                group2.add(list.get(i));
            } else if (list.get(i).dt > targetTimeStamp2.toEpochSecond() && list.get(i).dt <= targetTimeStamp3.toEpochSecond()){
                group3.add(list.get(i));
            } else if (list.get(i).dt > targetTimeStamp3.toEpochSecond() && list.get(i).dt <= targetTimeStamp4.toEpochSecond()){
                group4.add(list.get(i));
            } else if (list.get(i).dt > targetTimeStamp4.toEpochSecond() && list.get(i).dt <= targetTimeStamp5.toEpochSecond()){
                group5.add(list.get(i));
            }
        }

        addToDb(group1,group2,group3,group4,group5);

    }

    //add forecast data into db for offline storage
    private void addToDb(List<com.ravemaster.raveweather.api.getforecast.models.List> group1, List<com.ravemaster.raveweather.api.getforecast.models.List> group2, List<com.ravemaster.raveweather.api.getforecast.models.List> group3, List<com.ravemaster.raveweather.api.getforecast.models.List> group4, List<com.ravemaster.raveweather.api.getforecast.models.List> group5) {
        Gson gson = new Gson();
        String s1 = gson.toJson(group1);
        String s2 = gson.toJson(group2);
        String s3 = gson.toJson(group3);
        String s4 = gson.toJson(group4);
        String s5 = gson.toJson(group5);
        boolean delete = helper.deleteModelThree();
        boolean checkInsertModelThree = helper.insertModelThree(
                s1,s2,s3,s4,s5
        );
    }

    //display animations when internet available
    private void showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(
                layout,message, Snackbar.LENGTH_LONG
        );
        snackbar.show();
    }

    //hide animations when internet unavailable
    private void hideAnimation(){
        lottie.setVisibility(View.GONE);
    }

    //show animations when offline
    private void showAnimation(){
        lottie.setVisibility(View.VISIBLE);
        lottie.animate();
    }

    //show shimmer effect when loading
    private void startShimmer(){
        weatherPlaceHolder.setVisibility(View.VISIBLE);
        weatherPlaceHolder.startShimmer();
    }

    //hide shimmer effect when finished loading
    private void stopShimmer(){
        weatherPlaceHolder.stopShimmer();
        weatherPlaceHolder.setVisibility(View.GONE);
    }

    //show content when loading finished
    private void showLayouts(){
        weatherLayout.setVisibility(View.VISIBLE);
    }

    //hide content when offline or loading
    private void hideLayouts(){
        weatherLayout.setVisibility(View.GONE);
    }

    //convert temperature
    private String getTemperature(double temperature){
        double a = temperature -273.15;
        return String.format("%.1f",a);
    }

    //convert date to human readable format
    private String getDate(long timeStamp){
        // Convert Unix timestamp to Instant
        Instant instant = Instant.ofEpochSecond(timeStamp);

        // Define a formatter for the day and month
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d").withZone(ZoneId.systemDefault());
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM").withZone(ZoneId.systemDefault());
        DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE").withZone(ZoneId.systemDefault());

        // Format the Instant to a human-readable string
        String day = dayFormatter.format(instant);
        String month = monthFormatter.format(instant);
        String dayOfWeek = dayOfWeekFormatter.format(instant);

        // Print the result
        return String.format("%s %s, %s", day, month, dayOfWeek);
    }

    //get number of timestamps received from forecast data endpoint
    private String getCount(){
        long currentTimestampMillis = System.currentTimeMillis();

        // Convert to seconds if needed
        long currentTimestamp = currentTimestampMillis / 1000; // Current timestamp
        ZoneId zoneId = ZoneId.of("Africa/Addis_Ababa"); // Specify the time zone

        // Convert the current timestamp to LocalDate
        Instant instant = Instant.ofEpochSecond(currentTimestamp);
        ZonedDateTime currentDateTime = instant.atZone(zoneId);
        LocalDate currentDate = currentDateTime.toLocalDate();

        int[] targetHours = {3,6,9,12,15,18,21,24};
        List<Long> timestamps = new ArrayList<>();

        for (int i: targetHours){
            ZonedDateTime targetTimeStamp = currentDate.atStartOfDay(zoneId).plusHours(i);
            if (currentTimestamp<targetTimeStamp.toEpochSecond()){
                timestamps.add(targetTimeStamp.toEpochSecond());
            }
        }
        int listSize = timestamps.size()+ 32;
        return String.valueOf(listSize);
    }

    //set texts displayed on tabs
    private void setTabTexts(){
        // Get the current date
        LocalDate today = LocalDate.now();

        // Create an array to hold the days
        String[] days = new String[5];

        // Set the texts based on the current day
        days[0] = "Today"; // Current day
        days[1] = "Tomorrow"; // Next day

        // Assign the remaining days based on the current day
        days[2] = today.plusDays(2).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH); // Tomorrow
        days[3] = today.plusDays(3).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH); // Day after tomorrow
        days[4] = today.plusDays(4).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH); // Two days after tomorrow

        tabLayout.getTabAt(0).setText(days[0]);
        tabLayout.getTabAt(1).setText(days[1]);
        tabLayout.getTabAt(2).setText(days[2]);
        tabLayout.getTabAt(3).setText(days[3]);
        tabLayout.getTabAt(4).setText(days[4]);
    }

    //refresh data when app is resumed
    @Override
    protected void onResume() {
        super.onResume();
        requestManager.getWeatherData(listener,"-1.2921","36.8219");
        requestManager.getForecastData(listener2,"-1.2921","36.8219",getCount());
    }

    //refresh data when activity is restarted
    @Override
    protected void onRestart() {
        super.onRestart();
        requestManager.getWeatherData(listener,"-1.2921","36.8219");
        requestManager.getForecastData(listener2,"-1.2921","36.8219",getCount());
    }

    //insert name into database
    private void insertName(String name){
        boolean checkInsertName = helper.insertName(name);
    }

    //get name from database
    private String getName(){
        String name = null;
        Cursor cursor = helper.getName();
        if (cursor.getCount() == 0){

        } else {
            while (cursor.moveToNext()){
                name = cursor.getString(0);
            }
        }
        return name;
    }

    //delete name from database
    private void deleteName(){
        boolean delete = helper.deleteName();
    }

    //views initialized here
    private void initViews() {
        lottie = findViewById(R.id.noInternetAnimation);
        txtCity = findViewById(R.id.txtCityName);
        txtDate = findViewById(R.id.txtDate);
        txtTemperature = findViewById(R.id.txtTemperature);
        txtWeatherCode = findViewById(R.id.txtWeatherCode);
        txtWind = findViewById(R.id.txtWind);
        txtPressure = findViewById(R.id.txtPressure);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtMax = findViewById(R.id.txtMax);
        txtMin = findViewById(R.id.txtMin);
        txtLand = findViewById(R.id.txtLand);
        txtSea = findViewById(R.id.txtSea);
        txtSpeed = findViewById(R.id.txtSpeed);
        txtGust = findViewById(R.id.txtGust);
        txtDirection = findViewById(R.id.txtDirection);
        txtVisibility = findViewById(R.id.txtVisibility);
        txtClouds = findViewById(R.id.txtClouds);
        imgWeather = findViewById(R.id.imgWeatherImage);
        imgWind = findViewById(R.id.imgWind);
        imgPressure = findViewById(R.id.imgPressure);
        imgHumidity = findViewById(R.id.imgHumidity);
        swipeRefreshLayout = findViewById(R.id.refreshWeather);
        weatherPlaceHolder = findViewById(R.id.weatherPlaceholderLayout);
        weatherLayout = findViewById(R.id.weatherLayout);
        forecastRecycler = findViewById(R.id.forecastRecycler);
        tabLayout = findViewById(R.id.dayTabs);
        btnSearch = findViewById(R.id.btnMore);
        layout = findViewById(R.id.mainLayout);
    }
}