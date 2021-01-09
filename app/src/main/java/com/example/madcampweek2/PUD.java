package com.example.madcampweek2;

import android.util.Log;
import android.widget.ArrayAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class PUD {

    ContactList contactList = ContactList.getInstance();

    public PUD() {}

    public void postContact(Contact contact, ArrayAdapter<String> listViewAdapter) {
        JsonPlaceHolderAPI mAPI = NetRetrofit.getInstance().getService();

        System.out.println(contact);

        Call<Contact> createCall = mAPI.createContact(contact);
        createCall.enqueue(new Callback<Contact>(){

            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                if (response.isSuccessful()) {
                    Contact obj = response.body();
                    contactList.add(contact);

                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Status Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.d(TAG, "Fail msg: " + t.getMessage());
            }
        });
    }


    public void updateContact(int id, Contact contact, ArrayAdapter<String> listViewAdapter) {

        JsonPlaceHolderAPI mAPI = NetRetrofit.getInstance().getService();

        System.out.println(contact);

        Call<Contact> updateCall = mAPI.updateContact(contact, id);
        updateCall.enqueue(new Callback<Contact>(){

            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                if (response.isSuccessful()) {
                    ContactList contactList = ContactList.getInstance();
                    contactList.update(id, contact);
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Status Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.d(TAG, "Fail msg: " + t.getMessage());
            }
        });
    }


    public void deleteContact(int id, ArrayAdapter<String> listViewAdapter) {

        JsonPlaceHolderAPI mAPI = NetRetrofit.getInstance().getService();

        Call<Contact> deleteCall = mAPI.deleteContact(id);
        deleteCall.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                if (response.isSuccessful()) {
                    ContactList contactList = ContactList.getInstance();
                    contactList.delete(id);
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Status Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.d(TAG, "Fail msg: " + t.getMessage());
            }
        });
    }
}
