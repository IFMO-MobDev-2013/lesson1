package com.example.whirl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: elena
 * Date: 11.09.13
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
public class Draw extends View {

    public Draw(Context context) {
        super(context);
        for (int i = 0; i < 16; i++) {
            color[i] = random.nextInt();
        }

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                m[i][j] = StrictMath.abs(random.nextInt() % 16);
            }
        }
        begin = System.currentTimeMillis();
        paint.setColor(Color.BLUE);
    }

    Paint paint = new Paint();
    int[][] m = new int[HEIGHT][WIDTH];
    int[][] m1 = new int[HEIGHT][WIDTH];
    int[][] tmp = new int[HEIGHT][WIDTH];
    int[] color = new int[16];
    Random random = new Random();
    long begin;
    long end;
    final static int HEIGHT = 320;
    final static int WIDTH = 240;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.scale(1f * MyActivity.getScreenWidth() / WIDTH, 1f * MyActivity.getScreenHeight() / HEIGHT);
        int[] draw_field = new int[WIDTH * HEIGHT];


        begin = System.currentTimeMillis();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {

                boolean f = true;
                if (m[((i - 1) + HEIGHT) % HEIGHT][((j - 1) + WIDTH) % WIDTH] != (m[i][j] + 1) % 16)
                    if (m[((i - 1) + HEIGHT) % HEIGHT][j] != (m[i][j] + 1) % 16)
                        if (m[((i - 1) + HEIGHT) % HEIGHT][((j + 1) + WIDTH) % WIDTH] != (m[i][j] + 1) % 16)
                            if (m[i][((j + 1) + WIDTH) % WIDTH] != (m[i][j] + 1) % 16)
                                if (m[((i + 1) + HEIGHT) % HEIGHT][((j + 1) + WIDTH) % WIDTH] != (m[i][j] + 1) % 16)
                                    if (m[((i + 1) + HEIGHT) % HEIGHT][j] != (m[i][j] + 1) % 16)
                                        if (m[((i + 1) + HEIGHT) % HEIGHT][((j - 1) + WIDTH) % WIDTH] != (m[i][j] + 1) % 16)
                                            if (m[i][((j - 1) + WIDTH) % WIDTH] != (m[i][j] + 1) % 16)
                                                f = false;
                if (f)
                    m1[i][j] = (m[i][j] + 1) % 16;
                else
                    m1[i][j] = m[i][j];
            }
        }

        tmp = m;
        m = m1;
        m1 = tmp;
        for (int l = 0; l < HEIGHT; l++) {
            for (int k = 0; k < WIDTH; k++) {
                draw_field[k + l * WIDTH] = color[m[l][k]];
            }
        }

        canvas.drawBitmap(draw_field, 0, WIDTH, 0, 0, WIDTH, HEIGHT, true, null);
        end = System.currentTimeMillis();
        canvas.drawText(1000 / (end - begin) + " fps", 30, 30, paint);
        begin = end;
        invalidate();
    }
}


