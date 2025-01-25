package com.ravemaster.raveweather;

import android.content.Context;
import android.widget.Toast;

public class GetIcon {

    private Context context;

    public GetIcon( Context context){
        this.context = context;
    }

    public int getIcon (String code){
        switch (code){
            case "01d":
                return R.drawable.oned;
            case "01n":
                return R.drawable.onen;
            case "02d":
                return R.drawable.twod;
            case "02n":
                return R.drawable.twon;
            case "03d":
                return R.drawable.threed;
            case "03n":
                return R.drawable.threen;
            case "04d":
                return R.drawable.fourd;
            case "04n":
                return R.drawable.fourn;
            case "09d":
                return R.drawable.nined;
            case "09n":
                return R.drawable.ninen;
            case "10d":
                return R.drawable.tend;
            case "10n":
                return R.drawable.tenn;
            case "11d":
                return R.drawable.elevend;
            case "11n":
                return R.drawable.elevenn;
            case "13d":
                return R.drawable.thirteend;
            case "13n":
                return R.drawable.thirteenn;
            case "50d":
                return R.drawable.fiftyd;
            case "50n":
                return R.drawable.fiftyn;
        }
        return 0;
    }

}
