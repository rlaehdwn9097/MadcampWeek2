package com.example.madcampweek2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class ContactActivity extends AppCompatActivity {
    private TextView textView;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        textView = findViewById(R.id.text_view);
        button = findViewById(R.id.button);

        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://c6a40f56e7e8.ngrok.io/")
                //.baseUrl("https://c6a40f56e7e8.ngrok.io/")
                .baseUrl("https://e602491981ca.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);

        Call<List<PostContact>> call = jsonPlaceHolderAPI.getContacts();
        call.enqueue(new Callback<List<PostContact>>() {
            @Override
            public void onResponse(Call<List<PostContact>> call, Response<List<PostContact>> response) {
                if (!response.isSuccessful()) {
                    textView.setText("Code: " + response.code());
                    return;
                }
                List<PostContact> posts = response.body();

                for (PostContact post: posts) {
                    String content = "";
                    content += post.toString();
                    content += "\n";

                    textView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<PostContact>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                startActivity(intent);
            }
        });

    }
}