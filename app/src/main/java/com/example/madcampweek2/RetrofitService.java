package com.example.madcampweek2;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface
RetrofitService {
    //다 가져오기
    @GET("/restfulapi/photos/")
    Call<ArrayList<Photo>> getAllPhoto();
    //집어넣기
    @POST("/restfulapi/photos/")
    Call<Photo> createPhoto(@Body Photo photo);
    //삭제하기
    @DELETE("/restfulapi/photos/{id}/")
    Call<Photo> deletePhoto(@Path("id") int id);

    /*@POST("User/DoctorLogin")
    Call<LoginResult> getStringScalar(@Body LoginData body);*/
}