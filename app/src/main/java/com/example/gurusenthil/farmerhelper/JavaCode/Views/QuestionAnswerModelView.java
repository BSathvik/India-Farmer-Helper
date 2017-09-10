package com.example.gurusenthil.farmerhelper.JavaCode.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gurusenthil.farmerhelper.R;

/**
 * Created by GuruSenthil on 12/4/16.
 */

public class QuestionAnswerModelView extends LinearLayout {

    private TextView questionView;
    private LinearLayout answerView;
    private Context context;

    public QuestionAnswerModelView(Context context){
        super(context);
        this.setLayoutParams(getParams());
        this.context = context;
        makeView();
    }

    public QuestionAnswerModelView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setLayoutParams(getParams());
        this.context = context;
        makeView();

    }

    public QuestionAnswerModelView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.setLayoutParams(getParams());
        this.context = context;
        makeView();
    }

    private LayoutParams getParams() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
        return layoutParams;
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void makeView() {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.question_answer_view_layout, this, true);
        questionView = (TextView) findViewById(R.id.question_textview);
        answerView = (LinearLayout) findViewById(R.id.answer_layout);
    }

    public LinearLayout getAnswerView() {
        return answerView;
    }

    public void setQuestionText(String question){
        questionView.setText(String.format("Question: %s", question));
    }

}
