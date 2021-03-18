/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/18/21 2:16 PM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.large_file_upload_url;

import org.json.JSONException;
import org.json.JSONObject;

public class LargeFileUrlModel {

    public String authorizationToken;
    public String fileId;
    public String uploadUrl;

    public LargeFileUrlModel(JSONObject response) throws JSONException {
        authorizationToken = response.getString("authorizationToken");
        fileId = response.getString("fileId");
        uploadUrl = response.getString("uploadUrl");
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public String getFileId() {
        return fileId;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }
}
