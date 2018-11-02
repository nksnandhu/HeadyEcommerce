package com.nkgroup.headyui.service;


import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class VolleyService {

    //private final Context mContext;

    private final ResponseListener mResponseListener;

    public  VolleyService(ResponseListener listener){
      //  mContext=context;
        mResponseListener=listener;
    }

    public  void  getObjectResponse(){
        String url="https://stark-spire-93433.herokuapp.com/json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, mResponseListener::onObjectListener, error -> Log.e("LOG", error.toString()));

        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        ApplicationControl.getInstance().addToRequestQueue(jsonObjectRequest);

    }


    public  interface  ResponseListener{
        void onObjectListener(JSONObject jsonObject);
    }
}
