package com.example.securenoteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import java.security.MessageDigest;


public class SplashActivity extends AppCompatActivity {

    String password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Load Password
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() { //igual se puede cambiar de postdelayed a post a secas
            @Override
            public void run() {
                if (password.equals("")){
                    //if there is no password
                    Intent intent = new Intent (getApplicationContext(), CreatePasswordActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    //if there is a password already
                    Intent intent = new Intent (getApplicationContext(), EnterPasswordActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 10);
    }
}
