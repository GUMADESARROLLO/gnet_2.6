package com.gnet.activity;



import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.gnet.Config;
import com.guma.desarrollo.gnet.R;


public class ActivitySplash extends AppCompatActivity {
    MyApplication myApplication;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        new CountDownTimer(Config.SPLASH_TIME, 1000) {

            @Override
            public void onFinish() {
                myApplication = MyApplication.getInstance();

               if (myApplication.getIsLogin()){
                   startActivity(new Intent(getBaseContext(), DashboardMenu.class));
                }else{
                   startActivity(new Intent(getBaseContext(), Login.class));
                }

                finish();
                progressBar.setVisibility(View.GONE);




            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();

    }
}
