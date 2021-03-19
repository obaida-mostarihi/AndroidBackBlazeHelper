/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/19/21 10:48 AM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.filedownloader;

public interface DownloadListener {

    void onDownloadStart();
    void onDownloadProgress(int percentage, long progress, long total);
    void onDownloadFinish();

}
