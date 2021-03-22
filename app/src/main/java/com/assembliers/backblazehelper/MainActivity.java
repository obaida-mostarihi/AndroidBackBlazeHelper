/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/18/21 2:16 PM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.app / BackBlaze Helper
 *
 */

package com.assembliers.backblazehelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;

import com.assembliers.androidbackblazehelper.FileTypes;
import com.assembliers.androidbackblazehelper.client.BlazeClient;
import com.assembliers.androidbackblazehelper.filedownloader.BlazeFileDownloader;
import com.assembliers.androidbackblazehelper.filedownloader.DownloadListener;
import com.assembliers.androidbackblazehelper.filedownloader.MultiDownload;
import com.assembliers.androidbackblazehelper.fileuploader.BlazeFileUploader;
import com.assembliers.androidbackblazehelper.fileuploader.MultiFile;
import com.assembliers.androidbackblazehelper.fileuploader.UploadListener;
import com.assembliers.androidbackblazehelper.upload_models.UploadResponse;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> files = new ArrayList<>();
    BlazeClient client;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final String BLAZE_APP_KEY_ID = "lkhjkln";
    public static final String BLAZE_APP_KEY = "kjhkj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new BlazeClient(this , BLAZE_APP_KEY_ID , BLAZE_APP_KEY);

        Intent intent;
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
      startActivityForResult(Intent.createChooser(intent,""), 10);





    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<MultiFile> arrayList = new ArrayList<>();


        if (requestCode == 10) {
            BlazeFileUploader blazeFileUploader =
                    new BlazeFileUploader(client, "");
            if (resultCode == Activity.RESULT_OK && data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for (int i = 0; i < count; i++) {
                        MultiFile multiFile = new MultiFile();
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        InputStream iStream = null;

                        try {
                            iStream = getContentResolver().openInputStream(imageUri);
                            byte[] inputData = getBytes(iStream);
                            multiFile.init(imageUri, new File(imageUri.getPath()).getAbsolutePath(), FileTypes.IMAGE_TYPE);

                            arrayList.add(multiFile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                        //getImageFilePath(imageUri);
                    }
                    if (arrayList.size() > 0) {

                        blazeFileUploader.startUploadingMultipleFiles(arrayList);


                    }

                } else {
                    InputStream iStream = null;
                    try {
                        iStream = getContentResolver().openInputStream(data.getData());
                        byte[] inputData = getBytes(iStream);
                        blazeFileUploader.startUploading(data.getData(), "obaidauri.png");


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                blazeFileUploader.setOnUploadingListener(new UploadListener() {
                    @Override
                    public void onUploadStarted() {

                    }


                    @Override
                    public void onUploadProgress(int percentage, long progress, long total) {

                        Log.v("uplooooad", percentage + "  " + progress + "   " + total);

                    }

                    @Override
                    public void onUploadFinished(UploadResponse response, boolean allFilesUploaded) {

                    }

                    @Override
                    public void onUploadFailed(Exception e) {

                    }
                });

            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 4:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    BlazeClient client = new BlazeClient(this, "", "KO6fwwhA");

                    BlazeFileDownloader blazeFileDownloader = new BlazeFileDownloader(client);
                    blazeFileDownloader.startDownloading("hkjhkjloijhk", "Beavis-1.png");
                } else {


                }
                break;
        }
    }


    public void gallery(View view) {
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
}