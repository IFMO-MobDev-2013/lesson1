package com.polarnick.day01;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class WhirlView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawerThread drawerThread;

    public WhirlView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawerThread = new DrawerThread(getHolder());
        drawerThread.setRunning(true);
        drawerThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawerThread.setRunning(false);
        while (retry) {
            try {
                drawerThread.join();
                retry = false;
            } catch (InterruptedException ignored) {
            }
        }
    }

}