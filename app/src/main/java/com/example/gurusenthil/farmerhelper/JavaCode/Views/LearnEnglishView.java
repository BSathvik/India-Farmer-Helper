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

public class LearnEnglishView extends QuestionAnswerModelView {

    private Context context;
    private TextView learnEnglishTextView;

    private static final String TAG = "LearnEnglishView";

    private String tip;
    private TextToSpeech textToSpeech;

    public LearnEnglishView(Context context, TextToSpeech textToSpeech) {
        super(context);
        this.context = context;
        inflateView();
        this.textToSpeech = textToSpeech;
    }

    public LearnEnglishView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflateView();
    }

    public LearnEnglishView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        inflateView();
    }

    private void inflateView() {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.learn_english_view_layout, getAnswerView(), true);

        learnEnglishTextView = (TextView) findViewById(R.id.learn_english_text_view);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doTextToSpeech();
            }
        });
    }

    public void setEnglishTip(String tip) {
        learnEnglishTextView.setText(tip);
        this.tip = tip;
        doTextToSpeech();
    }

    private void doTextToSpeech() {
        textToSpeech.speak(tip, TextToSpeech.QUEUE_FLUSH, null, "meowy");
    }
}
