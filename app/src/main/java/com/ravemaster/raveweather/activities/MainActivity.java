package com.ravemaster.raveweather.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.ravemaster.raveweather.GetIcon;
import com.ravemaster.raveweather.R;
import com.ravemaster.raveweather.api.RequestManager;
import com.ravemaster.raveweather.api.getWeather.interfaces.GetWeatherListener;
import com.ravemaster.raveweather.api.getWeather.models.WeatherDataResponse;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    //member objects
    RequestManager requestManager;

    //views
    LinearLayout weatherLayout;
    LottieAnimationView lottie;
    TextView txtCity, txtDate, txtTemperature, txtWeatherCode, txtWind, txtPressure, txtHumidity;
    ImageView imgWeather, imgWind, imgPressure, imgHumidity;
    SwipeRefreshLayout swipeRefreshLayout;
    ShimmerFrameLayout weatherPlaceHolder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initViews();

        //api calling
        requestManager = new RequestManager(this);
        requestManager.getWeatherData(listener,"40.7128","-74.0060");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestManager.getWeatherData(listener,"40.7128","-74.0060");
            }
        });
    }

    //api listener
    private final GetWeatherListener listener = new GetWeatherListener() {
        @Override
        public void onResponse(WeatherDataResponse response, String message) {
            swipeRefreshLayout.setRefreshing(false);
            hideAnimation();
            stopShimmer();
            showLayouts();
            showData(response);
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

        GetIcon getIcon = new GetIcon(MainActivity.this);
        imgWeather.setBackgroundResource(getIcon.getIcon(response.weather.get(0).icon));

        txtCity.setText(response.name);
        txtDate.setText(getDate(response.dt));
        double Temp = getTemperature(response.main.temp);
        String formatedTemp = String.format("%.1f", Temp);
        txtTemperature.setText(formatedTemp+"°");
        txtWeatherCode.setText(response.weather.get(0).description);
        txtWind.setText(String.valueOf(response.wind.speed)+"m/s");
        txtHumidity.setText(String.valueOf(response.main.humidity)+"%");
        txtPressure.setText(String.valueOf(response.main.pressure)+"hPa");
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
        imgWeather = findViewById(R.id.imgWeatherImage);
        imgWind = findViewById(R.id.imgWind);
        imgPressure = findViewById(R.id.imgPressure);
        imgHumidity = findViewById(R.id.imgHumidity);
        swipeRefreshLayout = findViewById(R.id.refreshWeather);
        weatherPlaceHolder = findViewById(R.id.weatherPlaceholderLayout);
        weatherLayout = findViewById(R.id.weatherLayout);
    }
}