package com.example.a2048;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashTFE extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tfe_splash); //activity_main
        ImageView zero = findViewById(R.id.imageView_num0);
        ImageView dos = findViewById(R.id.imageView_num2);
        ImageView quatre = findViewById(R.id.imageView_num4);
        ImageView vuit = findViewById(R.id.imageView_num8);
            zero.setVisibility(View.INVISIBLE);
            quatre.setVisibility(View.INVISIBLE);
            vuit.setVisibility(View.INVISIBLE);
        Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.intro_2);
        Animation animation0 = AnimationUtils.loadAnimation(this,R.anim.intro_0);
        Animation animation4 = AnimationUtils.loadAnimation(this,R.anim.intro_4);
        Animation animation8 = AnimationUtils.loadAnimation(this,R.anim.intro_8);
            animation2.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation){
                quatre.setVisibility(View.VISIBLE);
                quatre.startAnimation(animation4);
                vuit.setVisibility(View.VISIBLE);
                vuit.startAnimation(animation8);
            }

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation){}
        });
            animation8.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation){
                zero.setVisibility(View.VISIBLE);
                zero.startAnimation(animation0);
            }
        });
            animation0.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation){
                try {
                    Thread.sleep(500);
                }catch(Exception ex){

                }
                Intent intent = new Intent();
                Bundle extras = getIntent().getExtras();
                intent.putExtra("Size",extras.getInt("Size"));
                intent.putExtra("Score",extras.getInt("Score"));
                intent.setClass(getApplicationContext(),GameStarter.class);
                startActivity(intent);
            }
        });
            dos.startAnimation(animation2);
    }
}
