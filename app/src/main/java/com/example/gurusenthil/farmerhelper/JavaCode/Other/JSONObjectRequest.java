package com.example.gurusenthil.farmerhelper.JavaCode.Other;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by BSathvik on 16/04/16.
 */
public class JSONObjectRequest extends Request<JSONObject>{

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;
    private Priority priority = Priority.NORMAL;
    private Context context = null;

    public JSONObjectRequest(Context context, int method, String url, Map<String, String> params,
                             Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
        this.context = context;
    }


    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {

            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.d("testing response", jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);

            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }


    @Override
    protected void deliverResponse(JSONObject response) {
            listener.onResponse(response);
    }

    @Override
    public Priority getPriority() {
        return priority;
    }
}
