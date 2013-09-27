package com.example.testapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import static android.os.SystemClock.*;

public class MySurfaceView  extends SurfaceView  implements SurfaceHolder.Callback{
    private final int THREAD_COUNT = 3;
    private Double FPS = 0.0;
    private Double FPSAVG = 0.0;
    static public final int DEFAULT_COLOR_COUNT = 3;
    private int GRID_WIDTH;
    private int GRID_HEIGHT;
    private int COLORS;
    static public final int DEFAULT_WIDTH = 100;
    static public final int DEFAULT_HEIGHT = 100;
    private int[][] arr_front;
    private int[][] arr_back;
    private int[] color_buffer;
    private long current_time;
    private long starttime;
    private long frames = 0;
    private int[] color_map;
    private Bitmap bmp;
    Paint p;
    public MySurfaceView(Context context, int height, int width, int colors) {
        super(context);
        p = new Paint();
        getHolder().addCallback(this);
        current_time = uptimeMillis();
        starttime = current_time;
        GRID_WIDTH = width;
        GRID_HEIGHT = height;
        COLORS = colors;
        arr_front = new int[height][width];
        color_buffer = new int[colors];
        color_map = new int [width * height];
        Random x = new Random();
        for (int i = 0; i < colors; i++) {
            color_buffer[i] = x.nextInt();
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                arr_front[i][j] = x.nextInt(colors);
                color_map[i * width + j] = color_buffer[arr_front[i][j]];
            }
        }
        arr_back = arr_front.clone();
        bmp = Bitmap.createBitmap(color_map, height, width, Bitmap.Config.RGB_565).copy(Bitmap.Config.RGB_565, true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder hld) {
        MyThread[] threads = new MyThread[4];
        for (int i = 0; i < 4; i++) {
            threads[i] = new MyThread(hld, this);
            threads[i].setRunnable(true);
            threads[i].start();
        }

    }
    @Override
    public void surfaceChanged(SurfaceHolder hld, int fmt, int w, int h) {


    }
    @Override
    public void surfaceDestroyed(SurfaceHolder hld) {

    }
    public void doDraw(Canvas cs) {
        update_grid();
        frames++;
        p.setColor(Color.WHITE);
        p.setTextSize(40);
        long now = SystemClock.uptimeMillis();
        long delta = now - current_time;
        FPS = (1.0 / delta) * 1000.0;
        current_time = now;
        long deltaavg = now - starttime;
        FPSAVG = ((double)frames / deltaavg) * 1000.0;
        cs.drawColor(Color.BLACK);
        cs.scale(cs.getWidth() / (float) GRID_WIDTH, cs.getHeight() / (float) GRID_HEIGHT);
        cs.drawBitmap(bmp, 0, 0, null);
        cs.scale(1/(cs.getWidth()/(float)GRID_WIDTH), 1/(cs.getHeight()/(float)GRID_HEIGHT));

        cs.drawText("FPS:" + FPS.toString(), 120, 100, p);
        cs.drawText("FPSAVG:" + FPSAVG.toString(), 120, 200, p);
    }
    private void update_grid() {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                int t = (arr_back[i][j] + 1) % COLORS;
                int r1 = i == 0 ? GRID_HEIGHT - 1 : i - 1;
                int r2 = i == GRID_HEIGHT - 1 ? 0 : i + 1;
                int c1 = j == 0 ? GRID_WIDTH -1 : j - 1;
                int c2 = j == GRID_WIDTH - 1 ? 0 : j + 1;
                if (arr_back[r1][c1] == t || arr_back[r2][c2] == t || arr_back[r1][c2] == t || arr_back[r2][c1] == t
                        || arr_back[r1][j] == t || arr_back[r2][j] == t || arr_back [i][c1] == t || arr_back[i][c2] == t) {
                    arr_front[i][j] = t;
                    //color_map[i * j] = color_buffer[t];
                    bmp.setPixel(i,j , color_buffer[t]);
                }
            }
        }
        arr_back = arr_front.clone();



    }





}
