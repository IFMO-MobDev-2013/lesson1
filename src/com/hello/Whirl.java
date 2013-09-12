package com.hello;

/*
    http://habrahabr.ru/post/126316/ SurfaceView
*/


import android.app.Activity;
import android.os.Bundle;

public class Whirl extends Activity {
    public static final int THREADS = 2 * Runtime.getRuntime().availableProcessors();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WhirlMain currentWhirl = new WhirlMain();
        WhirlSurfaceView surfaceView = new WhirlSurfaceView(this, currentWhirl, THREADS);
        setContentView(surfaceView);
    }

}