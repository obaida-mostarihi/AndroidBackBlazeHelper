/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/18/21 2:16 PM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.start_large_file;

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

public class StartLargeFile {

    private String apiUrl = ""; // Provided by b2_authorize_account
    private String bucketId = ""; // The ID of the destination bucket. The bucket ID is provided by b2_create_bucket, b2_list_buckets.
    private String fileName = ""; // Name of the file as it will appear in B2
    private String fileContentType = ""; // Content type of the file
    private String accountAuthorizationToken = "";// Provided by b2_authorize_account


    public StartLargeFile(String apiUrl, String bucketId, String fileName, String fileContentType, String accountAuthorizationToken) {
        this.apiUrl = apiUrl;
        this.bucketId = bucketId;
        this.fileName = fileName;
        this.fileContentType = fileContentType;
        this.accountAuthorizationToken = accountAuthorizationToken;
    }


    public void getLargeFileData(Context context, LargeFileInterface listener) {

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiUrl + "/b2api/v2/b2_start_large_file", getPostData(), response -> {
            Log.v("loool" , response.toString());

            try {

                LargeFileModel model = new LargeFileModel(response);
                if (listener != null)
                    listener.onRetrieveData(model);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, Throwable::printStackTrace) {
            @Override
            public Map<String, String> getHeaders() {


                return StartLargeFile.this.getParams();
            }
        };

        requestQueue.add(jsonObjectRequest);


    }

    private JSONObject getPostData() {
        JSONObject postData = new JSONObject();
        try {
            postData.put("fileName", fileName);
            postData.put("contentType", fileContentType);
            postData.put("bucketId", bucketId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }


    private Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();


        params.put("Authorization", accountAuthorizationToken);
        params.put("Content-Type", "application/json");
        params.put("charset", "utf-8");

        return params;


    }

}
