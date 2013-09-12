package com.example.Whirl;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import java.util.Random;

public class Whirl extends Activity {
    /**
     * Called when the activity is first created.
     */

    Point point = new Point();
    final int rowsCount = 320;
    final int colsCount = 240;
    int[][] cells = new int[rowsCount][colsCount];
    int[][] cells2 = new int[rowsCount][colsCount];
    int[] pixels = new int[rowsCount * colsCount];
    int[] colors = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA,
            Color.YELLOW, 0xCCFF33, 0xCC9933, 0x99FFCC, 0x9900CC,
            0x33FF66, 0x000066, 0x00FF00, 0xFF99CC, 0xFF0066, 0xCCCCFF};
    int fps = 0;
    int tempFPS = 0;
    long currTime = System.currentTimeMillis();


    class WhirlView extends View {


        public WhirlView(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            Paint p = new Paint();
            canvas.scale(point.x / (float) colsCount, point.y / (float) rowsCount);
            canvas.drawBitmap(pixels, 0, colsCount, 0, 0, colsCount, rowsCount, false, null);

            countFPS(System.currentTimeMillis());
            p.setColor(Color.BLACK);
            p.setTextSize(25);
            p.setTypeface(Typeface.DEFAULT_BOLD);
            canvas.drawText("FPS: " + Integer.toString(fps), 100, 100, p);
            recountColors();
            for (int i = 0; i < rowsCount; i++) {
                for (int j = 0; j < colsCount; j++) {
                    cells[i][j] = cells2[i][j];
                    pixels[colsCount * i + j] = colors[cells2[i][j]];
                }
            }
            invalidate();

        }
    }

    private void countFPS(long time) {
        if (time - currTime < 1000) {
            tempFPS++;
        } else {
            currTime = time;
            fps = tempFPS;
            tempFPS = 0;
        }
    }

    private void recountColors() {
        int ip;
        int im;
        int jp;
        int jm;
        int t;
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < colsCount; j++) {
                im = i == 0 ? rowsCount - 1 : i - 1;
                ip = i == rowsCount - 1 ? 0 : i + 1;
                jm = j == 0 ? colsCount - 1 : j - 1;
                jp = j == colsCount - 1 ? 0 : j + 1;
                cells2[i][j] = cells[i][j];
                t = cells[im][jm];
                if (t == (cells[i][j] + 1) % colors.length) {
                    cells2[i][j] = t;
                    continue;
                }
                t = cells[im][j];
                if (t == (cells[i][j] + 1) % colors.length) {
                    cells2[i][j] = t;
                    continue;
                }
                t = cells[im][jp];
                if (t == (cells[i][j] + 1) % colors.length) {
                    cells2[i][j] = t;
                    continue;
                }
                t = cells[i][jm];
                if (t == (cells[i][j] + 1) % colors.length) {
                    cells2[i][j] = t;
                    continue;
                }
                t = cells[i][jp];
                if (t == (cells[i][j] + 1) % colors.length) {
                    cells2[i][j] = t;
                    continue;
                }
                t = cells[ip][jm];
                if (t == (cells[i][j] + 1) % colors.length) {
                    cells2[i][j] = t;
                    continue;
                }
                t = cells[ip][j];
                if (t == (cells[i][j] + 1) % colors.length) {
                    cells2[i][j] = t;
                    continue;
                }
                t = cells[ip][jp];
                if (t == (cells[i][j] + 1) % colors.length) {
                    cells2[i][j] = t;

                }
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(point);
        Random rand = new Random();
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < colsCount; j++) {
                cells[i][j] = Math.abs(rand.nextInt()) % colors.length;
                pixels[colsCount * i + j] = colors[cells[i][j]];
            }
        }
        setContentView(new WhirlView(this));
    }
}