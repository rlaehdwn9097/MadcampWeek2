package com.example.madcampweek2;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderAPI {

    @GET("contacts/")
    Call<List<PostContact>> getContacts();

    @POST("contacts/")
    Call<PostContact> createPost(@Body PostContact contact);
}

