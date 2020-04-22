package com.example.vocabuilder;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ConnectionHandler extends Volley{
    private Context context;
    private String url;
    private RequestQueue requestQueue;
    private HashMap<String, String> authHeaders;

    public ConnectionHandler(Context context, HashMap<String, String> authHeaders, String url){
        this.context = context;
        this.url = url;
        this.authHeaders = authHeaders;
        requestQueue = Volley.newRequestQueue(context);
    }


    public void getResponse(final OnSuccessCallback successCallback){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                successCallback.onSuccess(result);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, error.networkResponse + "Word not present,please try again", Toast.LENGTH_SHORT).show();
                successCallback.onSuccess(null);
                return;
            }

        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = authHeaders;
                if(headers != null && headers.size() != 0)
                    return headers;
                else
                    return super.getHeaders();
            }
        };
        requestQueue.add(stringRequest);
    }
}
