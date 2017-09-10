package com.example.gurusenthil.farmerhelper.JavaCode.Views;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gurusenthil.farmerhelper.JavaCode.Other.HindiConstants;
import com.example.gurusenthil.farmerhelper.R;

import java.util.Locale;

/**
 * Created by GuruSenthil on 12/4/16.
 */

public class Weather3DayQuestionAnswerView extends QuestionAnswerModelView {

    private Context context;
    private final static String TAG = "WeatherQuestionAnswer";

    private String celsius_constant = "°C";

    private ImageView day1Icon;
    private TextView day1LowTempTextView;
    private TextView day1HighTempTextView;
    private TextView day1WindTextView;
    private TextView day1RainTextView;

    private ImageView day2Icon;
    private TextView day2Low;
    private TextView day2High;
    private TextView day2Wind;
    private TextView day2Rain;

    private ImageView day3Icon;
    private TextView day3Low;
    private TextView day3High;
    private TextView day3Wind;
    private TextView day3Rain;

    private TextToSpeech textToSpeech;
    private String day1ttsString;
    private String day2ttsString;
    private String day3ttsString;

    public Weather3DayQuestionAnswerView(Context context, TextToSpeech textToSpeech) {
        super(context);

        this.context = context;
        inflateView();
        this.textToSpeech = textToSpeech;
    }

    public Weather3DayQuestionAnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        inflateView();
    }

    public Weather3DayQuestionAnswerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
        inflateView();
    }

    private void inflateView() {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.weather_question_view_layout, getAnswerView(), true);

        day1Icon = (ImageView) findViewById(R.id.day1weathericon);
        day1HighTempTextView = (TextView) findViewById(R.id.day1HighTemp);
        day1LowTempTextView = (TextView) findViewById(R.id.day1LowTemp);
        day1WindTextView = (TextView) findViewById(R.id.day1Wind);
        day1RainTextView = (TextView) findViewById(R.id.day1Rain);

        day2Icon = (ImageView) findViewById(R.id.day2Icon);
        day2Low = (TextView) findViewById(R.id.day2Low);
        day2High = (TextView) findViewById(R.id.day2High);
        day2Wind = (TextView) findViewById(R.id.day2Wind);
        day2Rain = (TextView) findViewById(R.id.day2Rain);

        day3Icon = (ImageView) findViewById(R.id.day3Icon);
        day3Low = (TextView) findViewById(R.id.day3Low);
        day3High = (TextView) findViewById(R.id.day3High);
        day3Wind = (TextView) findViewById(R.id.day3Wind);
        day3Rain = (TextView) findViewById(R.id.day3Rain);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doTextToSpeech();
            }
        });

    }

    public void setTodayInfo(String iconName, String highTemp, String lowTemp, int windSpeed, int windDirection , String rain, String description){
        String uri = "@drawable/i"+iconName;

        int imageResource = getResources().getIdentifier(uri, null, context.getPackageName());

        //Drawable res = getResources().getDrawable(imageResource);
        day1Icon.setImageResource(imageResource);

        day1HighTempTextView.setText("High: "+(int)Double.parseDouble(highTemp)+celsius_constant);
        day1LowTempTextView.setText("Low: "+(int)Double.parseDouble(lowTemp)+celsius_constant);
        day1WindTextView.setText(windSpeed + " km/hr " + parseWindDirectionEnglish(windDirection));
        day1RainTextView.setText(String.format("%s cm", rain));

        day1ttsString = HindiConstants.weatherTodayIs + description + HindiConstants.hai + HindiConstants.and + HindiConstants.low + lowTemp + HindiConstants.celcius + HindiConstants.hai + HindiConstants.andHigh + highTemp + HindiConstants.celcius + HindiConstants.hai + HindiConstants.and + HindiConstants.wind + windSpeed + " km/hr " + parseWindDirectionHindi(windDirection) +HindiConstants.hai + ". ";
    }

    public void setTomorrowInfo(String iconName, String highTemp, String lowTemp, int windSpeed, int windDirection, String rain, String description){

        String uri = "@drawable/i"+iconName;

        int imageResource = getResources().getIdentifier(uri, null, context.getPackageName());

        //Drawable res = getResources().getDrawable(imageResource);
        day2Icon.setImageResource(imageResource);

        day2High.setText(String.format("%s%s", (int)Double.parseDouble(highTemp), celsius_constant));
        day2Low.setText(String.format("%s%s", (int)Double.parseDouble(lowTemp), celsius_constant));
        day2Wind.setText(windSpeed + " km/hr " + parseWindDirectionEnglish(windDirection));
        day2Rain.setText(String.format("%s cm", rain));

        day2ttsString = HindiConstants.weatherTomorrowIs + description + HindiConstants.hai + HindiConstants.and + HindiConstants.low + lowTemp + HindiConstants.celcius + HindiConstants.hai + HindiConstants.andHigh + highTemp + HindiConstants.celcius + HindiConstants.hai + HindiConstants.and + HindiConstants.wind + windSpeed + " km/hr " + parseWindDirectionHindi(windDirection) + HindiConstants.hai + ". ";
    }

    public void setDayAfterInfo(String iconName, String highTemp, String lowTemp, int windSpeed, int windDirection, String rain, String description){
        String uri = "@drawable/i"+iconName;

        int imageResource = getResources().getIdentifier(uri, null, context.getPackageName());

        //Drawable res = getResources().getDrawable(imageResource);
        day3Icon.setImageResource(imageResource);

        day3High.setText(String.format("%s%s", (int)Double.parseDouble(highTemp), celsius_constant));
        day3Low.setText(String.format("%s%s", (int)Double.parseDouble(lowTemp), celsius_constant));
        day3Wind.setText(windSpeed + " km/hr " + parseWindDirectionEnglish(windDirection));
        day3Rain.setText(String.format("%s cm", rain));

        day3ttsString = HindiConstants.weatherDayAfterIs + description + HindiConstants.hai + HindiConstants.and + HindiConstants.low + lowTemp + HindiConstants.celcius + HindiConstants.hai + HindiConstants.andHigh + highTemp + HindiConstants.celcius + HindiConstants.hai + HindiConstants.and + HindiConstants.wind + windSpeed + " km/hr " + parseWindDirectionHindi(windDirection) + HindiConstants.hai + ". ";
        doTextToSpeech();
    }

    public void doTextToSpeech() {
        String toSpeak = day1ttsString + day2ttsString + day3ttsString;
        Log.d(TAG, "doTextToSpeech: "+toSpeak);
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "meowy");
    }

    private String parseWindDirectionEnglish(int direction) {
        if (direction <= 90 && direction >= 0){
            return "NE";
        }else if (direction >= 90 && direction <= 180){
            return "SE";
        }else if (direction >= 180 && direction <= 270){
            return "SW";
        }else if (direction >= 270 && direction <= 360){
            return "NW";
        }
        return "";
    }

    private String parseWindDirectionHindi(int direction) {
        if (direction <= 90 && direction >= 0){
            return " ईशान कोण ";
        }else if (direction >= 90 && direction <= 180){
            return " दक्षिण-पूर्व ";
        }else if (direction >= 180 && direction <= 270){
            return " उत्तर पश्चिम ";
        }else if (direction >= 270 && direction <= 360){
            return " उत्तर पश्चिम ";
        }
        return "";
    }


}
