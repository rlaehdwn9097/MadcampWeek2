package com.example.madcampweek2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonPlaceHolderAPI {

    @GET("contacts/")
    Call<List<Contact>> getContacts();

    @POST("contacts/")
    Call<Contact> createContact(@Body Contact contact);

    @PUT("contacts/{pk}/")
    Call<Contact> updateContact(@Body Contact contact, @Path("pk") int pk);

    @DELETE("contacts/{pk}/")
    Call<Contact> deleteContact(@Path("pk") int pk);
}

