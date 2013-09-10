package ru.zulyaev.ifmo.android;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Никита
 */
public abstract class BaseActivity extends Activity {
    private DrawingView view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = makeDrawingView();
        setContentView(view);
    }

    protected abstract DrawingView makeDrawingView();

    @Override
    protected void onPause() {
        super.onPause();
        view.pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        view.resume();
    }
}
