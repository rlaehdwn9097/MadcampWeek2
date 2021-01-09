package com.example.madcampweek2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

public class LoginCallback implements FacebookCallback<LoginResult>{
    Context context;
    FirebaseAuth auth;

    public LoginCallback (Context context) {
        context = context;
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Log.d(TAG,"facebook: onSuccess: " + loginResult);
        System.out.println("hey, i'm calling handleFacebookToken method here.");
        handleFacebookToken(loginResult.getAccessToken());
        //requestMe(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
        Log.d(TAG, "facebook: onCancel" );
    }

    @Override
    public void onError(FacebookException error) {
        Log.d(TAG,"facebook: onError", error);
    }


    public void handleFacebookToken(AccessToken accessToken) {

        System.out.println("handling facebooktoken");
        auth = FirebaseAuth.getInstance();
        System.out.println(accessToken.getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("nyes way!!!!!!!!!!!!!!!!!!!!!!!!");
                            FirebaseUser myUserObj = auth.getCurrentUser();
                            String uid = myUserObj.getUid();

                            //updateUI(uid);
                            //Toast.makeText(context, uid, Toast.LENGTH_SHORT).show();

                            System.out.println(uid);
                        } else {

                            System.out.println("no way!!!!!!!!!!!!!!!!!!!!!!!!");
                            Toast.makeText(context, "Could not register to firebase", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI(String uid) {
        //Intent intent = new Intent(context, ServiceActivity.class);
        //intent.putExtra("uid", uid);
        //startActivity(intent);
    }

    /*
    public void requestMe(AccessToken token) {

        GraphRequest graphRequest = GraphRequest.newMeRequest(token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("result", object.toString());
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "handleFacebookAccesToken: "+ token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    }
                })

    }

    */
}
