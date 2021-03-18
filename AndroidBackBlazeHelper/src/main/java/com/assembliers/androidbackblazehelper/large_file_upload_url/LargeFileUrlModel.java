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
