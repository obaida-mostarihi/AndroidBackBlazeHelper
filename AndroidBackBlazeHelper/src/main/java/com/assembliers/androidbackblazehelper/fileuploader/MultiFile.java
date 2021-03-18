package com.assembliers.androidbackblazehelper.fileuploader;

import android.net.Uri;

public class MultiFile {

    public Uri fileUri;
    public String fileName;
    public String contentType;
    public MultiFile() {
    }

    public MultiFile(Uri fileUri, String fileName, String contentType) {
        this.fileUri = fileUri;
        this.fileName = fileName;
        this.contentType = contentType;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    public void init(Uri fileUri, String fileName, String contentType) {
        this.fileUri = fileUri;
        this.fileName = fileName;
        this.contentType = contentType;
    }

}
