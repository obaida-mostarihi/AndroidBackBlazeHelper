/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/19/21 8:23 AM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.filedownloader;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import com.assembliers.androidbackblazehelper.client.BlazeClient;
import com.assembliers.androidbackblazehelper.client.ClientListener;
import com.assembliers.androidbackblazehelper.client.ClientModel;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;


public class BlazeFileDownloader {
    private String bucketName = "";
    private String fileName = "";
    private int totalFileSize;
    private String fileExtension = "";
    BlazeFileDownloader.downloadFileTask downloadFileTask;

    private DownloadListener downloadListener;

    private BlazeClient blazeClient;


    private String authorizationToken = "";
    private String downloadUrl = "";


    private boolean isAuth = false;


    //Multiple download
    private boolean isMultiDownload = false;
    private ArrayList<MultiDownload> files;
    private MultiDownload multiDownload;
    private ArrayList<MultiDownload> downloadedFiles;


    public BlazeFileDownloader(BlazeClient blazeClient) {
        this.blazeClient = blazeClient;

        downloadedFiles  = new ArrayList<>();
    }



    public void startDownloadingMultipleFiles(String bucketName,ArrayList<MultiDownload> files) {

        isMultiDownload = true;

        this.files = files;
        this.bucketName = bucketName;
        checkIfDownloadAuthed();


    }



    private void downloadMultipleFiles(){

        MultiDownload multiDownload = files.get(downloadedFiles.size());


        fileName = multiDownload.getFileName();

        fileExtension = multiDownload.getFileExtension();

        initDownload();





    }

    /**
     *                   <p>
     *                   check if the user have been authed if not
     *                   get a new key and the download url
     *                   then start downloading
     */
    private void checkIfDownloadAuthed() {

        if(!isAuth){
            blazeClient.setClientListener(new ClientListener() {
                @Override
                public void onConnectionStarted() {

                }

                @Override
                public void onRetrievingData(ClientModel data, JSONObject response) {


                    authorizationToken = data.getAuthorizationToken();
                    downloadUrl = data.getDownloadUrl();

                    checkIfDownloadAuthed();
                    isAuth = true;


                }

                @Override
                public void onFailure(Exception e) {
                    if (downloadListener != null)
                        downloadListener.onUploadFailed(e);
                }
            });

        }else{

            if(!isMultiDownload)
                initDownload();
            else
                downloadMultipleFiles();


        }

    }


    /**
     * @param bucketName the bucket of the location of the file name
     * @param fileName   the name of the file in the storage
     */
    public void startDownloading(String bucketName, String fileName) {
        isMultiDownload = false;


        if (downloadListener != null)
            downloadListener.onDownloadStart();


        this.bucketName = bucketName;
        this.fileName = fileName;


        checkIfDownloadAuthed();


    }


    /**
     * @param fileExtension here should be the file extension e.g. jpg , png  , zip..etc
     */
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }


    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }


    /**
     * Get the file response from the storage and start an AsyncTask
     */
    private void initDownload() {


        DownloadInterface downloadService = createService(DownloadInterface.class, downloadUrl + "/");


        Call<ResponseBody> request = downloadService.downloadFile("file/" + bucketName + "/" + fileName, authorizationToken);
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {


                    downloadFileTask = new downloadFileTask();
                    downloadFileTask.execute(response.body());

                } else {
                    Log.d(TAG, "Connection failed " + response.errorBody());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (downloadListener != null)
                    downloadListener.onUploadFailed((Exception) t);
            }
        });


    }



    //Create a retrofit service
    private <T> T createService(Class<T> serviceClass, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(new OkHttpClient.Builder().build())
                .build();
        return retrofit.create(serviceClass);
    }


    /**
     * This AsyncTask will convert the response body to a file and save it with the same name
     * @apiNote Note: please set a file extension if the name of the file has no extension using the method setFileExtension
     */
    private class downloadFileTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {
        long fileSize;
        int progress = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(ResponseBody... urls) {
            //Copy you logic to calculate progress and call

            saveToDisk(urls[0]);
            return null;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {


            if (progress[0].first == 100) {

                if(isMultiDownload) {
                    downloadedFiles.add(multiDownload);
                    if (downloadedFiles.size() == files.size()) {

                    }else{
                        downloadMultipleFiles();

                    }
                }
                if (downloadListener != null)
                    downloadListener.onDownloadFinish();
            }

            if (progress[0].second > 0) {
                int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);

                if (downloadListener != null)
                    downloadListener.onDownloadProgress(currentProgress, (long) this.progress, (long) fileSize);

            }

            if (progress[0].first == -1) {
                Log.v("Downloading", "Progress failed");

                if (downloadListener != null)
                    downloadListener.onUploadFailed(new Exception("Progress failed"));
            }

        }

        public void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }

        @Override
        protected void onPostExecute(String result) {

        }

        private void saveToDisk(ResponseBody body) {
            try {

                File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + fileExtension);

                InputStream inputStream = null;
                OutputStream outputStream = null;

                try {

                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(destinationFile);
                    byte data[] = new byte[4096];
                    int count;
                    fileSize = body.contentLength();
                    Log.d(TAG, "File Size=" + fileSize);
                    while ((count = inputStream.read(data)) != -1) {
                        outputStream.write(data, 0, count);
                        progress += count;
                        Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                        downloadFileTask.doProgress(pairs);


                    }

                    outputStream.flush();

                    Log.d(TAG, destinationFile.getParent());
                    Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                    downloadFileTask.doProgress(pairs);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                    downloadFileTask.doProgress(pairs);
                    Log.d(TAG, "Failed to save the file!");
                    if (downloadListener != null)
                        downloadListener.onUploadFailed(new Exception("Failed to save the file!"));
                    return;
                } finally {
                    if (inputStream != null) inputStream.close();
                    if (outputStream != null) outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed to save the file!");
                if (downloadListener != null)
                    downloadListener.onUploadFailed(new Exception("Failed to save the file!"));
                return;
            }
        }
    }


}//Class
