/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/18/21 2:16 PM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.upload_auth;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadAuthModel {

    public String bucketId;
    public String uploadUrl;
    public String authorizationToken;

    public UploadAuthModel(JSONObject response) throws JSONException {
        bucketId = response.getString("bucketId");
        uploadUrl = response.getString("uploadUrl");
        authorizationToken = response.getString("authorizationToken");

    }

    public String getBucketId() {
        return bucketId;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }
}
