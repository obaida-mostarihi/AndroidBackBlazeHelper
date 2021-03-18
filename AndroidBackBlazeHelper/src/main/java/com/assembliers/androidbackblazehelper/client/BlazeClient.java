/*
 * Created by Obaida Al Mostarihi  3/17/21 9:33 AM
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * Last modified:  3/17/21 9:27 AM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 */

package com.assembliers.androidbackblazehelper.client;


import android.content.Context;

import android.os.Build;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class BlazeClient {

    //Variables
    private String applicationKeyId; // Obtained from your B2 account page.
    private String applicationKey; // Obtained from your B2 account page.
    private Context context;
    private Map<String, String> customHeaders;
    //InterFaces
    private ClientListener clientListener;

    /**
     * @param applicationKeyId Obtained from your B2 account page.
     * @param applicationKey   Obtained from your B2 account page.
     */
    public BlazeClient(Context context, String applicationKeyId, String applicationKey) {
        this.applicationKey = applicationKey;
        this.applicationKeyId = applicationKeyId;
        this.context = context;

        throwExceptionIfNoKeys();


    }


    public android.content.Context getContext() {
        return context;
    }

    void getClientAuth() {

        if (clientListener != null)
            clientListener.onConnectionStarted();

        RequestQueue mQueue = Volley.newRequestQueue(context.getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ClientConstants.AUTH_URL, null,
                response -> {
                    try {
                        ClientModel clientModel = new ClientModel(response);


                        if (clientListener != null)
                            clientListener.onRetrievingData(clientModel , response);

                    } catch (JSONException e) {
                        if (clientListener != null)
                            clientListener.onFailure(e);
                    }

                }, Throwable::printStackTrace) { //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() {
              return BlazeClient.this.getParams();
            }
        };
        mQueue.add(jsonObjectRequest);

    }


    /**
     * @param customHeaders add custom headers for the http connection
     * @apiNote WARNING this will override the default headers
     */
    public void addCustomConnectionHeaders(Map<String, String> customHeaders){
        this.customHeaders = customHeaders;
    }


    private  Map<String, String> getParams(){
        if(customHeaders ==null || customHeaders.isEmpty()) {
            Map<String, String> params = new HashMap<String, String>();

            byte[] header = (applicationKeyId + ":" + applicationKey).getBytes();
            String headerForAuthorizeAccount = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                headerForAuthorizeAccount = "Basic " + Base64.getEncoder().encodeToString(header);
            } else {
                headerForAuthorizeAccount = "Basic " + android.util.Base64.encodeToString(header, android.util.Base64.DEFAULT);
            }
            params.put("Authorization", headerForAuthorizeAccount);
            return params;
        }
        else {
            return customHeaders;
        }
    }

    /**
     * @param clientListener the listener has three methods
     *                       onConnectionStarted() when starting to get data
     *                       onRetrievingData(ClientModel data) when retrieved data
     *                       onFailure(Exception e) when something goes wrong
     */
    public void setClientListener(ClientListener clientListener) {
        this.clientListener = clientListener;
        getClientAuth();

    }


    //This will throw an exception if the developer didn't set keys
    private void throwExceptionIfNoKeys() {
        if (applicationKey.isEmpty() || applicationKeyId.isEmpty())
            throw new IllegalStateException("Please fill your keys from your B2 account page.");

    }




}//Class


