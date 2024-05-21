package com.example.ontherun;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OcrApi {
    @FormUrlEncoded
    @POST("parse/image/")
    Call<Page> getTextFromImage(
            @Field("apikey") String apikey,
            @Field("base64Image") String base64Image, // or url
            @Field("language") String language,
            @Field("isOverlayRequired") Boolean isOverlayRequired,
            @Field("filetype") String filetype
    );
}
