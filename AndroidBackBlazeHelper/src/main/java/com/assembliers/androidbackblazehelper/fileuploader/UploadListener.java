/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/18/21 2:16 PM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.fileuploader;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
public interface UploadListener {
    void onUploadStarted();

    void onUploadProgress(int percentage, long progress, long total);
    void onUploadFinished(String response,boolean allFilesUploaded);

    void onUploadFailed(Exception e);
}
