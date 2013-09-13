package ru.zulyaev.ifmo.android;

import android.graphics.*;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * @author Никита
 */
public class DrawingRunnable implements Runnable {
    private static final int AGGREGATION_FRAMES = 100;
    private static final Paint TEXT = new Paint();
    static {
        TEXT.setColor(Color.WHITE);
        TEXT.setTextSize(50f);
        TEXT.setStyle(Paint.Style.FILL);
    }

    private final long[] frames = new long[AGGREGATION_FRAMES];

    private final SurfaceHolder holder;
    private final UpdaterRunnable updater;
    private final int width;
    private final int height;
    private final Matrix matrix;

    public DrawingRunnable(SurfaceHolder holder, UpdaterRunnable updater, int width, int height) {
        this.holder = holder;
        this.updater = updater;
        this.width = width;
        this.height = height;

        Rect size = holder.getSurfaceFrame();
        matrix = new Matrix();
        matrix.setScale((float) size.right / width, (float) size.bottom / height);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                long time = SystemClock.uptimeMillis();

                System.arraycopy(frames, 0, frames, 1, frames.length - 1);
                frames[0] = SystemClock.uptimeMillis();

                Log.d("LOGIC", Long.toString(SystemClock.uptimeMillis() - time));

                final Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.setMatrix(matrix);
                    canvas.drawBitmap(updater.getBuffer(), 0, width, 0, 0, width, height, false, null);
                    canvas.setMatrix(null);
                    canvas.drawText(Long.toString(Math.round(AGGREGATION_FRAMES * 1000. / (frames[0] - frames[frames.length - 1]))), 10, 60, TEXT);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        } catch (InterruptedException e) {
            // done.
        }
    }
}
