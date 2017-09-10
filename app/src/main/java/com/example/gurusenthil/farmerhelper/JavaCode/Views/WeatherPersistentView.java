package com.example.gurusenthil.farmerhelper.JavaCode.Views;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gurusenthil.farmerhelper.JavaCode.Other.HindiConstants;
import com.example.gurusenthil.farmerhelper.R;

import java.util.Locale;

/**
 * Created by GuruSenthil on 12/5/16.
 */

public class WeatherPersistentView extends LinearLayout {

    private static final String TAG = "WeatherPersistentView";

    private Context context;
    private String celsius_constant = "°C";
    private TextView tempsView;
    private ImageView weatherIcon;

    private int lowTemp;
    private int highTemp;
    private String description;

    private TextToSpeech textToSpeech;

    public WeatherPersistentView(Context context){
        super(context);
        this.context = context;
        inflateView();
    }

    public WeatherPersistentView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        inflateView();
    }

    public WeatherPersistentView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.context = context;
        inflateView();
    }

    private void inflateView() {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.weather_persistent_view_layout, this, true);

        tempsView = (TextView) findViewById(R.id.weather_persistent_high_and_low);
        weatherIcon = (ImageView) findViewById(R.id.weather_persistent_status_icon);


        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doTextToSpeech();
            }
        });
    }

    public void setData(double high, double low, String iconName, String description) {
        lowTemp = (int) low;
        highTemp = (int) high;
        tempsView.setText(lowTemp + celsius_constant + " , " + highTemp + celsius_constant);

        String uri = "@drawable/i"+iconName;  // where myresource (without the extension) is the file

        int imageResource = getResources().getIdentifier(uri, null, context.getPackageName());

        //Drawable res = getResources().getDrawable(imageResource);
        weatherIcon.setImageResource(imageResource);
        this.description = description;

    }


    public void setTextToSpeech(TextToSpeech textToSpeech) {
        this.textToSpeech = textToSpeech;
        doTextToSpeech();
    }

    public void doTextToSpeech() {

        Log.d(TAG, "doTextToSpeech: description hindi test: "+description);
        String toSpeak =  HindiConstants.weatherTodayIs + description+ HindiConstants.and + HindiConstants.lowToday + lowTemp + HindiConstants.celcius + HindiConstants.hai + HindiConstants.andHighToday + highTemp + HindiConstants.celcius + HindiConstants.hai;
        Log.d(TAG, "doTextToSpeech: "+toSpeak);
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "meowy");
    }

}
