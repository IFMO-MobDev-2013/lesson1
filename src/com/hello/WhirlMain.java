package com.hello;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.*;

import android.graphics.Paint;
import android.os.SystemClock;

public class WhirlMain {

    static final int COLORS_CNT = 15;
    Bitmap bitmap = null;
    int[] color;
    int width = 240;
    int height = 320;
    int curElement, i, j;
    int[][] map;
    int[] prev_row, current_row, next_row;
    int[][] second_map;
    int[] resultColor;
    Paint paint;
    int matrixWidth, matrixHeight;
    float SX, SY;
    int count = 0;
    long currentTime, lastTime;
    float fps;
    public WhirlMain() {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        color = new int[COLORS_CNT];
        second_map = new int[width][height];
        map = new int[width][height];
        resultColor = new int[width * height];
        //------------------------------------
        Random rand = new Random();
        for (int i = 0; i < COLORS_CNT; i++) {
            color[i] = rand.nextInt(0xFFFFFF + 1);
        }
        //------------------------------------ generate random colors

        //------------------------------------

        for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++) {
                map[i][j] = rand.nextInt(COLORS_CNT);
            }
        //------------------------------------ generate pixels' map
        paint = new Paint();
    }

    void update() {
        i = j = 0;
        int dy1, dy2;
        int cnt = 0;
        for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++) {

                dy1 = (j + 1 == height ? 0 : j + 1);
                dy2 = (j - 1 == -1 ? height - 1 : j - 1);
                curElement = (map[i][j] + 1 == COLORS_CNT ? 0 : map[i][j] + 1);
                prev_row = map[i - 1 == -1 ? width - 1 : i - 1];
                current_row = map[i];
                next_row = map[i + 1 == width ? 0 : i + 1];
                if (curElement == current_row[dy1] ||
                        curElement == next_row[dy1] ||
                        curElement == next_row[j] || curElement == next_row[dy2] ||
                        curElement == current_row[dy2] ||
                        curElement == prev_row[dy2] || curElement == prev_row[j] ||
                        curElement == prev_row[dy1]) {
                    second_map[i][j] = curElement;
                } else {
                    second_map[i][j] = map[i][j];
                }
                resultColor[cnt++] = color[second_map[i][j]];
            }
        int[][] swap = second_map;
        second_map = map;
        map = swap;
    }

    void printFps(Canvas canvas) {
        paint.setTextSize(30);
        paint.setARGB(127, 0, 0, 0);
        canvas.drawText("fps = " + (float) Math.round(1000 * fps) / 1000, 40, 40, paint);
    }

    void countFps() {
        count++;
        currentTime = SystemClock.uptimeMillis();
        long difference = currentTime - lastTime;
        if (difference > 1000) {
            fps = (float) count * 1000 / difference;
            count = 0;
            lastTime = currentTime;
        }

    }

    public void draw(Canvas canvas) {
        matrixHeight = canvas.getHeight();
        matrixWidth = canvas.getWidth();

        update();
        countFps();
        SX = (float) matrixWidth / width;
        SY = (float) matrixHeight / height;
        canvas.scale(SX, SY);  // do scale

        canvas.drawBitmap(resultColor, 0, width, 0, 0, width, height, false, paint);
        canvas.scale((float) 1 / SX, (float) 1 / SY);

        printFps(canvas);
    }
}