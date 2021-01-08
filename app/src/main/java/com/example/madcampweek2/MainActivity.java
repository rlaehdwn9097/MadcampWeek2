package com.example.madcampweek2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public Button button;
    public static final int sub = 1001;
    private LoginButton loginButton;
    private LoginCallback loginCallback;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();
        loginCallback = new LoginCallback();

        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, loginCallback);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        //로그인이 안되도 바로 Service Activity로 넘어감.
        //REQUEST 온 것 SUCCESS 인지 아닌지 확인해서 Intent 넘겨줘야할 듯
        Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
        startActivity(intent);
    }

}