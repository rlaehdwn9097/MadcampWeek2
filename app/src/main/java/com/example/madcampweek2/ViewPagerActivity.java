package com.example.madcampweek2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;


public class ViewPagerActivity extends AppCompatActivity {

    private Photos photosInstance;

    ViewPagerAdapter adapter;
    ViewPager viewPager;
    Button btn;
    ArrayList<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);


        Intent receivedIntent = getIntent();
        photosInstance = Photos.getInstance();
        photos = photosInstance.getPhotos();

        int position = receivedIntent.getExtras().getInt("position");

        viewPager = (ViewPager) findViewById(R.id.view);
        adapter = new ViewPagerAdapter(this, photos,position);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

    }
}