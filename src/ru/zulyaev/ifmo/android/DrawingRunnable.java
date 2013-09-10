package ru.zulyaev.ifmo.android;

import android.graphics.*;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Random;

/**
 * @author Никита
 */
public class DrawingRunnable implements Runnable {
    private static final int TASK_WIDTH = 240;
    private static final int TASK_HEIGHT = 240;

    private static final Random random = new Random();
    private static final int COLOR_NUM = 15;
    private static final int[] COLORS = new int[COLOR_NUM];
    private static final int CACHE_SIZE_BITS = 4;
    private static final int CACHE_SIZE = 1 << CACHE_SIZE_BITS;
    private static final int CACHE_MASK = CACHE_SIZE - 1;
    private static final int[] NEXT_COLOR = new int[CACHE_SIZE];

    static {
        if (CACHE_SIZE < COLOR_NUM) {
            throw new RuntimeException("Cache size is too small for such number of colors");
        }

        boolean[] was = new boolean[CACHE_SIZE];
        for (int i = 0; i < COLOR_NUM; ++i) {
            int color;
            do {
                color = random.nextInt(1 << 24);
            } while (was[color & CACHE_MASK]);
            was[color & CACHE_MASK] = true;
            COLORS[i] = color;
        }
        for (int i = 0; i < COLOR_NUM; ++i) {
            NEXT_COLOR[COLORS[i] & CACHE_MASK] = COLORS[(i + 1) % COLOR_NUM];
        }
    }

    private static final int AGGREGATION_FRAMES = 100;
    private static final Paint FILL = new Paint();
    private static final Paint TEXT = new Paint();
    static {
        TEXT.setColor(Color.WHITE);
        TEXT.setTextSize(20f);
        TEXT.setStyle(Paint.Style.FILL);
        FILL.setAntiAlias(false);
        FILL.setHinting(Paint.HINTING_OFF);
    }

    private static final int BUFFERS = COLOR_NUM;

    private final long[] frames = new long[AGGREGATION_FRAMES];

    private final SurfaceHolder holder;
    private final boolean hack;

    private final int width;
    private final int height;
    private final int cells;
    private final int maxX;
    private final int maxOffset;
    private final Matrix matrix;

    private final int[][] buffers;
    private int currentBuffer = 0;
    private int cycle = 0;

    public DrawingRunnable(SurfaceHolder holder, boolean fullscreen, boolean hack) {
        this.holder = holder;
        this.hack = hack;

        Rect size = holder.getSurfaceFrame();
        if (fullscreen) {
            this.width = size.right;
            this.height = size.bottom;

            matrix = null;
        } else {
            this.width = TASK_WIDTH;
            this.height = TASK_HEIGHT;

            matrix = new Matrix();
            matrix.setScale((float) size.right / TASK_WIDTH, (float) size.bottom / TASK_HEIGHT);
        }

        this.cells = width * height;
        this.maxX = width - 1;
        this.maxOffset = cells - width;
        this.buffers = new int[BUFFERS][cells];


        for (int i = 0; i < cells; ++i) {
            int j = random.nextInt(COLORS.length);
            buffers[0][i] = COLORS[j];
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            long time = SystemClock.uptimeMillis();

            final int[] buffer;

            final int[] old = buffers[currentBuffer];
            if (++currentBuffer == BUFFERS) currentBuffer = 0;
            buffer = buffers[currentBuffer];

            if (cycle == 0) {
                boolean all = true;
                for (int offset = 0, offset1 = maxOffset, offset2 = width, x = 0; offset < cells; ) {
                    final int i = offset + x;
                    final int should = NEXT_COLOR[old[i] & CACHE_MASK];

                    final int x1 = x == 0 ? maxX : x - 1;
                    final int x2 = x == maxX ? 0 : x + 1;

                    if (old[offset1 + x1] == should || old[offset1 + x] == should || old[offset1 + x2] == should
                            || old[offset + x1] == should || old[offset + x2] == should
                            || old[offset2 + x1] == should || old[offset2 + x] == should || old[offset2 + x2] == should) {
                        buffer[i] = should;
                    } else {
                        buffer[i] = old[i];
                        all = false;
                    }

                    if (x == maxX) {
                        x = 0;
                        if (offset == 0) {
                            offset1 = 0;
                        } else {
                            offset1 += width;
                        }
                        offset += width;
                        if (offset == maxOffset) {
                            offset2 = 0;
                        } else {
                            offset2 += width;
                        }
                    } else {
                        x += 1;
                    }
                }
                if (all && hack) ++cycle;
            } else if (cycle++ < BUFFERS) {
                for (int i = 0; i < cells; ++i) {
                    buffer[i] = NEXT_COLOR[old[i] & CACHE_MASK];
                }
            }

            System.arraycopy(frames, 0, frames, 1, frames.length - 1);
            frames[0] = SystemClock.uptimeMillis();

            Log.d("LOGIC", Long.toString(SystemClock.uptimeMillis() - time));

            final Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.setMatrix(matrix);
                canvas.drawBitmap(buffer, 0, width, 0, 0, width, height, false, null);
                canvas.drawText(Long.toString(Math.round(AGGREGATION_FRAMES * 1000. / (frames[0] - frames[frames.length - 1]))), 10, 25, TEXT);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
