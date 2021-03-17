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
import android.net.Uri;
import android.os.AsyncTask;

import com.assembliers.androidbackblazehelper.client.BlazeClient;
import com.assembliers.androidbackblazehelper.interfaces.ClientListener;
import com.assembliers.androidbackblazehelper.interfaces.UploadListener;
import com.assembliers.androidbackblazehelper.models.ClientModel;

import org.json.JSONObject;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import cz.msebera.android.httpclient.client.ClientProtocolException;

public class BlazeFileUploader {
    private Context context;
    private BlazeClient blazeClient;
    private String bucketId;
    private UploadListener uploadingListener;
    private boolean isAuthed = false;
    private String accountAuthorizationToken, apiUrl;


    //Upload Auth
    String uploadUrl;
    String uploadAuthorizationToken;

    public BlazeFileUploader(BlazeClient blazeClient, String bucketId) {
        this.context = blazeClient.getContext();
        this.blazeClient = blazeClient;
        this.bucketId = bucketId;

    }


    public void startUploading(Uri fileUri, String fileName) {
        InputStream iStream = null;

        try {

            iStream = context.getContentResolver().openInputStream(fileUri);
            byte[] inputData = getBytes(iStream);
            if (uploadingListener != null)
                uploadingListener.onUploadStarted();


            if (!isAuthed) {

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
                            startUploading(fileUri, fileName);
                        });

                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (uploadingListener != null)
                            uploadingListener.onUploadFailed(e);
                    }
                });
            } else {

                new UploadTask(inputData, fileName, "image").execute();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setOnUploadingListener(UploadListener uploadingListener) {
        this.uploadingListener = uploadingListener;
    }


    public static String SHAsum(byte[] convertme) {
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

    static public String myInputStreamReader(InputStream in) throws IOException {
        InputStreamReader reader = new InputStreamReader(in);
        StringBuilder sb = new StringBuilder();
        int c = reader.read();
        while (c != -1) {
            sb.append((char) c);
            c = reader.read();
        }
        reader.close();
        return sb.toString();
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


    class UploadTask extends AsyncTask<String, Void, String> {
        private String contentType;
        private byte[] fileData;
        private String fileName;
        HttpURLConnection uploadConnection;
        URLConnection urlconnection = null;

        public UploadTask(byte[] fileData, String fileName, String contentType) {
            this.fileData = fileData;
            this.fileName = fileName;
            this.contentType = contentType;
        }

        @Override
        protected String doInBackground(String... uri) {
            String responseString = "null";

            try {


                uploadConnection = getUploadConnection(uploadUrl, uploadAuthorizationToken);


                DataOutputStream uploadWriter = new DataOutputStream(uploadConnection.getOutputStream());
                if (uploadingListener != null)
                    uploadingListener.onUploadConnection(uploadConnection, uploadWriter);
                uploadWriter.write(fileData);

                responseString = myInputStreamReader(uploadConnection.getInputStream());


                uploadConnection.disconnect();

            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }


            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            if (uploadingListener != null)
                uploadingListener.onUploadFinished(result);
        }

        private HttpURLConnection getUploadConnection(String uploadUrl, String uploadAuthorizationToken) throws IOException {
            String contentType = "image"; // The content type of the file
            String sha1 = SHAsum(fileData); // SHA1 of the file you are uploading
            HttpURLConnection connection = null;
            URL url = new URL(uploadUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", uploadAuthorizationToken);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("X-Bz-File-Name", fileName);
            connection.setRequestProperty("X-Bz-Content-Sha1", sha1);
            connection.setDoOutput(true);
            return connection;
        }

    }


}
