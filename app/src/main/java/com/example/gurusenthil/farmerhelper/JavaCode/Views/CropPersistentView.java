package com.example.gurusenthil.farmerhelper.JavaCode.Views;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gurusenthil.farmerhelper.JavaCode.Other.HindiConstants;
import com.example.gurusenthil.farmerhelper.R;

import java.util.Locale;

/**
 * Created by GuruSenthil on 12/5/16.
 */

public class CropPersistentView extends LinearLayout {

    private final static String TAG = "CropPersistentView";

    private Context context;
    private TextView nameAndPrice;
    private TextView locationAndDistance;

    private String rupeeSymbol = "₹";
    private TextToSpeech textToSpeech;

    private static String toSpeak;

    public CropPersistentView(Context context){
        super(context);
        this.context = context;
        inflateView();

    }

    public CropPersistentView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        inflateView();
    }

    public CropPersistentView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.context = context;
        inflateView();
    }

    private void inflateView() {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.crop_persistent_view_layout, this, true);

        nameAndPrice = (TextView) findViewById(R.id.persistent_crop_name_and_price);
        locationAndDistance = (TextView) findViewById(R.id.persistent_market_location_and_distance);


        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doTextToSpeech();
            }
        });
    }

    public void setInfo(String cropName, String cropPrice, String marketName, String distance){
        nameAndPrice.setText(cropName + ": " + rupeeSymbol + cropPrice);
        locationAndDistance.setText(marketName + " ("+((int)Float.parseFloat(distance))+" km)");

        toSpeak = cropName + HindiConstants.bestPrice + cropPrice + HindiConstants.hai + marketName + HindiConstants.market + HindiConstants.hai;

    }

    public void setTextToSpeech(TextToSpeech textToSpeech) {
        this.textToSpeech = textToSpeech;
        doTextToSpeech();
    }

    private void doTextToSpeech() {
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "meowy");
    }
}
