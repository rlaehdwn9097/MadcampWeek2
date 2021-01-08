package com.example.madcampweek2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

public class LoginCallback implements FacebookCallback<LoginResult>{
    /*
    Context context;

    public LoginCallback(Context context) {
        this.context = context;
    }

     */

    @Override
    public void onSuccess(LoginResult loginResult) {
        requestMe(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

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
    }
}
