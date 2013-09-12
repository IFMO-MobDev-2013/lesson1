package com.polarnick.day01;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class HelloAndroidActivity extends Activity {

    private static int screenWidth;
    private static int screenHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        super.onCreate(savedInstanceState);
        setContentView(new WhirlView(this));
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }
}

