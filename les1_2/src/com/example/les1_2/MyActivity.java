package com.example.les1_2;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

public class MyActivity extends Activity {
    public static int screen_w = 0;
    public static int screen_h = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screen_w=size.x;
        screen_h=size.y;
        super.onCreate(savedInstanceState);
        setContentView(new MySurfaceView(this));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);// full screen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);// no title
    }
}
