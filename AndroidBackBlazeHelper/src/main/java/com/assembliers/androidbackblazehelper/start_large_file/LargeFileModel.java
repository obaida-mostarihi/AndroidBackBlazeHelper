package com.assembliers.androidbackblazehelper.start_large_file;

import org.json.JSONException;
import org.json.JSONObject;

public class LargeFileModel {

    public String accountId;
    public String action;
    public String bucketId;
    public long contentLength;
    public String contentSha1;
    public String contentType;
    public String fileId;
    public String fileName;
    public long uploadTimestamp;


    public LargeFileModel(JSONObject response) throws JSONException {
        accountId = response.getString("accountId");
        action = response.getString("action");
        bucketId = response.getString("bucketId");
        contentLength = response.getLong("contentLength");
        contentSha1 = response.getString("contentSha1");
        contentType = response.getString("contentType");
        fileId = response.getString("fileId");
        fileName = response.getString("fileName");
        uploadTimestamp = response.getLong("uploadTimestamp");

    }

    public String getAccountId() {
        return accountId;
    }

    public String getAction() {
        return action;
    }

    public String getBucketId() {
        return bucketId;
    }

    public long getContentLength() {
        return contentLength;
    }

    public String getContentSha1() {
        return contentSha1;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public long getUploadTimestamp() {
        return uploadTimestamp;
    }
}
