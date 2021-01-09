package com.example.madcampweek2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonPlaceHolderAPI {

    @GET("/restfulapi/contacts/")
    Call<List<Contact>> getContacts();

    @POST("/restfulapi/contacts/")
    Call<Contact> createContact(@Body Contact contact);

    @PUT("/restfulapi/contacts/{pk}/")
    Call<Contact> updateContact(@Body Contact contact, @Path("pk") int pk);

    @DELETE("/restfulapi/contacts/{pk}/")
    Call<Contact> deleteContact(@Path("pk") int pk);


    @GET("/restfulapi/photos/")
    Call<ArrayList<Photo>> getAllPhoto();
    //집어넣기
    @POST("/restfulapi/photos/")
    Call<Photo> createPhoto(@Body Photo photo);
    //삭제하기
    @DELETE("/restfulapi/photos/{id}/")
    Call<Photo> deletePhoto(@Path("id") int id);

}

