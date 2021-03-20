/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/18/21 2:16 PM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.upload_auth;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UploadAuth {

    private String bucketId ,accountAuthorizationToken,  apiUrl;
    private UploadAuthListener uploadAuthListener;
    private Context context;
    private Map<String, String> customHeaders;

    public UploadAuth(Context context, String bucketId, String accountAuthorizationToken, String apiUrl) {
        this.context=context;
        this.bucketId = bucketId;
        this.accountAuthorizationToken = accountAuthorizationToken;
        this.apiUrl = apiUrl;
    }




    public void getUploadAuthData(){

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiUrl + "/b2api/v2/b2_get_upload_url", getPostData(), response -> {

            try {
                UploadAuthModel model = new UploadAuthModel(response);

                if(uploadAuthListener!=null)
                    uploadAuthListener.onResponseRetrieved(model);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, Throwable::printStackTrace){
            @Override
            public Map<String, String> getHeaders() {


                return UploadAuth.this.getParams();
            }
        };

        requestQueue.add(jsonObjectRequest);


    }


    private  Map<String, String> getParams(){
        if(customHeaders ==null || customHeaders.isEmpty()) {
            Map<String, String> params = new HashMap<String, String>();


            params.put("Authorization", accountAuthorizationToken);
            params.put("Content-Type", "application/x-www-form-urlencoded");
            params.put("charset", "utf-8");
            return params;
        }
        else {
            return customHeaders;
        }
    }


    /**
     * @param customHeaders add custom headers for the http connection
     * @apiNote WARNING this will override the default headers
     */
    public void addCustomConnectionHeaders(Map<String, String> customHeaders){
        this.customHeaders = customHeaders;
    }


    public void setUploadAuthListener(UploadAuthListener uploadAuthListener){
        this.uploadAuthListener = uploadAuthListener;
    }



    private JSONObject getPostData(){
        JSONObject postData = new JSONObject();
        try {
            postData.put("bucketId", bucketId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }

}
