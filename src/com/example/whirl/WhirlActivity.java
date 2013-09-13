package com.example.whirl;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.*;

import java.util.Random;

// Виктор Хованский 2539
public class WhirlActivity extends Activity {
    static class WhirlView extends SurfaceView implements Runnable
    {
        private int frameCounter;
        private long lastFpsCalcUptime;
        private final long FPS_CALC_INTERVAL = 1000;
        private double fps = 0;
        private Paint p = new Paint();
        private final int MAX_COLOR = 20;
        private static SurfaceHolder holder;
        private static int[][][] field = null;
        private static int[] bytes;
        private static int current = 0;
        private static int width = 240;
        private static int height = 320;
        private static int[] palette;

        public WhirlView(Context context)
        {
            super(context);
            initField();
            holder = getHolder();
            Thread thread = new Thread(this);
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }

        private void measureFps() {
            frameCounter++;
            long now = SystemClock.uptimeMillis();
            long delta = now - lastFpsCalcUptime;
            if (delta > FPS_CALC_INTERVAL) {
                fps = frameCounter * FPS_CALC_INTERVAL / (double)delta;

                frameCounter = 0;
                lastFpsCalcUptime = now;
            }
        }

        public void run()
        {
            while (true)
            {
                updateField();
                measureFps();
                if (holder.getSurface().isValid())
                {
                    Canvas canvas = holder.lockCanvas();
                    onDraw(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }

        void initField()
        {
            Random rand = new Random();
            palette = new int[MAX_COLOR];
            for (int i = 0; i < MAX_COLOR; i++)
            {
                float[] hsv = new float[3];
                hsv[0] = (MAX_COLOR - i -1) * 360f / MAX_COLOR;
                hsv[1] = 0.7f;
                hsv[2] = 0.8f;
                palette[i] = Color.HSVToColor(hsv);
            }
            field = new int[2][width][height];
            bytes = new int[width * height];
            for (int x = 0; x < width; ++x)
            {
                for (int y = 0; y < height; ++y)
                {
                    field[0][x][y] = rand.nextInt(MAX_COLOR);
                }
            }
        }

        void updateField()
        {
            int next = 1 - current;
            int k = 0;
            for (int x = 0; x < width; ++x)
            {
                for (int y = 0; y < height; ++y)
                {
                    int next_color = (field[current][x][y] + 1) % MAX_COLOR;
                    if (    cell(x-1, y-1) == next_color ||
                            cell(x, y-1) == next_color ||
                            cell(x-1, y) == next_color ||
                            cell(x+1, y-1) == next_color ||
                            cell(x-1, y+1) == next_color ||
                            cell(x, y+1) == next_color ||
                            cell(x+1, y) == next_color ||
                            cell(x+1, y+1) == next_color)
                    {
                        field[next][x][y] = next_color;
                    }
                    else
                    {
                        field[next][x][y] = field[current][x][y];
                    }
                    bytes[k] = palette[field[next][x][y]];
                    k++;
                }
            }
            current = next;
        }

        int cell(int x, int y)
        {
            if (x == -1) x = width - 1;
            if (y == -1) y = height - 1;
            if (x == width) x = 0;
            if (y == height) y = 0;
            return field[current][x][y];
        }

        @Override
        public void onDraw(Canvas canvas)
        {
            Matrix matrix = new Matrix();
            matrix.setScale(canvas.getWidth() * 1f / height, canvas.getHeight() * 1f / width);
            canvas.setMatrix(matrix);
            canvas.drawBitmap(bytes, 0, height, 0, 0, height, width, false, null);
            p.setColor(Color.BLACK);
            canvas.drawText(Math.ceil(fps*10)/10+" FPS", 30, 30, p);
        }
    }
    private static View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        view = new WhirlView(this);
        setContentView(view);
    }
}
