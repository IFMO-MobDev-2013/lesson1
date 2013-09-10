package ru.zulyaev.ifmo.android;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Никита
 */
public class DrawingView extends SurfaceView implements SurfaceHolder.Callback {
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private Runnable drawingRunnable;
    private Future<?> future;

    private boolean fullscreen;
    private boolean hack;

    public DrawingView(Context context, boolean fullscreen, boolean hack) {
        super(context);
        this.fullscreen = fullscreen;
        this.hack = hack;
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawingRunnable = new DrawingRunnable(holder, fullscreen, hack);
        future = service.submit(drawingRunnable);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        pause();
    }

    public void pause() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }

    public void resume() {
        if (drawingRunnable != null && future == null) {
            future = service.submit(drawingRunnable);
        }
    }
}
