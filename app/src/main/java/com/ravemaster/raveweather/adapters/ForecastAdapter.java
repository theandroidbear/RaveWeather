package com.ravemaster.raveweather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.ravemaster.raveweather.GetIcon;
import com.ravemaster.raveweather.R;
import com.ravemaster.raveweather.api.getforecast.models.ForecastResponse;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Response;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private Context context;
    private List<com.ravemaster.raveweather.api.getforecast.models.List> forecastList;
    private ForecastResponse response;

    public ForecastAdapter(Context context, List<com.ravemaster.raveweather.api.getforecast.models.List> forecastList, ForecastResponse response) {
        this.context = context;
        this.forecastList = forecastList;
        this.response = response;
    }
    public ForecastAdapter(Context context, List<com.ravemaster.raveweather.api.getforecast.models.List> forecastList) {
        this.context = context;
        this.forecastList = forecastList;
    }

    public void setResponse(ForecastResponse response) {
        this.response = response;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForecastViewHolder(LayoutInflater.from(context).inflate(R.layout.forecast_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        GetIcon getIcon = new GetIcon(context);
        holder.imgForecast.setBackgroundResource(getIcon.getIcon(forecastList.get(position).weather.get(0).icon));
        double Temp = getTemperature(forecastList.get(position).main.temp);
        String formatedTemp = String.format("%.1f", Temp);
        holder.txtForecastTemp.setText(formatedTemp+"°C");
        holder.txtForecastDate.setText(getDate(forecastList.get(position).dt));
        holder.txtForecastDescription.setText(forecastList.get(position).weather.get(0).description);
        holder.txtCity.setText(response.city.name);

        holder.forecastCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    private double getTemperature(double temperature){
        return temperature - 273.15;
    }

    private String getDate(long timeStamp){
        // Convert Unix timestamp to Instant
        Instant instant = Instant.ofEpochSecond(timeStamp);

        // Define a formatter for the day and month
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d").withZone(ZoneId.systemDefault());
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM").withZone(ZoneId.systemDefault());
        DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE").withZone(ZoneId.systemDefault());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a").withZone(ZoneId.systemDefault());

        // Format the Instant to a human-readable string
        String timeOfDay = timeFormatter.format(instant);

        // Print the result
        return String.format("%s",timeOfDay);
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView forecastCardView;
        ImageView imgForecast;
        TextView txtForecastDate, txtForecastTemp,txtForecastDescription,txtCity;
        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            forecastCardView = itemView.findViewById(R.id.forecastCardView);
            imgForecast = itemView.findViewById(R.id.imgForecast);
            txtForecastDate = itemView.findViewById(R.id.txtForecastDate);
            txtForecastTemp = itemView.findViewById(R.id.txtForecastTemp);
            txtForecastDescription = itemView.findViewById(R.id.txtForecastDescription);
            txtCity = itemView.findViewById(R.id.txtForecastCity);
        }
    }
}
