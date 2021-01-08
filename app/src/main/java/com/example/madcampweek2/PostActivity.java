package com.example.madcampweek2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText numberEditText;
    private EditText emailEditText;
    private Button button;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        nameEditText = findViewById(R.id.name_edit_text);
        numberEditText = findViewById(R.id.number_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        button = findViewById(R.id.button);
        errorText = findViewById(R.id.error);


        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://c6a40f56e7e8.ngrok.io/")
                .baseUrl("https://c6a40f56e7e8.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);

                String name = nameEditText.getText().toString();
                String number = numberEditText.getText().toString();
                String email = emailEditText.getText().toString();

                /*
                JSONObject obj = new JSONObject();
                try {
                    obj.put("name", name);
                    obj.put("number", number);
                    obj.put("email", email);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                 */
                PostContact obj  = new PostContact();
                obj.setName(name);
                obj.setNumber(number);
                obj.setEmail(email);

                Call<PostContact> call = jsonPlaceHolderAPI.createPost(obj);

                call.enqueue(new Callback<PostContact>() {
                    @Override
                    public void onResponse(Call<PostContact> call, Response<PostContact> response) {
                        if (!response.isSuccessful()) {
                            errorText.setText("Code: " + response.code());
                            return;
                        }
                        errorText.setText("successfully posted");
                        return;
                    }

                    @Override
                    public void onFailure(Call<PostContact> call, Throwable t) {
                        errorText.setText(t.getMessage());
                    }
                });

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
    }
}