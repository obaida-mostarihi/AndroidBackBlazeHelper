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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.assembliers.androidbackblazehelper.client.BlazeClient;
import com.assembliers.androidbackblazehelper.fileuploader.BlazeFileUploader;
import com.assembliers.androidbackblazehelper.interfaces.ClientListener;
import com.assembliers.androidbackblazehelper.interfaces.UploadListener;
import com.assembliers.androidbackblazehelper.models.ClientModel;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent;
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
startActivityForResult(Intent.createChooser(intent,"lol"), 2);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {

            if (resultCode == Activity.RESULT_OK && data != null) {
             if (data.getData() != null) {
                 BlazeClient client = new BlazeClient(this,"d3919b687db0","000c9a1d6d8ccd1889f38704c8cd240ec707fc274d");

                 BlazeFileUploader blazeFileUploader = new BlazeFileUploader(  client , "1df369d1b94b3698778d0b10");
                 blazeFileUploader.startUploading(data.getData(), "teejkhkjheestttt");

                 blazeFileUploader.setOnUploadingListener(new UploadListener() {
                     @Override
                     public void onUploadStarted() {
                         Log.v("loool" , "start");
                     }

                     @Override
                     public void onUploadConnection(HttpURLConnection uploadConnection, DataOutputStream uploadWriter) {
                         Log.v("loool" , "onUploadConnection");

                     }

                     @Override
                     public void onUploadFinished(String response) {
                         Log.v("loool" , response);

                     }

                     @Override
                     public void onUploadFailed(Exception e) {

                     }
                 });

                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
            }
        }


    }
}