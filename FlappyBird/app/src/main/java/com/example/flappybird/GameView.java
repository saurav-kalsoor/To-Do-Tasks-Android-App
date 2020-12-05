package com.example.flappybird;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private int screenX, screenY;
    private Background background1, background2;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private boolean isPlaying = true, scored = false;
    private Bird bird;
    private int jump = 0, buildingSpeed = 10, buildingGap, score = 0;
    Random random;
    private Building buildingBot, buildingTop;
    private SharedPreferences prefs;


    public GameView(Activity activity, int screenX, int screenY) {
        super(activity);
        this.screenX = screenX;
        this.screenY = screenY;

        random = new Random();

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        screenRatioX = screenX / 1080f;
        screenRatioY = screenY / 1980f;

        buildingGap = (int) (400 * screenRatioY);

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        background2.x = screenX;

        bird = new Bird(getResources());
        bird.y = screenY / 2;
        bird.x = screenX / 3;


        buildingBot = new Building(getResources());
        buildingTop = new Building(getResources());

        setBuildingHeight();

        buildingBot.x = screenX;
        buildingTop.x = screenX;


        paint = new Paint();
        paint.setTextSize(128 * screenRatioX);
        paint.setColor(Color.WHITE);
    }

    @Override
    public void run() {

        while (isPlaying) {
            update();
            draw();
            sleep();
        }

    }


    private void update() {

        background1.x -= (int) (buildingSpeed * screenRatioX);
        background2.x -= (int) (buildingSpeed * screenRatioX);

        if (background1.x + background1.background.getWidth() < 0)
            background1.x = screenX;

        if (background2.x + background2.background.getWidth() < 0)
            background2.x = screenX;

        if (jump > 0) {
            bird.y -= (int) (140 * screenRatioY);
            jump--;
        } else {
            bird.y += (int) (20 * screenRatioY);
        }

        if (bird.y > screenY - bird.height) {
            updateHighScore();
            isPlaying = false;
        }


        if (bird.y < 0)
            bird.y = 0;

        buildingBot.x -= (int) (buildingSpeed * screenRatioX);
        buildingTop.x -= (int) (buildingSpeed * screenRatioX);

        if (buildingBot.x + buildingBot.width < 0) {
            buildingBot.x = screenX;
            buildingTop.x = screenX;
            scored = false;
            setBuildingHeight();
        }

        if (buildingBot.x + buildingBot.width < bird.x && !scored) {
            score++;
            scored = true;
        }


        if (Rect.intersects(bird.getCollisionShape(), buildingBot.getCollisionShapeBot()) || Rect.intersects(bird.getCollisionShape(), buildingTop.getCollisionShapeBot())) {
            updateHighScore();
            isPlaying = false;
        }


    }

    private void setBuildingHeight() {
        buildingBot.height = random.nextInt(2 * (screenY / 3));
        if (buildingBot.height < screenY / 5)
            buildingBot.height = screenY / 5;
        buildingTop.height = screenY - buildingBot.height - buildingGap;

        buildingBot.y = screenY - buildingBot.height + 30;
        buildingTop.y = -50;
    }


    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);

            canvas.drawBitmap(buildingBot.getBuilding(), buildingBot.x, buildingBot.y, paint);
            canvas.drawBitmap(buildingTop.getBuilding(), buildingTop.x, buildingTop.y, paint);


            canvas.drawText("" + score, screenX / 2, 128 * screenRatioY, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }


    }

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
            updateHighScore();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateHighScore() {
        int hscore = prefs.getInt("score", 0);
        if (score > hscore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("score", score);
            editor.apply();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                jump++;
                break;
        }
        return true;
    }
}
