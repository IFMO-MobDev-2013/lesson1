package ru.ifmo.mobdev.whirl;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;


public class MyActivity extends Activity {

    private WhirlView myView;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //shuffleColors();
        //fillMatrix();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        myView = new WhirlView(this, width, height);
        setContentView(myView);
    }
}
