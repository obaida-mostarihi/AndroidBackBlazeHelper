/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/18/21 2:16 PM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.large_file_upload_url;

import com.assembliers.androidbackblazehelper.start_large_file.LargeFileModel;

import java.net.MalformedURLException;

public interface LargeFileUploadUrlInterface {
    void onRetrieveData(LargeFileUrlModel model) throws MalformedURLException;

}
