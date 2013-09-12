package ru.skipor.Whirl.WhirlView;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;

import ru.skipor.Whirl.WhirlEngine.Exception.WhrilEngineWrongInputFormatException;


public class MainActivity extends Activity {
    DisplayMetrics metrics;

    public MainActivity() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        metrics = getResources().getDisplayMetrics();

        try {
            setContentView(new WhirlView(this));
        } catch (WhrilEngineWrongInputFormatException e) {
            e.printStackTrace();
        }
    }

    public int getScreenHeight() {
        return metrics.heightPixels;
    }

    public int getScreenWidth() {
        return metrics.widthPixels;
    }
}
