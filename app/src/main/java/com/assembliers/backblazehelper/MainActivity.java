/*
 * Created by Obaida Al Mostarihi  3/17/21 9:33 AM
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * Last modified:  3/17/21 9:17 AM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.app / BackBlaze Helper
 */

package com.assembliers.backblazehelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.assembliers.androidbackblazehelper.FileTypes;
import com.assembliers.androidbackblazehelper.client.BlazeClient;
import com.assembliers.androidbackblazehelper.fileuploader.ApiClient;
import com.assembliers.androidbackblazehelper.fileuploader.BlazeFileUploader;
import com.assembliers.androidbackblazehelper.fileuploader.MultiFile;
import com.assembliers.androidbackblazehelper.fileuploader.ProgressRequestBody;
import com.assembliers.androidbackblazehelper.fileuploader.UploadInterface;
import com.assembliers.androidbackblazehelper.fileuploader.UploadListener;
import com.assembliers.androidbackblazehelper.upload_auth.UploadAuth;
import com.assembliers.androidbackblazehelper.client.ClientListener;
import com.assembliers.androidbackblazehelper.client.ClientModel;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> files = new ArrayList<>();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }else{
            Intent intent ;
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<MultiFile> arrayList = new ArrayList<>();
        BlazeClient client = new BlazeClient(this,"d3919b687db0","000c9a1d6d8ccd1889f38704c8cd240ec707fc274d");
        BlazeFileUploader blazeFileUploader =
                new BlazeFileUploader(  client , "1df369d1b94b3698778d0b10");

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK && data != null) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for(int i = 0; i < count; i++){
                        MultiFile multiFile = new MultiFile();
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();

                        multiFile.init(imageUri, new File(imageUri.getPath()).getAbsolutePath() , FileTypes.IMAGE_TYPE);

                        arrayList.add(multiFile);

                        //getImageFilePath(imageUri);
                    }
                    if(arrayList.size()>0){

                        blazeFileUploader.startUploadingMultipleFiles(arrayList);



                    }

                }else{
                    blazeFileUploader.startUploading(data.getData(),"obaida");
                }

                blazeFileUploader.setOnUploadingListener(new UploadListener() {
                    @Override
                    public void onUploadStarted() {

                    }


                    @Override
                    public void onUploadProgress(int percentage, long progress, long total) {

                        Log.v("uplooooad" , percentage+"  "+progress+"   " + total);

                    }

                    @Override
                    public void onUploadFinished(String response , boolean allFilesUploaded) {
                        Log.v("uplooooad" , response+"    " +allFilesUploaded);

                    }

                    @Override
                    public void onUploadFailed(Exception e) {

                    }
                });

            }
        }


    }

    private void uploadFiles(Uri imageUri)  {



    }




}