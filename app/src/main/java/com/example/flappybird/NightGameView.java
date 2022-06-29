package com.example.flappybird;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NightGameView extends View {
    private Bird bird;
    private Handler handler;
    private Runnable r;
    private ArrayList<Obstacles> arrObstacles;
    private int sumObstacles, distance;
    private int score, topScore = 0;
    private boolean start;
    private Context context;
    private int jumpSound;
    private boolean loadedSound;
    private SoundPool soundPool;

    public NightGameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
        if (sp != null) {
            topScore = sp.getInt("topScore", 0);
        }
        score = 0;
        start = false;
        initBird();
        initObstacles();
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        if (Build.VERSION.SDK_INT >= 21) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(5);
            this.soundPool = builder.build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loadedSound = true;
            }
        });
        jumpSound = this.soundPool.load(context, R.raw.jump_02, 1);
    }

    private void initObstacles() {
        sumObstacles = 4;
        distance = 400 * Constants.SCREEN_HEIGHT / 1920;
        arrObstacles = new ArrayList<>();
        for (int i = 0; i < sumObstacles; i++) {
            if (i < sumObstacles / 2) {
                this.arrObstacles.add(new Obstacles(Constants.SCREEN_WIDTH + i * ((Constants.SCREEN_WIDTH + 200 * Constants.SCREEN_WIDTH / 1080) / (sumObstacles / 2)), 0, 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2));
                this.arrObstacles.get(this.arrObstacles.size() - 1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe2));
                this.arrObstacles.get(this.arrObstacles.size() - 1).randomY();
            } else {
                this.arrObstacles.add(new Obstacles(this.arrObstacles.get(i - sumObstacles / 2).getX(), this.arrObstacles.get(i - sumObstacles / 2).getY() + this.arrObstacles.get(i - sumObstacles / 2).getHeight() + this.distance, 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2));
                this.arrObstacles.get(this.arrObstacles.size() - 1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe1));
            }
        }
    }

    private void initBird() {
        bird = new Bird();
        bird.setWidth(150 * Constants.SCREEN_WIDTH / 1080);
        bird.setHeight(150 * Constants.SCREEN_HEIGHT / 1920);
        bird.setX(150 * Constants.SCREEN_WIDTH / 1080);
        bird.setY(Constants.SCREEN_HEIGHT / 2 - bird.getHeight() / 2);
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bat1));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bat2));
        bird.setArrBms(arrBms);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (start) {
            bird.draw(canvas);
            for (int i = 0; i < sumObstacles; i++) {
                if (bird.getRect().intersect(arrObstacles.get(i).getRect()) || bird.getY() - bird.getHeight() < 0 || bird.getY() > Constants.SCREEN_HEIGHT) {
                    Obstacles.speed = 0;
                    Night.txtScoreOver.setText(Night.txtScore.getText());
                    Night.txtTopScore.setText("Top Score : " + topScore);
                    Night.txtScore.setVisibility(INVISIBLE);
                    Night.PanelGameOver.setVisibility(VISIBLE);

                }
                if (this.bird.getX() + this.bird.getWidth() > arrObstacles.get(i).getX() + arrObstacles.get(i).getWidth() / 2
                        && this.bird.getX() + this.bird.getWidth() <= arrObstacles.get(i).getX() + arrObstacles.get(i).getWidth() / 2 + Obstacles.speed && i < sumObstacles / 2) {
                    score++;
                    if (score > topScore) {
                        topScore = score;
                        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("topScore", topScore);
                        editor.apply();
                    }
                    Night.txtScore.setText("Score : " + score);

                }
                if (this.arrObstacles.get(i).getX() < -arrObstacles.get(i).getWidth()) {
                    this.arrObstacles.get(i).setX(Constants.SCREEN_WIDTH);
                    if (i < sumObstacles / 2) {
                        arrObstacles.get(i).randomY();
                    } else {
                        arrObstacles.get(i).setY(this.arrObstacles.get(i - sumObstacles / 2).getY() + this.arrObstacles.get(i - sumObstacles / 2).getHeight() + this.distance);
                    }
                }
                this.arrObstacles.get(i).draw(canvas);
            }
        } else {
            if (bird.getY() > Constants.SCREEN_HEIGHT / 2) {
                bird.setDrop(-15 * Constants.SCREEN_HEIGHT / 1920);
            }
            bird.draw(canvas);
        }
        handler.postDelayed(r, 10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            bird.setDrop(-15);
            if (loadedSound) {
                int streamId = this.soundPool.play(this.jumpSound, (float) 0.5, (float) 0.5, 1, 0, 1f);
            }
        }
        return true;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void reset() {
        Night.txtScore.setText("Score : 0");
        score = 0;
        initObstacles();
        initBird();
    }
}
