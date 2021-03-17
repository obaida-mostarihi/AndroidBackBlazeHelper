/*
 * Created by Obaida Al Mostarihi  3/17/21 10:17 AM
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * Last modified:  3/17/21 10:17 AM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 */

package com.assembliers.androidbackblazehelper.interfaces;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;

public interface UploadListener {
    void onUploadStarted();

    void onUploadConnection(HttpURLConnection uploadConnection, DataOutputStream uploadWriter);

    void onUploadFinished(String response);

    void onUploadFailed(Exception e);
}
