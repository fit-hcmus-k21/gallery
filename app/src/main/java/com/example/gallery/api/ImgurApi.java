package com.example.gallery.api;

import com.example.gallery.api.json_holder.Imgur;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImgurApi {
    public static final String DOMAIN = "https://api.imgur.com/3/";

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    ImgurApi imgurApi = new Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ImgurApi.class);

    @Multipart
    @POST("image") // Đường dẫn API upload của Imgur
    Call<Imgur> uploadImage(
            @Part MultipartBody.Part image,
            @Header("Authorization") String authorization
    );
}
