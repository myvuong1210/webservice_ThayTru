package com.example.adminqlbh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    Animation slideUpAnim, slideDownAnim;
    ImageView imageLogo;
    TextView slogan;
    //Hold 4s
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hide title_bar
        setContentView(R.layout.layout_welcome_screen);

        //Animations
        slideUpAnim = AnimationUtils.loadAnimation(this, R.anim.silde_up_anim);
        slideDownAnim = AnimationUtils.loadAnimation(this, R.anim.slide_down_anim);

        //Hooks
        imageLogo = findViewById(R.id.imageLogo);
        slogan = findViewById(R.id.tv_Slogan);
        imageLogo.setAnimation(slideDownAnim);
        slogan.setAnimation(slideUpAnim);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}