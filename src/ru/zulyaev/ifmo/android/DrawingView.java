package ru.zulyaev.ifmo.android;

import android.content.Context;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Никита
 */
public class DrawingView extends SurfaceView implements SurfaceHolder.Callback {
    private static final int TASK_WIDTH = 240;
    private static final int TASK_HEIGHT = 320;

    private ExecutorService service = Executors.newFixedThreadPool(2);
    private Runnable drawingRunnable;
    private UpdaterRunnable updaterRunnable;
    private Future<?> drawingFuture;
    private Future<?> updaterFuture;

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
        final int width;
        final int height;
        if (fullscreen) {
            Rect size = holder.getSurfaceFrame();
            width = size.right;
            height = size.bottom;
        } else {
            width = TASK_WIDTH;
            height = TASK_HEIGHT;
        }
        updaterRunnable = new UpdaterRunnable(width, height, hack);
        drawingRunnable = new DrawingRunnable(holder, updaterRunnable, width, height);
        drawingFuture = service.submit(drawingRunnable);
        updaterFuture = service.submit(updaterRunnable);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        pause();
    }

    public void pause() {
        if (drawingFuture != null) {
            drawingFuture.cancel(true);
            drawingFuture = null;

        }
        if (updaterFuture != null) {
            updaterFuture.cancel(true);
            updaterFuture = null;
        }
    }

    public void resume() {
        if (drawingRunnable != null && drawingFuture == null) {
            drawingFuture = service.submit(drawingRunnable);
        }
        if (updaterRunnable != null && updaterFuture == null) {
            updaterFuture = service.submit(updaterRunnable);
        }
    }
}
