package com.caue.splitter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Caue Polimanti
 * @version 1.0
 * created on 9/3/2016
 */
public class SplashScreenActivity extends AppCompatActivity{

    private final int DURACAO_SPLASHSCREEN = 1500;  // tempo de duracao da splash screen (ms)


    @BindView(R.id.textLogo) TextView logoTextView;// = (TextView) findViewById(R.id.textLogo);
    @BindView(R.id.progressBar) ProgressBar progressBar;    // = (ProgressBar) findViewById(R.id.progressBar);

    @BindView(android.R.id.content) View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        Log.d("SplashScreenActivity","onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        Log.d("SplashScreenActivity","after ButterKnife.bind");
        // Criando a animação
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.move_up);
        /*
        // Aplicando animação à imagem
        ImageView logoImageView = (ImageView) findViewById(R.id.logo);
        logoImageView.setAnimation(anim);*/

        // Aplicando animação ao texto

        logoTextView.setAnimation(anim);

        // Definindo o que acontece quando a animação terminar
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d("SplashScreenActivity","onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("SplashScreenActivity","onAnimationEnd");
                // Ao termino da animacao, exiba a progressBar
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // Criando Handler para duração da Splash
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("SplashScreenActivity","Handler running");
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish(); // Finalização da atividade para evitar que quando o usuário
                            // pressione Back, ele retorne à Splash Screen
            }
        },DURACAO_SPLASHSCREEN);     //definindo o tempo da animacao

    }
}
