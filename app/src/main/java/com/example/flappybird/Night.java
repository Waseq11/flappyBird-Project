package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Night extends AppCompatActivity {

    public static TextView txtScore, txtTopScore, txtScoreOver, logo;
    public static RelativeLayout PanelGameOver;
    private NightGameView Ngv;
    public static Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_night);
        logo = findViewById(R.id.logo);
        txtScore = findViewById(R.id.txtScore);
        txtTopScore = findViewById(R.id.txtTopScore);
        txtScoreOver = findViewById(R.id.txtScoreOver);
        PanelGameOver = findViewById(R.id.PanelGameOver);
        Ngv = findViewById(R.id.Ngv);
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ngv.setStart(true);
                txtScore.setVisibility(View.VISIBLE);
                logo.setVisibility(View.INVISIBLE);
                btnStart.setVisibility(View.INVISIBLE);
            }
        });
        PanelGameOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStart.setVisibility(View.VISIBLE);
                PanelGameOver.setVisibility(View.INVISIBLE);
                Ngv.setStart(false);
                Ngv.reset();
            }
        });
    }
}