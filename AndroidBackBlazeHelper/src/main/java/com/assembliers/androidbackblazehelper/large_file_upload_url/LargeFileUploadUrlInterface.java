package com.assembliers.androidbackblazehelper.large_file_upload_url;

import com.assembliers.androidbackblazehelper.start_large_file.LargeFileModel;

import java.net.MalformedURLException;

public interface LargeFileUploadUrlInterface {
    void onRetrieveData(LargeFileUrlModel model) throws MalformedURLException;

}
