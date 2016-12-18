package com.provehitoIA.ferno92.volleyscorekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.provehitoIA.ferno92.volleyscorekeeper.homepage.MainActivity;

/**
 * Created by lucas on 03/12/2016.
 */

public class SplashActivity extends AppCompatActivity {
    int timeout = 250;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_2);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, timeout);

    }
}
