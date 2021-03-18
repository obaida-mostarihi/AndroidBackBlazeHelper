package com.assembliers.androidbackblazehelper.fileuploader;

import com.google.gson.JsonElement;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface UploadInterface {
    @POST
    Call<JsonElement> uploadFile(@Url String url, @Body RequestBody file, @Header("Authorization") String authorization
            , @Header("X-Bz-Content-Sha1") String sha1, @Header("X-Bz-File-Name") String fileName );

}
