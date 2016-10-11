package com.jvmed.hashapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen_Activity extends AppCompatActivity {

    private static final int SPLASH_DURATION_MS = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Animation that moves the logo from the top to the center
        Animation animation = AnimationUtils.loadAnimation(SplashScreen_Activity.this, R.anim.splash_logo_move);

        ImageView imgLogo = (ImageView) findViewById(R.id.imgLogo);
        if (imgLogo != null) {
            imgLogo.setAnimation(animation);
        }

        /**
         * The object waits X milliseconds before starting the other Activity
         * @see Handler
         */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen_Activity.this, Home_Activity.class));
                finish();
            }
        }, SPLASH_DURATION_MS);
    }
}
