package com.example.madcampweek2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter{

    private ArrayList<Photo> photos;
    private LayoutInflater inflater;
    private Photo photo;
    private Context context;
    private int preposition;

    public ViewPagerAdapter(Context context,  ArrayList<Photo> photos, int preposition){
        this.context = context;
        this.photos = photos;
        this.preposition = preposition;
    }


    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout)object);
    }

    public Object instantiateItem(ViewGroup container, int position){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider, container,false);
        photo = photos.get(position);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);

        //여기에도  bitmap decode 하고 이미지 set
        final byte[] decodeImageBytes = Base64.decode(photo.getImage(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(decodeImageBytes, 0, decodeImageBytes.length);
        imageView.setImageBitmap(decodedImage);

        container.addView(v);
        return v;
    }

    public void destroyItem(ViewGroup container, int position, Object object){
        container.invalidate();
    }
}
