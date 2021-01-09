package com.example.madcampweek2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public Button button;
    public static final int sub = 1001;
    private LoginButton loginButton;
    private LoginCallback loginCallback;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();
        loginCallback = new LoginCallback(getApplicationContext());

        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, loginCallback);

        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                    //intent.putExtra("uid", currentUser.getUid());
                    startActivity(intent);
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                    //intent.putExtra("uid", currentUser.getUid());
                    startActivity(intent);
                }
            }
        };

        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            System.out.println(uid);
        } else {
            System.out.println("current user is stillnull");
        }
/*
        if (resultCode == -1) {
            Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
            //intent.putExtra("uid", currentUser.getUid());
            startActivity(intent);
        }

 */
    }

    @Override
    protected void onStop() {
        super.onStop();

        FacebookSdk.fullyInitialize();
        LoginManager.getInstance().logOut();
    }
}