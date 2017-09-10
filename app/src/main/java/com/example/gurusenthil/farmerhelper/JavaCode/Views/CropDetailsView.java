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
import android.widget.Toast;

import com.example.gurusenthil.farmerhelper.JavaCode.Other.HindiConstants;
import com.example.gurusenthil.farmerhelper.R;

import java.util.Locale;

/**
 * Created by GuruSenthil on 12/7/16.
 */


public class CropDetailsView extends QuestionAnswerModelView {

    private Context context;
    private ImageView cropIcon;
    private TextView cropName;
    private TextView soilType;
    private TextView fertility;
    private TextView drainage;
    private TextView salinity;
    private TextView pests;
    private TextView diseases;
    private TextView weatherRequired;
    private TextView rainRequired;

    private TextToSpeech textToSpeech;

    private String toSpeak;

    private final String TAG = "CropDetailsView";


    public CropDetailsView(Context context, TextToSpeech textToSpeech){
        super(context);
        this.context = context;
        inflateView();
        this.textToSpeech = textToSpeech;
    }

    public CropDetailsView(Context context, AttributeSet attrs){
        super(context,attrs);
        this.context = context;
        inflateView();
    }

    public CropDetailsView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs, defStyle);
        this.context = context;
        inflateView();
    }

    private void inflateView() {

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.crop_details_view_layout, getAnswerView(), true);

        cropIcon = (ImageView) findViewById(R.id.crop_picture);
        cropName = (TextView) findViewById(R.id.crop_name);
        soilType = (TextView) findViewById(R.id.soil_type_requirement);
        fertility= (TextView) findViewById(R.id.fertility_requirement);
        drainage = (TextView) findViewById(R.id.drainage_requirement);
        salinity = (TextView) findViewById(R.id.salinity_requirement);
        pests = (TextView) findViewById(R.id.crop_pests_info);
        diseases = (TextView) findViewById(R.id.crop_diseases);
        weatherRequired = (TextView) findViewById(R.id.weather_required);
        rainRequired = (TextView) findViewById(R.id.rain_required);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doTextToSpeech();
            }
        });

    }

    public void setCropInfo(String cropName, String soilType, String fertility, String drainage, String salinity, String pests, String diseases, String weatherRequired, String rainRequred, String icon) {
        //TODO: Set the crop icon based on the enumeration value.

        String uri = "@drawable/"+icon;

        int imageResource = getResources().getIdentifier(uri, null, context.getPackageName());

        //Drawable res = getResources().getDrawable(imageResource);
        cropIcon.setImageResource(imageResource);

        this.cropName.setText(cropName);
        this.soilType.setText(soilType);
        this.fertility.setText(fertility);
        this.drainage.setText(drainage);
        this.salinity.setText(salinity);
        this.pests.setText(pests);
        this.diseases.setText(diseases);
        this.weatherRequired.setText(weatherRequired);
        this.rainRequired.setText(rainRequred);

        toSpeak = cropName + ". " + HindiConstants.soilTypeIs + soilType + HindiConstants.hai + ". " + fertility + HindiConstants.hai + drainage + HindiConstants.required + HindiConstants.hai + salinity + HindiConstants.required + HindiConstants.hai + HindiConstants.pestsInclude + pests + HindiConstants.hai + HindiConstants.diseasesInclude + diseases + weatherRequired + HindiConstants.required + HindiConstants.hai;

        doTextToSpeech();
    }

    private void doTextToSpeech() {

        Log.d(TAG, "doTextToSpeech: "+toSpeak);
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "meowy");
    }


}
