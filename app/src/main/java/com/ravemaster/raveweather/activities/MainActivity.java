package com.ravemaster.raveweather.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.ravemaster.raveweather.GetIcon;
import com.ravemaster.raveweather.R;
import com.ravemaster.raveweather.adapters.ForecastAdapter;
import com.ravemaster.raveweather.api.RequestManager;
import com.ravemaster.raveweather.api.getLocations.interfaces.LocationListener;
import com.ravemaster.raveweather.api.getLocations.models.LocationsResponse;
import com.ravemaster.raveweather.api.getWeather.interfaces.GetWeatherListener;
import com.ravemaster.raveweather.api.getWeather.models.WeatherDataResponse;
import com.ravemaster.raveweather.api.getforecast.interfaces.ForecastListener;
import com.ravemaster.raveweather.api.getforecast.models.ForecastResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //member objects
    RequestManager requestManager;
    ForecastAdapter forecastAdapter;
    List<com.ravemaster.raveweather.api.getforecast.models.List> group1, group2, group3, group4, group5;
    ForecastResponse response1;

    String cityName = "";
    String latitude = "";
    String longitude = "";
    String name = "";
    int selectedTabIndex = 0;

    Dialog permissionsDialog,searchDialog;
    Button request;

    //views
    LinearLayout weatherLayout, searchLayout;
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

    String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initViews();
        setTabTexts();
        showSearchDialog();

        if (hasPermissions()){
            Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show();
        }else{
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                    || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)){
                showRationaleDialog();
            }else {
                requestPermissionLauncher.launch(permissions);
            }
        }

        response1 = new ForecastResponse();

        group1 = new ArrayList<>();
        group2 = new ArrayList<>();
        group3 = new ArrayList<>();
        group4 = new ArrayList<>();
        group5 = new ArrayList<>();

        //api calling
        requestManager = new RequestManager(this);
        requestManager.getWeatherData(listener,"-1.2921","36.8219");
        requestManager.getForecastData(listener2,"-1.2921","36.8219",getCount());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                group1.clear();
                group2.clear();
                group3.clear();
                group4.clear();
                group5.clear();
                requestManager.getWeatherData(listener,"-1.2921","36.8219");
                requestManager.getForecastData(listener2,"-1.2921","36.8219",getCount());
            }
        });
        displayWeatherPerTab();

        tabLayout.getTabAt(0).select();

        btnSearch.setOnClickListener(v ->{
            searchDialog.show();
        });

        search.setOnClickListener(view ->{
            cityName = editText.getText().toString();
            requestManager.getLocation(locationListener, cityName);
        });


    }

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

    private void showRationaleDialog() {
        permissionsDialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_request_permissions,null);
        request = view.findViewById(R.id.btnRequestPermissions);
        permissionsDialog.setContentView(view);
        permissionsDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        permissionsDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        permissionsDialog.setCancelable(false);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionLauncher.launch(permissions);
                permissionsDialog.dismiss();
            }
        });
        permissionsDialog.show();
    }

    private ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> o) {

        }
    });

    private boolean hasPermissions(){
        return
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

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
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
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
            tabLayout.getTabAt(0).select();
        }

        @Override
        public void onError(String message) {
            swipeRefreshLayout.setRefreshing(false);
            stopShimmer();
            hideLayouts();
            showAnimation();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoading(boolean isLoading) {
            if (isLoading){
                hideAnimation();
                hideLayouts();
                startShimmer();
            } else {
                swipeRefreshLayout.setRefreshing(false);
                stopShimmer();
                hideAnimation();
            }
        }
    };

    //get forecast data listener
    private final ForecastListener listener2 = new ForecastListener() {
        @Override
        public void onResponse(ForecastResponse response, String message) {
            showForecastMain(response);
            response1 = response;
            tabLayout.getTabAt(0).select();
        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoading(boolean isLoading) {
            if (isLoading){
                Toast.makeText(MainActivity.this, "Fetching", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Finished", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //display animations when internet available
    private void showAnimation(){
        lottie.setVisibility(View.VISIBLE);
        lottie.animate();
    }

    //hide animations when internet unavailable
    private void hideAnimation(){
        lottie.setVisibility(View.GONE);
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

    //data displayed via views here
    private void showData(WeatherDataResponse response) {

        tabLayout.getTabAt(0).select();

        GetIcon getIcon = new GetIcon(MainActivity.this);
        imgWeather.setBackgroundResource(getIcon.getIcon(response.weather.get(0).icon));

        txtCity.setText(response.name);
        txtCity.setSelected(true);
        txtDate.setText(getDate(response.dt));
        double Temp = getTemperature(response.main.temp);
        double maxTemp = getTemperature(response.main.temp_max);
        double minTemp = getTemperature(response.main.temp_min);
        String formatedTemp = String.format("%.1f", Temp);
        String formatedTemp2 = String.format("%.1f", maxTemp);
        String formatedTemp3 = String.format("%.1f", minTemp);
        txtTemperature.setText(formatedTemp+"°C");
        txtMax.setText(formatedTemp2+"°C (max)");
        txtMin.setText(formatedTemp3+"°C (min)");
        txtWeatherCode.setText(response.weather.get(0).description);
        txtWind.setText(String.valueOf(response.wind.speed)+" m/s");
        txtSpeed.setText(String.valueOf(response.wind.speed)+" m/s");
        txtGust.setText(String.valueOf(response.wind.speed)+" m/s");
        txtDirection.setText(String.valueOf(response.wind.speed)+"°");
        txtHumidity.setText(String.valueOf(response.main.humidity)+"%");
        txtPressure.setText(String.valueOf(response.main.pressure)+" hPa");
        txtLand.setText(String.valueOf(response.main.grnd_level)+" hPa (ground)");
        txtSea.setText(String.valueOf(response.main.sea_level)+" hPa (sea)");
        txtVisibility.setText(String.valueOf(response.visibility)+" m");
        txtClouds.setText(String.valueOf(response.clouds.all)+"%");

    }

    private void showForecastMain(ForecastResponse response) {
        tabLayout.getTabAt(0).select();
        groupTimeStamps(response.list);
        forecastAdapter = new ForecastAdapter(MainActivity.this,group1,response);
        forecastRecycler.setAdapter(forecastAdapter);
        forecastRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
        forecastRecycler.setHasFixedSize(true);
    }

    private void showForecast(List<com.ravemaster.raveweather.api.getforecast.models.List> list){
        forecastAdapter = new ForecastAdapter(MainActivity.this,list);
        forecastAdapter.setResponse(response1);
        forecastRecycler.setAdapter(forecastAdapter);
        forecastRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
        forecastRecycler.setHasFixedSize(true);
    }

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

    //convert temperature
    private double getTemperature(double temperature){
        return temperature - 273.15;
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

    private String getCount(){
        long currentTimestampMillis = System.currentTimeMillis();

        // Convert to seconds if needed
        long currentTimestamp = currentTimestampMillis / 1000; // Current timestamp
        ZoneId zoneId = ZoneId.of("Africa/Addis_Ababa"); // Specify the time zone (UTC)

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

    }
    
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
    }
}