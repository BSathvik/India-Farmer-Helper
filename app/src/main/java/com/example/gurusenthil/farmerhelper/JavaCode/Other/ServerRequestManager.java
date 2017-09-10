package com.example.gurusenthil.farmerhelper.JavaCode.Other;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by GuruSenthil on 12/6/16.
 */

public class ServerRequestManager {

    private final static String TAG = "ServerRequestManager";
    private final static String serverNumber = "9220592205";



    private static ServerRequestManager instance;

    private RequestQueue requestQueue;   //the queue for all of the requests to the server

    public static void initManager(Context context) {
        if (instance == null) {
            instance = new ServerRequestManager();
            instance.requestQueue = Volley.newRequestQueue(context);

        } else {
            Log.d(TAG, "Error manager already initialized!");
        }
    }

    public static ServerRequestManager getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void makeRequest(Context context, RequestType requestType, String request, Response.Listener<JSONObject> listener) {

        Location location;
        double latitude;
        double longitude;
        location = getLastKnownLocation(context);
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        if (requestType.equals(RequestType.SMS)) {

            JSONObject requeste = new JSONObject();
            try {
                requeste.put(ServerConstants.latitude, Double.toString(latitude));
                requeste.put(ServerConstants.longitude, Double.toString(longitude));
                requeste.put("text", Base64.encodeToString(request.getBytes(), Base64.DEFAULT));
                requeste.put("per", "false");
                Log.d(TAG, "makeRequest: hindi text: "+request);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String withoutCurlyBraces = requeste.toString().replace("{", "<").replace("}", ">");
            //Log.d(TAG, "makeRequest: reached send sms for farmer request final request: "+Base64.encodeToString(requeste.toString().getBytes(), Base64.DEFAULT));

            Log.d(TAG, "makeRequest: sms to send: "+withoutCurlyBraces);

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(serverNumber, null, "6PP3T " + withoutCurlyBraces, null, null);


        }else if (requestType.equals(RequestType.Internet)){
            Map<String, String> variables = new HashMap<>();

            JSONObject requeste = new JSONObject();
            try {
                requeste.put(ServerConstants.latitude, Double.toString(latitude));
                requeste.put(ServerConstants.longitude, Double.toString(longitude));
                requeste.put("text", request);
                requeste.put("per", "false");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            variables.put("text", requeste.toString());
            variables.put("number", "9597507628");

            Log.d(TAG, "persistentDataRequest: variables sent: "+variables.toString());

            final JSONObjectRequest requesteee = new JSONObjectRequest(context, Request.Method.POST, ServerConstants.base_url + ServerConstants.smsApiEndpoint, variables, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            getRequestQueue().add(requesteee);
        }
    }

    public void persistentDataRequest(Context context, RequestType requestType, Response.Listener<JSONObject> weatherListener, Response.Listener<JSONObject> cropListener) {
        Location location;
        double latitude;
        double longitude;
        location = getLastKnownLocation(context);
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        if (requestType.equals(RequestType.SMS)) {

            Log.d(TAG, "persistentDataRequest: SMS request");

            JSONObject request = new JSONObject();
            try {
                request.put(ServerConstants.latitude, Double.toString(latitude));
                request.put(ServerConstants.longitude, Double.toString(longitude));
                request.put("text", Base64.encodeToString(" आज का मौसम क्या है ".getBytes(), Base64.DEFAULT));
                request.put("per", "true");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String withoutCurlyBraces = request.toString().replace("{", "<").replace("}", ">");

            Log.d(TAG, "makeRequest: sms to send: "+withoutCurlyBraces);

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(serverNumber, null, "6PP3T " + withoutCurlyBraces, null, null);
        }else if (requestType.equals(RequestType.Internet)){

            Log.d(TAG, "persistentDataRequest: Internet request");
            Map<String, String> variables = new HashMap<>();
            variables.put(ServerConstants.latitude, Double.toString(latitude));
            variables.put(ServerConstants.longitude, Double.toString(longitude));
            variables.put("per", "true");

            Log.d(TAG, "persistentDataRequest: variables sent: "+variables.toString());

            final JSONObjectRequest request = new JSONObjectRequest(context, Request.Method.POST, ServerConstants.base_url + ServerConstants.weather_persistent_endpoint, variables, weatherListener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            getRequestQueue().add(request);


            //for crop persistent info
            Map<String, String> cropvariables = new HashMap<>();

            JSONObject requeste = new JSONObject();
            try {
                requeste.put(ServerConstants.latitude, Double.toString(latitude));
                requeste.put(ServerConstants.longitude, Double.toString(longitude));
                String hindiToSend = "कहां सबसे अच्छी जगह मक्का बेचने के लिए ह";
                requeste.put("text", hindiToSend);
                requeste.put("per", "false");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            cropvariables.put("text", "{\"lat\":\"12.9528859\",\"long\":\"77.6442161\",\"text\":\"कहाँ सबसे अच्छी जगह मक्का बेचने के लिए है\",\"per\":\"false\"}");
            cropvariables.put("number", "9597507628");

            Log.d(TAG, "persistentDataRequest: variables sent: "+cropvariables.toString());

            final JSONObjectRequest requesteee = new JSONObjectRequest(context, Request.Method.POST, ServerConstants.base_url + ServerConstants.smsApiEndpoint, variables, cropListener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            getRequestQueue().add(requesteee);
        }
    }

    private ServerRequestManager() {}

    public enum RequestType {
        SMS, Internet
    }

    LocationManager mLocationManager;

    private Location getLastKnownLocation(Context context) {
        mLocationManager = (LocationManager)context.getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
