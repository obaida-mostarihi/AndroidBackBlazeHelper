/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/18/21 2:16 PM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.client;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientModel {

    public ClientModel(){}

    public ClientModel(long absoluteMinimumPartSize, String accountId, String apiUrl, String authorizationToken, String downloadUrl, long recommendedPartSize) {
        this.absoluteMinimumPartSize = absoluteMinimumPartSize;
        this.accountId = accountId;
        this.apiUrl = apiUrl;
        this.authorizationToken = authorizationToken;
        this.downloadUrl = downloadUrl;
        this.recommendedPartSize = recommendedPartSize;
    }

    public long absoluteMinimumPartSize;
    public String accountId;
    public String apiUrl;
    public String authorizationToken;
    public String downloadUrl;
    public long recommendedPartSize;

    public ClientModel(JSONObject response) throws JSONException {
       absoluteMinimumPartSize = response.getLong("absoluteMinimumPartSize");
        accountId = response.getString("accountId");
        apiUrl = response.getString("apiUrl");
        authorizationToken = response.getString("authorizationToken");
        downloadUrl = response.getString("downloadUrl");
        recommendedPartSize = response.getLong("recommendedPartSize");
    }

    public long getAbsoluteMinimumPartSize() {
        return absoluteMinimumPartSize;
    }


    public String getAccountId() {
        return accountId;
    }



    public String getApiUrl() {
        return apiUrl;
    }


    public String getAuthorizationToken() {
        return authorizationToken;
    }


    public String getDownloadUrl() {
        return downloadUrl;
    }



    public long getRecommendedPartSize() {
        return recommendedPartSize;
    }


}

