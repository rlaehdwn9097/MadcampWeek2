package com.example.madcampweek2;

import android.content.Context;
import android.util.Log;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class Photos {

    private static Photos photosInstance = null;
    private static ArrayList<Photo> photos= new ArrayList<>();
    private PhotoAdapter adapter;


    private Photos() {
        init();
    }

    public static Photos getInstance() {
        if (photosInstance == null) {
            photosInstance = new Photos();
        }
        return photosInstance;
    }

    public static void setInstance(){
        photosInstance = null;
    }

    public synchronized void init(){
        // 여기서는 몽고디비에서 가져오는 거 구현
        // GET 방식으로 photos 다 가져와서 ArrayList 에 넣어주기.

        Log.e("1", "initFragmen2`12`12`1 들어옴");
        Call<ArrayList<Photo>> res = NetRetrofit.getInstance().getService().getAllPhoto();
        res.enqueue(new Callback<ArrayList<Photo>>() {
            @Override
            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                Log.e("2", "onResponse 들어옴");
                if (response.body() != null) {
                    Log.e("3", "데이터 있음");
                    //Toast.makeText(getContext(), "123!", Toast.LENGTH_LONG).show();
                    photos.addAll(response.body());
                } else {
                    Log.d(TAG, "Status Code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
                Log.e("Err", t.getMessage());
            }
        });
    }

    public synchronized ArrayList<Photo> deletePhoto(int id){

        for(Iterator<Photo> iterator = photos.iterator(); iterator.hasNext();){
            Photo photo = iterator.next();
            if(photo.getId() == id){
                iterator.remove();
                //break;
            }
        }

        return photos;
    }



    public synchronized ArrayList<Photo> updatePhoto(Photo photo){

        photos.add(photo);

        return photos;
    }


    public ArrayList<Photo> getPhotos() {
        return this.photos;
    }

}
