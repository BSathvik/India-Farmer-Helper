package com.example.gurusenthil.farmerhelper.JavaCode.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.Response;
import com.example.gurusenthil.farmerhelper.JavaCode.Other.ServerConstants;
import com.example.gurusenthil.farmerhelper.JavaCode.Other.ServerRequestManager;
import com.example.gurusenthil.farmerhelper.JavaCode.Views.CropDetailsView;
import com.example.gurusenthil.farmerhelper.JavaCode.Views.CropPersistentView;
import com.example.gurusenthil.farmerhelper.JavaCode.Views.CropQuestionAnswerView;
import com.example.gurusenthil.farmerhelper.JavaCode.Views.LearnEnglishView;
import com.example.gurusenthil.farmerhelper.JavaCode.Views.TipOfTheDayView;
import com.example.gurusenthil.farmerhelper.JavaCode.Views.Weather1DayQuestionAnswerView;
import com.example.gurusenthil.farmerhelper.JavaCode.Views.Weather3DayQuestionAnswerView;
import com.example.gurusenthil.farmerhelper.JavaCode.Views.WeatherPersistentView;
import com.example.gurusenthil.farmerhelper.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton speechButton;
    private WeatherPersistentView weatherPersistentView;
    private CropPersistentView cropPersistentView;

    private LinearLayout questionsAnswersLayout;
    private ScrollView questionsScrollView;

    private ServerRequestManager.RequestType currentType = ServerRequestManager.RequestType.Internet;

    private final static String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1234;
    private ProgressDialog progressDialog;
    private boolean requestSent = false;
    private boolean persistentCropsLoaded = false;

    private static String currentQuestion;

    private TextToSpeech textToSpeech;

    private String currentEncodedSms = "";


    private BroadcastReceiver QASMSReceivedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            speechButton.setClickable(true);
            if (requestSent) {
                progressDialog.dismiss();
                requestSent = false;
            }

            Log.d(TAG, "onReceive: called the sms received broadcast receiver!!");

            String encodedPartSMS = intent.getStringExtra("sms");
            currentEncodedSms += encodedPartSMS;
            if (encodedPartSMS.length() < 67){
                Log.d(TAG, "onReceive: encoded full sms: "+ currentEncodedSms);
                decode(currentEncodedSms);
            }

        }
    };

    int i = 1;
    private void decode(String encoded){
        Log.d(TAG, "decode: num hits: "+i);
        Log.d(TAG, "onReceive: decoded full sms: "+encoded+"  num hits: "+i);
        i++;
        try {
            JSONObject json = new JSONObject(encoded.replace("<", "{").replace(">", "}"));
            Log.d(TAG, "onReceive: decoded json: " + json.toString());
            updateUIUsingJSONData(json);
            currentEncodedSms = "";
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "onReceive: wrong numero or bad json");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hookupViews();





















        getPersistentData();

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newVoiceRequestButtonTapped();
            }
        });

        registerReceiver(QASMSReceivedBroadcastReceiver, new IntentFilter("QA_SMS_RECEIVED"));

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.forLanguageTag("hi"));
                    textToSpeech.setSpeechRate((float)0.7);
                }else {
                    Log.d(TAG, "Tts error");
                }
            }
        });

    }

    private void hookupViews() {
        speechButton = (FloatingActionButton) findViewById(R.id.speech_button);
        weatherPersistentView = (WeatherPersistentView) findViewById(R.id.weather_persistent_view);
        questionsAnswersLayout = (LinearLayout) findViewById(R.id.questions_answers_layout);
        questionsScrollView = (ScrollView) findViewById(R.id.questions_scrollview);
    }

    private void newVoiceRequestButtonTapped() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hindi Voice Recognition...");

        startActivityForResult(intent, REQUEST_CODE);

        speechButton.setClickable(false);
    }

    private void getPersistentData() {

        ServerRequestManager serverRequestManager = ServerRequestManager.getInstance();
        serverRequestManager.persistentDataRequest(this, ServerRequestManager.RequestType.SMS, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: response from server: " + response.toString());
                updateUIUsingJSONData(response);
            }
        }, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: response from server: " + response.toString());
                updateUIUsingJSONData(response);
            }
        });
        currentEncodedSms = "";

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenString = matches.get(0);
            currentQuestion = spokenString;
            Log.d(TAG, "Interpreted Sentence: "+spokenString);
            sendRequestToServer(spokenString);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendRequestToServer(String request){
        ServerRequestManager serverRequestManager = ServerRequestManager.getInstance();
        serverRequestManager.makeRequest(this, ServerRequestManager.RequestType.SMS, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "onResponse: response from server: "+response);
                updateUIUsingJSONData(response);
                speechButton.setClickable(true);
                progressDialog.dismiss();
                requestSent = false;

            }
        });

        //progressDialog = ProgressDialog.show(this, "Querying Server...", "Please wait", true);
        //requestSent = true;
        currentEncodedSms = "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(QASMSReceivedBroadcastReceiver);
    }

    private void updateUIUsingJSONData(JSONObject jsonObject){

        try {
            String type = jsonObject.getString(ServerConstants.answerType);
            if (type.equals(ServerConstants.persistentOneDayWeather)){
                weatherPersistentView.setData(jsonObject.getDouble(ServerConstants.high), jsonObject.getDouble(ServerConstants.low), jsonObject.getString(ServerConstants.icon), jsonObject.getString(ServerConstants.description));
                weatherPersistentView.setTextToSpeech(textToSpeech);
            }else if (type.equals(ServerConstants.ThreeDayWeather)){
                Weather3DayQuestionAnswerView weather3DayQuestionAnswerView = new Weather3DayQuestionAnswerView(this, textToSpeech);
                JSONObject todayInfo = jsonObject.getJSONArray("data").optJSONObject(0);
                weather3DayQuestionAnswerView.setTodayInfo(todayInfo.getString(ServerConstants.icon), todayInfo.getString(ServerConstants.high), todayInfo.getString(ServerConstants.low), (int)todayInfo.getDouble(ServerConstants.wind_speed), (int)todayInfo.getDouble(ServerConstants.wind_direction), "0", todayInfo.getString(ServerConstants.description));
                JSONObject tomorrowInfo = jsonObject.getJSONArray("data").optJSONObject(1);
                weather3DayQuestionAnswerView.setTomorrowInfo(tomorrowInfo.getString(ServerConstants.icon), tomorrowInfo.getString(ServerConstants.high), tomorrowInfo.getString(ServerConstants.low), (int)tomorrowInfo.getDouble(ServerConstants.wind_speed), (int)tomorrowInfo.getDouble(ServerConstants.wind_direction), "0", tomorrowInfo.getString(ServerConstants.description));
                JSONObject dayAfterInfo = jsonObject.getJSONArray("data").optJSONObject(2);
                weather3DayQuestionAnswerView.setDayAfterInfo(dayAfterInfo.getString(ServerConstants.icon), dayAfterInfo.getString(ServerConstants.high), dayAfterInfo.getString(ServerConstants.low), (int)dayAfterInfo.getDouble(ServerConstants.wind_speed), (int)dayAfterInfo.getDouble(ServerConstants.wind_direction), "0", dayAfterInfo.getString(ServerConstants.description));
                weather3DayQuestionAnswerView.setQuestionText(currentQuestion);
                questionsAnswersLayout.addView(weather3DayQuestionAnswerView);
            }else if (type.equals(ServerConstants.OneDayWeather)){
                Weather1DayQuestionAnswerView weather1DayQuestionAnswerView = new Weather1DayQuestionAnswerView(this, textToSpeech);
                weather1DayQuestionAnswerView.setData(jsonObject.getDouble(ServerConstants.high), jsonObject.getDouble(ServerConstants.low), jsonObject.getString(ServerConstants.icon), jsonObject.getString(ServerConstants.description));
                weather1DayQuestionAnswerView.setQuestionText(currentQuestion);
                questionsAnswersLayout.addView(weather1DayQuestionAnswerView);
            }else if (type.equals(ServerConstants.tipOfTheDay)){
                TipOfTheDayView tipOfTheDayView = new TipOfTheDayView(this, textToSpeech);
                tipOfTheDayView.setTipOfTheDay(jsonObject.getString(ServerConstants.tip));
                tipOfTheDayView.setQuestionText(currentQuestion);
                questionsAnswersLayout.addView(tipOfTheDayView);
            }else if (type.equals(ServerConstants.wordOfTheDay)){
                LearnEnglishView learnEnglishView = new LearnEnglishView(this, textToSpeech);
                learnEnglishView.setEnglishTip(jsonObject.getString(ServerConstants.tip));
                learnEnglishView.setQuestionText(currentQuestion);
                questionsAnswersLayout.addView(learnEnglishView);
            }else if (type.equals(ServerConstants.cropPriceRequest)){

                CropQuestionAnswerView questionAnswerModelView = new CropQuestionAnswerView(this, textToSpeech);
                questionAnswerModelView.setInfo(jsonObject.getString(ServerConstants.crop_name), jsonObject.getString(ServerConstants.crop_price), jsonObject.getString(ServerConstants.crop_city), jsonObject.getString(ServerConstants.crop_distance));
                questionAnswerModelView.setQuestionText(currentQuestion);
                questionsAnswersLayout.addView(questionAnswerModelView);

            }else if (type.equals(ServerConstants.cropInfo)){

                CropDetailsView cropDetailsView = new CropDetailsView(this, textToSpeech);
                cropDetailsView.setQuestionText(currentQuestion);
                cropDetailsView.setCropInfo(jsonObject.getString(ServerConstants.crop_info_name), jsonObject.getString(ServerConstants.crop_soil), jsonObject.getString(ServerConstants.crop_manure), jsonObject.getString(ServerConstants.crop_drainage), "", jsonObject.getString(ServerConstants.crop_pests), jsonObject.getString(ServerConstants.crop_disease), jsonObject.getString(ServerConstants.crop_temp), jsonObject.getString(ServerConstants.crop_water), jsonObject.getString(ServerConstants.crop_icon));
                questionsAnswersLayout.addView(cropDetailsView);
            }
            questionsScrollView.fullScroll(View.FOCUS_DOWN);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
