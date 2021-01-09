package com.example.madcampweek2;

import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class Fragment2 extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private boolean isOpen = false;
    private FloatingActionButton fab;
    private FloatingActionButton itemFab;
    private GridView gridView;
    private ArrayList<Photo> photos = new ArrayList<>();
    private Photos photosInstance;
    public PhotoAdapter adapter;


    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_2, container, false);
        //버튼 기본 설정
        fab = (FloatingActionButton) rootView.findViewById(R.id.mainFab);
        itemFab = (FloatingActionButton) rootView.findViewById(R.id.insertfab);
        fab.setOnClickListener(this);
        itemFab.setOnClickListener(this);


        //그리드뷰 기본 설정
        gridView = (GridView) rootView.findViewById(R.id.gridView);

        //init
        initFragment2();
        adapter.notifyDataSetChanged();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                deletePhoto(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.e(TAG, String.valueOf(photos.get(position).getId()));
                Toast.makeText(getContext(), String.format("position : %d\nid : %d\nlength : %s", position, photos.get(position).getId(), String.valueOf(photos.size())), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), ViewPagerActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("length", photos.size());
                Log.d("viewpager", "viewpager in");
                getContext().startActivity(intent);
                adapter.notifyDataSetChanged();

            }
        });


        return rootView;
    }

    public void initFragment2() {
        photosInstance = Photos.getInstance();
        photos = photosInstance.getPhotos();
        adapter = new PhotoAdapter(getContext(), photos);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void deletePhoto(int position) {
        int id = photos.get(position).getId();
        Call<Photo> res = NetRetrofit.getInstance().getService().deletePhoto(id);
        res.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()){
                    photos = photosInstance.deletePhoto(id);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Log.e("Err", t.getMessage());
            }
        });

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainFab:
                if(!isOpen){
                    //ObjectAnimator.ofFloat(itemFab, "translationY", -400f).start();
                    ObjectAnimator.ofFloat(itemFab, "translationY", -200f).start();
                    isOpen = true;
                }
                else{
                    //ObjectAnimator.ofFloat(itemFab, "translationY", 0f).start();
                    ObjectAnimator.ofFloat(itemFab, "translationY", 0f).start();
                    isOpen = false;
                }
                break;
                //버튼 하나 더 만들어서 카메라에서 찍은 거 업로드 하는 것

            case R.id.insertfab:
                Toast.makeText(getContext(),"메뉴 버튼 클릭", Toast.LENGTH_SHORT).show();
                //앨범으로 들어가서 원하는 사진 DB에 업로드!
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(MediaStore.Images.Media.CONTENT_TYPE);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                try {
                    i.putExtra("data", true);
                    startActivityForResult(Intent.createChooser(i, "Select Picture"), 0);

                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
                break;

        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("image", "image");
        super.onActivityResult(requestCode, resultCode, data);
        //super method removed
        if(requestCode == 0)
        {

            try{
                InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                in.close();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bmpCompressed.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] image = outputStream.toByteArray();
                String imageString = Base64.encodeToString(image, Base64.DEFAULT);

                Photo photo = new Photo(imageString, 1);

                //server로 보내기
                if (!imageString.isEmpty()) {
                    Call<Photo> res = NetRetrofit.getInstance().getService().createPhoto(photo);
                    res.enqueue(new Callback<Photo>() {
                        @Override
                        public void onResponse(Call<Photo> call, Response<Photo> response) {
                            Log.d("Retrofit", response.toString());
                            if (response.body() != null) {
                                Log.d(TAG, "onResponse: PostGood");
                                photos =photosInstance.updatePhoto(photo);
                                adapter.notifyDataSetChanged();

                            }
                        }

                        @Override
                        public void onFailure(Call<Photo> call, Throwable t) {
                            Log.e("Err", t.getMessage());
                        }
                    });
                }
            }catch(Exception e) { }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }
}