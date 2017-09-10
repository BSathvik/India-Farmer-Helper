package com.example.gurusenthil.farmerhelper.JavaCode.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gurusenthil.farmerhelper.JavaCode.Other.ServerRequestManager;
import com.example.gurusenthil.farmerhelper.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ServerRequestManager.initManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent threadViewIntent = new Intent(getApplicationContext(), MainActivity.class);
                threadViewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                threadViewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(threadViewIntent);
                overridePendingTransition(0,0);
            }
        }, 1500);
    }
}
