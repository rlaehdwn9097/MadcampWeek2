package com.example.madcampweek2;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetRetrofit {
    private static NetRetrofit ourInstance = new NetRetrofit();
    public static NetRetrofit getInstance() {
        return ourInstance;
    }
    private NetRetrofit() {
    }


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.249.18.234:8000/")
            .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
            .build();

    JsonPlaceHolderAPI service = retrofit.create(JsonPlaceHolderAPI.class);

    public JsonPlaceHolderAPI getService() {
        return service;
    }
}
