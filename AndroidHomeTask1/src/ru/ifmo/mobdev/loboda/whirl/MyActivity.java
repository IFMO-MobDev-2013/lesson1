package ru.ifmo.mobdev.loboda.whirl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyActivity extends Activity {

    public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
        private DrawThread drawThread;

        public MySurfaceView(Context context) {
            super(context);
            getHolder().addCallback(this);
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            drawThread = new DrawThread(getHolder(), 240, 320, size.x, size.y);
            drawThread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MySurfaceView(this));
    }
}
