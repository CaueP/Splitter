package com.caue.splitter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Caue on 9/3/2016.
 */
public class SplashScreenActivity extends AppCompatActivity{

    private final int DURACAO_SPLASHSCREEN = 4000;  // tempo de duracao da splash screen (ms)

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Criando a animação
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.move_up);
        /*
        // Aplicando animação à imagem
        ImageView logoImageView = (ImageView) findViewById(R.id.logo);
        logoImageView.setAnimation(anim);*/

        // Aplicando animação ao texto
        TextView logoTextView = (TextView) findViewById(R.id.textLogo);
        logoTextView.setAnimation(anim);

        // Definindo o que acontece quando a animação terminar
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Ao termino da animacao, exiba a progressBar
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish(); // Finalização da atividade para evitar que quando o usuário
                            // pressione Back, ele retorne à Splash Screen
            }
        },DURACAO_SPLASHSCREEN);     //definindo o tempo da animacao

    }
}
