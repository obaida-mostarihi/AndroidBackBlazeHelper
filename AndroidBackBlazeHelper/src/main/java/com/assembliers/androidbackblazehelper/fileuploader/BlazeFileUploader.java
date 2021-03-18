/*
 * Created by Obaida Al Mostarihi  3/17/21 10:10 AM
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * Last modified:  3/17/21 10:10 AM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 */

package com.assembliers.androidbackblazehelper.fileuploader;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.assembliers.androidbackblazehelper.client.BlazeClient;
import com.assembliers.androidbackblazehelper.client.ClientListener;
import com.assembliers.androidbackblazehelper.client.ClientModel;
import com.assembliers.androidbackblazehelper.upload_auth.UploadAuth;
import com.google.gson.JsonElement;

import org.json.JSONObject;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlazeFileUploader {


    private Context context;
    private BlazeClient blazeClient;
    private String bucketId;
    private UploadListener uploadingListener;
    private boolean isAuthed = false;
    private String accountAuthorizationToken, apiUrl;
    private String contentType = "";

    private ArrayList<MultiFile> files;

    //Upload Auth
    private String uploadUrl;
    private String uploadAuthorizationToken;

    private boolean isMultiUpload = false;

    public BlazeFileUploader(BlazeClient blazeClient, String bucketId) {
        this.context = blazeClient.getContext();
        this.blazeClient = blazeClient;
        this.bucketId = bucketId;

    }


    public void startUploadingMultipleFiles(ArrayList<MultiFile> files)  {

        this.files = files;
        isMultiUpload = true;



            try {
            uploadMultiImages(new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void uploadMultiImages(ArrayList<MultiFile> file) throws IOException {
        MultiFile fileModel = files.get(file.size());
        if (!isAuthed) {


            getClientData(fileModel.getFileUri(), fileModel.getFileName(),fileModel.getContentType());
        }else{


        InputStream iStream = context.getContentResolver().openInputStream(fileModel.getFileUri());
        byte[] inputData = getBytes(iStream);
        uploadFile(fileModel.getFileUri(), fileModel.getFileName(),
                inputData, fileModel.getContentType(), () -> {

                    file.add(fileModel);
                    if (file.size() == files.size()) {


                        uploadingListener.onUploadFinished("All files uploaded" ,true);
                    }else{
                        uploadMultiImages(file);
                    }

                    return null;
                });

        }
    }


    public void startUploading(Uri fileUri, String fileName, String contentType) {
        isMultiUpload = false;
        try {

            InputStream iStream = context.getContentResolver().openInputStream(fileUri);
            byte[] inputData = getBytes(iStream);
            if (uploadingListener != null)
                uploadingListener.onUploadStarted();


            if (!isAuthed) {

                getClientData(fileUri, fileName, contentType);
            } else {


                uploadFile(fileUri, fileName, inputData, contentType, null);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startUploading(Uri fileUri, String fileName) {
        isMultiUpload = false;
        try {

            InputStream iStream = context.getContentResolver().openInputStream(fileUri);
            byte[] inputData = getBytes(iStream);
            if (uploadingListener != null)
                uploadingListener.onUploadStarted();


            if (!isAuthed) {

                getClientData(fileUri, fileName, contentType);
            } else {


                uploadFile(fileUri, fileName, inputData, "", null);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadFile(Uri fileUri, String fileName, byte[] inputData, String contentType, Callable<Void> onFinish) {
        URL url = null;
        String path = null;
        try {
            url = new URL(uploadUrl);
            path = url.getPath();
            path = path.replaceFirst("/", "");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        String baseUrl = url.getProtocol() + "://" + url.getHost();


        UploadInterface uploadInterface = ApiClient.getClient(baseUrl).create(UploadInterface.class);

        ProgressRequestBody requestBody = new ProgressRequestBody(
                context,
                new ProgressRequestBody.UploadInfo(fileUri, inputData.length),
                (progress, total) -> {


                    int percentage = (int) ((progress * 100.0f) / total);

                    if (uploadingListener != null)
                        uploadingListener.onUploadProgress(percentage, progress, total);

                }
        );
        requestBody.setContentType(contentType);

// Upload
        Call<JsonElement> call = uploadInterface.uploadFile(path, requestBody, uploadAuthorizationToken,
                SHAsum(inputData), fileName);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                if (uploadingListener != null){
                    uploadingListener.onUploadFinished(response.toString() , !isMultiUpload);


                }
                if (onFinish != null) {
                    try {
                        onFinish.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                if (uploadingListener != null)
                    uploadingListener.onUploadFailed((Exception) t);

            }
        });

    }

    private void getClientData(Uri fileUri, String fileName, String contentType) {

        blazeClient.setClientListener(new ClientListener() {
            @Override
            public void onConnectionStarted() {

            }

            @Override
            public void onRetrievingData(ClientModel data, JSONObject response) {

                accountAuthorizationToken = data.getAuthorizationToken();
                apiUrl = data.getApiUrl();
                UploadAuth uploadAuth = new UploadAuth(context, bucketId, accountAuthorizationToken, apiUrl);

                uploadAuth.getUploadAuthData();
                uploadAuth.setUploadAuthListener(uploadAuthModel -> {
                    uploadUrl = uploadAuthModel.getUploadUrl();
                    uploadAuthorizationToken = uploadAuthModel.getAuthorizationToken();
                    isAuthed = true;
                    if(!isMultiUpload)
                    startUploading(fileUri, fileName, contentType);
                    else
                    startUploadingMultipleFiles(files);
                });

            }

            @Override
            public void onFailure(Exception e) {
                if (uploadingListener != null)
                    uploadingListener.onUploadFailed(e);
            }
        });
    }


    public void setOnUploadingListener(UploadListener uploadingListener) {
        this.uploadingListener = uploadingListener;
    }


    private static String SHAsum(byte[] convertme) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return byteArray2Hex(md.digest(convertme));
    }

    private static String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }


    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


}
