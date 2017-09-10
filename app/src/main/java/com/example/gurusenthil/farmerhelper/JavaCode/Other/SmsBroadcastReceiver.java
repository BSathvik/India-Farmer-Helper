package com.example.gurusenthil.farmerhelper.JavaCode.Other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by GuruSenthil on 12/6/16.
 */



public class SmsBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadcastReceiver";

    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            for (Object sm : sms) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sm);

                String smsBody = smsMessage.getMessageBody();
                String address = smsMessage.getOriginatingAddress();

                Log.d(TAG, "SMS received: "+smsBody+" Received From: "+address);
                if (address.equals("DM-020055")) {

                    Log.d(TAG, "onReceive: good address sms received");
                    Intent next = new Intent();
                    next.setAction("QA_SMS_RECEIVED");
                    next.putExtra("sms", smsBody);
                    context.sendBroadcast(next);
                }else {
                    Log.d(TAG, "onReceive: bad address sms received");
                }
            }

        }
    }
}