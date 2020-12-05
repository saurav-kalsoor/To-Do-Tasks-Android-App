package com.example.flappybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Building {
    int x = 0, y = 0, width, height;
    Bitmap building;

    Building(Resources res) {

        building = BitmapFactory.decodeResource(res, R.drawable.building11);

        width = building.getWidth() / 2;
        height = building.getHeight();
    }

    Bitmap getBuilding() {
        building = Bitmap.createScaledBitmap(building, width, height, false);
        return building;
    }

    Rect getCollisionShapeBot() {
        return new Rect(x, y, x + width, y + height);
    }

}
