package com.hello;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WhirlSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private WhirlMain currentWhirl;
    private int THREADS;
    private DrawThread[] threads;
    public WhirlSurfaceView(Context context, WhirlMain currentWhirl, int THREADS) {
        super(context);
        this.currentWhirl = currentWhirl;
        this.THREADS = THREADS;
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        threads = new DrawThread[THREADS];
        DrawThread drawThread;
        for (int i = 0; i < THREADS; i++) {
            drawThread = new DrawThread(holder, currentWhirl);
            threads[i] = drawThread;
            drawThread.setRunning(true);
            drawThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        // завершаем работу потока
        for (int i = 0; i < THREADS; i++)
            threads[i].setRunning(false);
        while (retry) {
            try {
                for (int i = 0; i < THREADS; i++)
                    threads[i].join();
                retry = false;
            } catch (InterruptedException e) {
                // если не получилось, то будем пытаться еще и еще
            }
        }
    }
}
