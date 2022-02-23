package com.rubick.falloutrpgapp.View;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rubick.falloutrpgapp.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView loading;
    private View screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        final MediaPlayer startApp = MediaPlayer.create(this, R.raw.start_app);
        startApp.start();
        progressBar = findViewById(R.id.progressBar);
        loading = findViewById(R.id.textView);
        screen = findViewById(R.id.screen);
        FillProgressBar(randomNumber());
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.blink_anim);
        loading.startAnimation(animation);
    }

    private int randomNumber(){
        Random rand = new Random();
        int max = 30;
        int min = 10;
        return rand.nextInt((max - min) + 1) + min;
    }

    private void FillProgressBar(final int progress) {
        progressBar.setProgress(progress);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(progress >= 100){
                    Intent status = new Intent(getApplicationContext(), Status.class);
                    startActivity(status);
                    overridePendingTransition(R.anim.fade_in_screen, R.anim.fade_out_screen);
                }else{
                    FillProgressBar(progress + randomNumber());
                }
            }
        });
        thread.start();
    }
}