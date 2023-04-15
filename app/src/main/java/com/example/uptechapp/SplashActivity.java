package com.example.uptechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "ActivitySplash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            boolean logged = sharedPref.getBoolean(getString(R.string.account_logging), false);

            Log.i(TAG, "BOOLEAN " + sharedPref.getBoolean(getString(R.string.account_logging), false));

            Intent intent;

            if (logged) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            SplashActivity.this.finish();

        }).start();
    }
}