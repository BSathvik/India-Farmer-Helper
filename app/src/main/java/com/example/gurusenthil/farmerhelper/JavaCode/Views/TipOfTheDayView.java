package com.example.gurusenthil.farmerhelper.JavaCode.Views;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gurusenthil.farmerhelper.R;

import java.util.Locale;

/**
 * Created by GuruSenthil on 12/5/16.
 */

public class TipOfTheDayView extends QuestionAnswerModelView {

    private Context context;
    private TextView tipOfTheDayTextView;

    private final String TAG = "TipOfTheDayView";

    private TextToSpeech textToSpeech;
    private String tip;

    public TipOfTheDayView(Context context, TextToSpeech textToSpeech) {
        super(context);
        this.context = context;
        inflateView();
        this.textToSpeech = textToSpeech;
    }

    public TipOfTheDayView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        inflateView();
    }

    public TipOfTheDayView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.context = context;
        inflateView();
    }

    private void inflateView() {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.tip_of_the_day_layout, getAnswerView(), true);

        tipOfTheDayTextView = (TextView) findViewById(R.id.tip_of_the_day_text_view);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doTextToSpeech();
            }
        });
    }

    public void setTipOfTheDay(String tip) {
        tipOfTheDayTextView.setText(tip);
        this.tip = tip;
        doTextToSpeech();
    }

    private void doTextToSpeech() {
        textToSpeech.speak(tip, TextToSpeech.QUEUE_FLUSH, null, "meowy");

    }
}
