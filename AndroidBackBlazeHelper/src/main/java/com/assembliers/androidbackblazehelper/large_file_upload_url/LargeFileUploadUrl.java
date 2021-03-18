/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/18/21 2:16 PM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.large_file_upload_url;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.assembliers.androidbackblazehelper.start_large_file.LargeFileInterface;
import com.assembliers.androidbackblazehelper.start_large_file.LargeFileModel;
import com.assembliers.androidbackblazehelper.start_large_file.StartLargeFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class LargeFileUploadUrl {
    private String accountAuthorizationToken = ""; // Provided by b2_authorize_account
    private String apiUrl = ""; // Provided by b2_authorize_account


    private String bucketId = ""; // The ID of the destination bucket. The bucket ID is provided by b2_create_bucket, b2_list_buckets.
    private String fileName = ""; // Name of the file as it will appear in B2
    private String fileContentType = ""; // Content type of the file


    public LargeFileUploadUrl(String accountAuthorizationToken, String apiUrl, String bucketId, String fileName, String fileContentType) {
        this.accountAuthorizationToken = accountAuthorizationToken;
        this.apiUrl = apiUrl;
        this.bucketId = bucketId;
        this.fileName = fileName;
        this.fileContentType = fileContentType;
    }



    public void getLargeFileUrlData(Context context, LargeFileUploadUrlInterface listener) {
        StartLargeFile startLargeFile = new StartLargeFile(apiUrl,bucketId,fileName ,fileContentType , accountAuthorizationToken);
        startLargeFile.getLargeFileData(context,model -> {

            JSONObject postData = new JSONObject();
            try {
                postData.put("fileId" , model.getFileId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiUrl + "/b2api/v2/b2_get_upload_part_url", postData, response -> {

                try {

                    LargeFileUrlModel urlModel = new LargeFileUrlModel(response);
                    if (listener != null)
                        listener.onRetrieveData(urlModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }, Throwable::printStackTrace) {
                @Override
                public Map<String, String> getHeaders() {


                    return LargeFileUploadUrl.this.getParams();
                }
            };

            requestQueue.add(jsonObjectRequest);
        });



    }



    private Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();


        params.put("Authorization", accountAuthorizationToken);
        params.put("Content-Type", "application/json");
        params.put("charset", "utf-8");

        return params;


    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }


}
