/*
 * Copyright (c) 2021 . All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * Last modified:  3/18/21 2:16 PM
 * Contact: Email : obaidaalmostarihi@gmail.com
 * Package: BackBlaze_Helper.AndroidBackBlazeHelper / BackBlaze Helper
 *
 */

package com.assembliers.androidbackblazehelper.fileuploader;

import com.assembliers.androidbackblazehelper.upload_models.UploadResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;

import retrofit2.http.POST;
import retrofit2.http.Url;

public interface UploadInterface {
    @POST
    Call<UploadResponse> uploadFile(@Url String url, @Body RequestBody file, @Header("Authorization") String authorization
            , @Header("X-Bz-Content-Sha1") String sha1, @Header("X-Bz-File-Name") String fileName );

}
