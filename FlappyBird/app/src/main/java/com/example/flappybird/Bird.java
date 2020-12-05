package com.example.flappybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bird {
    int x = 0, y = 0,birdCounter = 1, width, height;
    Bitmap bird1, bird2, bird3;

    Bird(Resources res){
        bird1 = BitmapFactory.decodeResource(res, R.drawable.bird1);
        bird2 = BitmapFactory.decodeResource(res, R.drawable.bird2);
        bird3 = BitmapFactory.decodeResource(res, R.drawable.bird3);

        width = (int) (bird1.getWidth()*3/2 * GameView.screenRatioX);
        height = (int) (bird1.getHeight()*3/2 * GameView.screenRatioY);

        bird1 = Bitmap.createScaledBitmap(bird1, width, height, false);
        bird2 = Bitmap.createScaledBitmap(bird2, width, height, false);
        bird3 = Bitmap.createScaledBitmap(bird3, width, height, false);

    }

    Bitmap getBird(){
        switch (birdCounter){
            case 1:
                birdCounter++;
                return bird1;
            case 2:
                birdCounter++;
                return bird2;
        }
        birdCounter = 1;
        return bird3;
    }

    Rect getCollisionShape(){
        return new Rect((int) (x + (0 * GameView.screenRatioX)), (int) (y + (0 * GameView.screenRatioY)), (int) (x + width - (0 * GameView.screenRatioX)), (int) (y + height - (0 * GameView.screenRatioY)));
    }
}
