/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/18/21 2:16 PM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.fileuploader;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.assembliers.androidbackblazehelper.client.BlazeClient;
import com.assembliers.androidbackblazehelper.client.ClientListener;
import com.assembliers.androidbackblazehelper.client.ClientModel;
import com.assembliers.androidbackblazehelper.upload_auth.UploadAuth;
import com.assembliers.androidbackblazehelper.upload_models.UploadResponse;

import org.json.JSONObject;


import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;
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
        isAuthed = false;

    }


    public void startUploadingMultipleFiles(ArrayList<MultiFile> files) {

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


            if (fileModel.getFileUri() != null)
                getClientData(fileModel.getFileUri(), fileModel.getFileName(), fileModel.getContentType());
            else
                getClientData(fileModel.getFileBytes(), fileModel.getFileName(), fileModel.getContentType());

        } else {
            byte[] inputData =null;
            if (fileModel.getFileUri() != null){

                InputStream iStream = context.getContentResolver().openInputStream(fileModel.getFileUri());
          inputData = getBytes(iStream);
                uploadFile(fileModel.getFileUri(), fileModel.getFileName(),
                        inputData, fileModel.getContentType(), () -> {

                            file.add(fileModel);
                            if (file.size() == files.size()) {

                                UploadResponse uploadResponse = new UploadResponse();

                                if (uploadingListener != null)
                                    uploadingListener.onUploadFinished(uploadResponse, true);
                            } else {
                                uploadMultiImages(file);
                            }

                            return null;
                        });
            }else{
                uploadFile(fileModel.getFileBytes(), fileModel.getFileName(), fileModel.getContentType(), () -> {

                            file.add(fileModel);
                            if (file.size() == files.size()) {

                                UploadResponse uploadResponse = new UploadResponse();

                                if (uploadingListener != null)
                                    uploadingListener.onUploadFinished(uploadResponse, true);
                            } else {
                                uploadMultiImages(file);
                            }

                            return null;
                        });
            }


        }
    }


    public void startUploading(Uri fileUri, String fileName, String contentType) {
        this.contentType = contentType;
        isMultiUpload = false;
        try {

            InputStream iStream = context.getContentResolver().openInputStream(fileUri);
            byte[] inputData = getBytes(iStream);
            if (uploadingListener != null)
                uploadingListener.onUploadStarted();


            checkIfAuthed(fileUri, fileName, inputData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startUploading(byte[] fileBytes, String fileName, String contentType) {
        this.contentType = contentType;
        isMultiUpload = false;


        if (uploadingListener != null)
            uploadingListener.onUploadStarted();


        checkIfAuthed(fileBytes, fileName);

    }

    public void startUploading(byte[] fileBytes, String fileName) {
        isMultiUpload = false;


        if (uploadingListener != null)
            uploadingListener.onUploadStarted();


        checkIfAuthed(fileBytes, fileName);

    }

    public void startUploading(Uri fileUri, String fileName) {
        isMultiUpload = false;
        try {

            InputStream iStream = context.getContentResolver().openInputStream(fileUri);
            byte[] inputData = getBytes(iStream);
            if (uploadingListener != null)
                uploadingListener.onUploadStarted();


            checkIfAuthed(fileUri, fileName, inputData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkIfAuthed(byte[] filebytes, String fileName) {
        if (!isAuthed) {

            getClientData(filebytes, fileName, contentType);
        } else {


            uploadFile(filebytes, fileName, contentType, null);


        }
    }

    private void checkIfAuthed(Uri fileUri, String fileName, byte[] inputData) {
        if (!isAuthed) {

            getClientData(fileUri, fileName, contentType);
        } else {


            uploadFile(fileUri, fileName, inputData, contentType, null);


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

        UploadProgressRequestBody requestBody = new UploadProgressRequestBody(
                context,
                new UploadProgressRequestBody.UploadInfo(fileUri, inputData.length),
                (progress, total) -> {


                    int percentage = (int) ((progress * 100.0f) / total);

                    if (uploadingListener != null)
                        uploadingListener.onUploadProgress(percentage, progress, total);

                }
        );
        requestBody.setContentType(contentType);

// Upload
        Call<UploadResponse> call = uploadInterface.uploadFile(path, requestBody, uploadAuthorizationToken,
                SHAsum(inputData), fileName);
        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {


                if (uploadingListener != null) {
                    uploadingListener.onUploadFinished(response.body(), !isMultiUpload);
                    call.cancel();


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
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                if (uploadingListener != null)
                    uploadingListener.onUploadFailed((Exception) t);

            }
        });

    }

    private void uploadFile(byte[] fileBytes, String fileName, String contentType, Callable<Void> onFinish) {
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

        UploadProgressRequestBody requestBody = new UploadProgressRequestBody(
                context,
                new UploadProgressRequestBody.UploadInfo(fileBytes, fileBytes.length),
                (progress, total) -> {


                    int percentage = (int) ((progress * 100.0f) / total);

                    if (uploadingListener != null)
                        uploadingListener.onUploadProgress(percentage, progress, total);

                }
        );
        requestBody.setContentType(contentType);


// Upload
        Call<UploadResponse> call = uploadInterface.uploadFile(path, requestBody, uploadAuthorizationToken,
                SHAsum(fileBytes), fileName);
        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                Log.v("whynull" , response.body().toString());



                if (uploadingListener != null) {
                    uploadingListener.onUploadFinished(response.body(), !isMultiUpload);


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
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                if (uploadingListener != null)
                    uploadingListener.onUploadFailed((Exception) t);

            }
        });

    }

    private void getClientData(byte[] fileBytes, String fileName, String contentType) {

        blazeClient.setClientListener(new ClientListener() {
            @Override
            public void onConnectionStarted() {

            }

            @Override
            public void onRetrievingData(ClientModel data, JSONObject response) {

                Log.v("whynull" , response.toString());
                accountAuthorizationToken = data.getAuthorizationToken();
                apiUrl = data.getApiUrl();
                UploadAuth uploadAuth = new UploadAuth(context, bucketId, accountAuthorizationToken, apiUrl);
                uploadAuth.getUploadAuthData();
                uploadAuth.setUploadAuthListener(uploadAuthModel -> {

                    uploadUrl = uploadAuthModel.getUploadUrl();
                    uploadAuthorizationToken = uploadAuthModel.getAuthorizationToken();
                    isAuthed = true;
                    if (!isMultiUpload)
                        startUploading(fileBytes, fileName, contentType);
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
                    if (!isMultiUpload)
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
