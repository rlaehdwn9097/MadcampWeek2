package com.example.madcampweek2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class PhotoAdapter extends BaseAdapter {

    private Context context;
    public ImageView imageView;
    Photo photo;
    ArrayList<Photo> photos;

    @Override
    public int getCount() {
        //return imageArray.length;
        return photos.size();
    }

    public PhotoAdapter(Context context, ArrayList<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }


    @Override
    public Object getItem(int position) {

        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.d("image id", String.valueOf(photos.get(position).getId()));
        context = parent.getContext();
        if (null != convertView)
            imageView = (ImageView) convertView;
        else {

            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            photo = photos.get(position);

            //bitmap decode 해서 imageView 에 넣어줘야한다.
            //decode base64 string to image
            final byte[] decodeImageBytes = Base64.decode(photo.getImage(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(decodeImageBytes, 0, decodeImageBytes.length);
            imageView.setImageBitmap(decodedImage);


        }
        return imageView;
    }



}
