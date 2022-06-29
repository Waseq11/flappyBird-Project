package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    public static TextView txtScore, txtTopScore, txtScoreOver, logo;
    public static RelativeLayout PanelGameOver;
    private GameView Gv;
    public static Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        logo = findViewById(R.id.logo);
        txtScore = findViewById(R.id.txtScore);
        txtTopScore = findViewById(R.id.txtTopScore);
        txtScoreOver = findViewById(R.id.txtScoreOver);
        PanelGameOver = findViewById(R.id.PanelGameOver);
        Gv = findViewById(R.id.Gv);
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gv.setStart(true);
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
                Gv.setStart(false);
                Gv.reset();
            }
        });
    }
}