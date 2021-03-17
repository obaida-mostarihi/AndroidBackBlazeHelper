/*
 * Created by Obaida Al Mostarihi  3/17/21 11:04 AM
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * Last modified:  3/17/21 11:04 AM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 */

package com.assembliers.androidbackblazehelper.models;

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
