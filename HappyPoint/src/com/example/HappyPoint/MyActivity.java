package com.example.HappyPoint;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import java.util.Random;

public class MyActivity extends Activity {
    private final int WIDTH = 240;
    private final int HEIGHT = 320;
    private final int NUMCOLORS = 32;
    private final int ALL = HEIGHT*WIDTH;

    private int[][] arr;
    private int[][] arrColor;
    private int[] colors;
    private Paint paint;
    private Point size;
    private int poz, poz2, W, H, cur = 0, in = 0;
    Display display;
    private long start, end;

    public class Painting extends View {
        public Painting(Context context) {
            super(context);
        }

        private void rePainting() {
            in++; in &= 1;

            // left - up - angle
            poz = 0;
            arrColor[in][poz] = (arrColor[cur][poz] == NUMCOLORS - 1) ? 0 : arrColor[cur][poz] + 1;
            if (arrColor[in][poz] != arrColor[cur][WIDTH - 1] &&
                arrColor[in][poz] != arrColor[cur][ALL - 1] &&
                arrColor[in][poz] != arrColor[cur][ALL - WIDTH] &&
                arrColor[in][poz] != arrColor[cur][ALL - WIDTH + 1] &&
                arrColor[in][poz] != arrColor[cur][1] &&
                arrColor[in][poz] != arrColor[cur][WIDTH + 1] &&
                arrColor[in][poz] != arrColor[cur][WIDTH] &&
                arrColor[in][poz] != arrColor[cur][WIDTH + WIDTH - 1])
                   arrColor[in][poz] = arrColor[cur][poz];
            arr[in][poz] = colors[arrColor[in][poz]];


            // right - up - angle
            poz = WIDTH - 1;
            arrColor[in][poz] = (arrColor[cur][poz] == NUMCOLORS - 1) ? 0 : arrColor[cur][poz] + 1;
            if (arrColor[in][poz] != arrColor[cur][WIDTH - 2] &&
                    arrColor[in][poz] != arrColor[cur][ALL - 2] &&
                    arrColor[in][poz] != arrColor[cur][ALL - 1] &&
                    arrColor[in][poz] != arrColor[cur][ALL - WIDTH] &&
                    arrColor[in][poz] != arrColor[cur][0] &&
                    arrColor[in][poz] != arrColor[cur][WIDTH] &&
                    arrColor[in][poz] != arrColor[cur][poz + WIDTH] &&
                    arrColor[in][poz] != arrColor[cur][WIDTH + WIDTH - 2])
                            arrColor[in][poz] = arrColor[cur][poz];
            arr[in][poz] = colors[arrColor[in][poz]];

            // right - down - angle
            poz = ALL - 1;
            arrColor[in][poz] = (arrColor[cur][poz] == NUMCOLORS - 1) ? 0 : arrColor[cur][poz] + 1;
            if (arrColor[in][poz] != arrColor[cur][poz - 1] &&
                    arrColor[in][poz] != arrColor[cur][poz - WIDTH - 1] &&
                    arrColor[in][poz] != arrColor[cur][poz - WIDTH] &&
                    arrColor[in][poz] != arrColor[cur][ALL - WIDTH - WIDTH] &&
                    arrColor[in][poz] != arrColor[cur][ALL - WIDTH] &&
                    arrColor[in][poz] != arrColor[cur][0] &&
                    arrColor[in][poz] != arrColor[cur][WIDTH - 1] &&
                    arrColor[in][poz] != arrColor[cur][WIDTH - 2])
                arrColor[in][poz] = arrColor[cur][poz];
            arr[in][poz] = colors[arrColor[in][poz]];

            // left - down - angle
            poz = ALL - WIDTH;
            arrColor[in][poz] = (arrColor[cur][poz] == NUMCOLORS - 1) ? 0 : arrColor[cur][poz] + 1;
            if (arrColor[in][poz] != arrColor[cur][ALL - 1] &&
                    arrColor[in][poz] != arrColor[cur][poz - 1] &&
                    arrColor[in][poz] != arrColor[cur][poz - WIDTH] &&
                    arrColor[in][poz] != arrColor[cur][poz - WIDTH + 1] &&
                    arrColor[in][poz] != arrColor[cur][poz + 1] &&
                    arrColor[in][poz] != arrColor[cur][1] &&
                    arrColor[in][poz] != arrColor[cur][0] &&
                    arrColor[in][poz] != arrColor[cur][WIDTH - 1])
                arrColor[in][poz] = arrColor[cur][poz];
            arr[in][poz] = colors[arrColor[in][poz]];

            // up ans down strings
            poz = 0;
            poz2 = ALL - WIDTH;
            for (int i = 1; i < WIDTH - 1; i++) {
                poz++;
                arrColor[in][poz] = (arrColor[cur][poz] == NUMCOLORS - 1) ? 0 : arrColor[cur][poz] + 1;
                if (arrColor[in][poz] != arrColor[cur][poz - 1] &&
                        arrColor[in][poz] != arrColor[cur][ALL - WIDTH + i - 1] &&
                        arrColor[in][poz] != arrColor[cur][ALL - WIDTH + i] &&
                        arrColor[in][poz] != arrColor[cur][ALL - WIDTH + i + 1] &&
                        arrColor[in][poz] != arrColor[cur][poz + 1] &&
                        arrColor[in][poz] != arrColor[cur][poz + WIDTH + 1] &&
                        arrColor[in][poz] != arrColor[cur][poz + WIDTH] &&
                        arrColor[in][poz] != arrColor[cur][poz + WIDTH - 1])
                    arrColor[in][poz] = arrColor[cur][poz];
                arr[in][poz] = colors[arrColor[in][poz]];

                poz2++;
                arrColor[in][poz2] = (arrColor[cur][poz2] == NUMCOLORS - 1) ? 0 : arrColor[cur][poz2] + 1;
                if (arrColor[in][poz2] != arrColor[cur][poz2 - 1] &&
                        arrColor[in][poz2] != arrColor[cur][poz2 - WIDTH - 1] &&
                        arrColor[in][poz2] != arrColor[cur][poz2 - WIDTH] &&
                        arrColor[in][poz2] != arrColor[cur][poz2 - WIDTH + 1] &&
                        arrColor[in][poz2] != arrColor[cur][poz2 + 1] &&
                        arrColor[in][poz2] != arrColor[cur][i + 1] &&
                        arrColor[in][poz2] != arrColor[cur][i] &&
                        arrColor[in][poz2] != arrColor[cur][i - 1])
                    arrColor[in][poz2] = arrColor[cur][poz2];
                arr[in][poz2] = colors[arrColor[in][poz2]];
            }

            // left ans right strings
            poz = 0;
            poz2 = WIDTH - 1;
            for (int i = 1; i < HEIGHT - 1; i++) {
                poz+=WIDTH;
                arrColor[in][poz] = (arrColor[cur][poz] == NUMCOLORS - 1) ? 0 : arrColor[cur][poz] + 1;
                if (arrColor[in][poz] != arrColor[cur][poz + WIDTH - 1] &&
                        arrColor[in][poz] != arrColor[cur][poz - 1] &&
                        arrColor[in][poz] != arrColor[cur][poz - WIDTH] &&
                        arrColor[in][poz] != arrColor[cur][poz - WIDTH + 1] &&
                        arrColor[in][poz] != arrColor[cur][poz + 1] &&
                        arrColor[in][poz] != arrColor[cur][poz + WIDTH + 1] &&
                        arrColor[in][poz] != arrColor[cur][poz + WIDTH] &&
                        arrColor[in][poz] != arrColor[cur][poz + WIDTH + WIDTH - 1])
                    arrColor[in][poz] = arrColor[cur][poz];
                arr[in][poz] = colors[arrColor[in][poz]];

                poz2+=WIDTH;
                arrColor[in][poz2] = (arrColor[cur][poz2] == NUMCOLORS - 1) ? 0 : arrColor[cur][poz2] + 1;
                if (arrColor[in][poz2] != arrColor[cur][poz2 - 1] &&
                        arrColor[in][poz2] != arrColor[cur][poz2 - WIDTH - 1] &&
                        arrColor[in][poz2] != arrColor[cur][poz2 - WIDTH] &&
                        arrColor[in][poz2] != arrColor[cur][poz2 - WIDTH - WIDTH + 1] &&
                        arrColor[in][poz2] != arrColor[cur][poz2 - WIDTH + 1] &&
                        arrColor[in][poz2] != arrColor[cur][poz2 + 1] &&
                        arrColor[in][poz2] != arrColor[cur][poz2 + WIDTH] &&
                        arrColor[in][poz2] != arrColor[cur][poz2 + WIDTH - 1])
                    arrColor[in][poz2] = arrColor[cur][poz2];
                arr[in][poz2] = colors[arrColor[in][poz2]];
            }

            // into frame
            poz = WIDTH;
            for (int i = 1; i < HEIGHT - 1; i++)
                for (int j = 1; j < WIDTH - 1; j++) {
                    poz++;
                    arrColor[in][poz] = (arrColor[cur][poz] == NUMCOLORS - 1) ? 0 : arrColor[cur][poz] + 1;
                    if (arrColor[in][poz] != arrColor[cur][poz - 1] &&
                        arrColor[in][poz] != arrColor[cur][poz - WIDTH - 1] &&
                        arrColor[in][poz] != arrColor[cur][poz - WIDTH] &&
                        arrColor[in][poz] != arrColor[cur][poz - WIDTH + 1] &&
                        arrColor[in][poz] != arrColor[cur][poz + 1] &&
                        arrColor[in][poz] != arrColor[cur][poz + WIDTH + 1] &&
                        arrColor[in][poz] != arrColor[cur][poz + WIDTH] &&
                        arrColor[in][poz] != arrColor[cur][poz + WIDTH - 1])
                              arrColor[in][poz] = arrColor[cur][poz];
                    arr[in][poz] = colors[arrColor[in][poz]];
                    if (j == WIDTH - 2) {poz++; poz++;}
                }
        }

        @Override
        public void onDraw(Canvas canvas) {
            display = getWindowManager().getDefaultDisplay();
            display.getSize(size);
            W = size.x;
            H = size.y;
            canvas.scale((float)W/WIDTH, (float)H/HEIGHT);
            canvas.drawBitmap(arr[cur], 0, WIDTH, 0, 0, WIDTH, HEIGHT, false, null);

            end = System.currentTimeMillis();
            canvas.drawText("FPS: " + 1000 / (end - start), 10, 30, paint);
            start = System.currentTimeMillis();

            rePainting();
            cur++; cur &= 1;
            invalidate();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        Random rand = new Random();
        paint = new Paint();
        size = new Point();
        colors   = new int[NUMCOLORS];
        arr = new int[2][ALL];
        arrColor = new int[2][ALL];
        start = 0;
        paint.setTextSize(20);
        paint.setARGB(255, 0, 0, 255);
        for (int i = 0; i < NUMCOLORS; i++)
            colors[i] = rand.nextInt(Integer.MAX_VALUE);

        for (int i = 0; i < ALL; i++) {
            arrColor[0][i] = rand.nextInt(NUMCOLORS);
            arr[0][i] = colors[arrColor[0][i]];
        }

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        setContentView(new Painting(this));
    }
}
