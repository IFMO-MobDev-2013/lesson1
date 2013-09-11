package com.example.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.*;

import java.util.Random;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    class WhirlView extends View {
        boolean t;
        long lastSecond, currentSecond;
        long tik;
        int k;
        final static int m = 240;
        final static int n = 320;
        final static int color_cnt = 10;
        int a[][] = new int[n][m];
        int b[][] = new int[n][m];
        int color[] = new int[color_cnt];
        int pixels[] = new int[n * m];
        Paint p = new Paint();
        Random r = new Random();
        Bitmap bitmap = Bitmap.createBitmap(m, n, Bitmap.Config.ARGB_8888);
        float _ratioWidth;
        float _ratioHeight;

        public WhirlView(Context context) {
            super(context);
            Display display = getWindowManager().getDefaultDisplay();


            _ratioWidth = (float) display.getWidth() / (float) bitmap.getWidth();
            _ratioHeight = (float) display.getHeight() / (float) bitmap.getHeight();


            tik = 0;
            p.setTextSize(20);
            p.setARGB(255, 0, 0, 0);
            lastSecond = SystemClock.uptimeMillis();
            color[0] = Color.argb(255, 255, 0, 0);
            color[1] = Color.argb(255, 0, 255, 0);
            color[2] = Color.argb(255, 0, 0, 255);
            color[3] = Color.argb(255, 0, 255, 255);
            color[4] = Color.argb(255, 255, 0, 255);
            color[5] = Color.argb(255, 255, 255, 0);
            color[6] = Color.argb(255, 255, 255, 255);
            color[7] = Color.argb(255, 153, 204, 153);
            color[8] = Color.argb(255, 102, 204, 0);
            color[9] = Color.argb(255, 102, 153, 102);
            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++) {
                    a[i][j] = (r.nextInt() % color_cnt + color_cnt) % color_cnt;
                }
        }

        void FPS(Canvas canvas) {
            tik++;
            currentSecond = SystemClock.uptimeMillis();
            canvas.drawText("FPS " + (tik * 1000 / (currentSecond - lastSecond)), 0, 20, p);
            if (lastSecond + 1000 < currentSecond) {
                lastSecond = currentSecond;
                tik = 0;
            }
        }

        @Override
        public void onDraw(Canvas canvas) {

            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++)
                    if (i == 0 || j == 0 || i == n - 1 || j == m - 1) {
                        t = true;
                        for (int dx = -1; dx <= 1 && t; dx++)
                            for (int dy = -1; dy <= 1 && t; dy++)
                                if ((a[i][j] + 1) % color_cnt == (a[(i + dx + n) % n][(j + dy + m) % m])) {
                                    b[i][j] = (a[i][j] + 1) % color_cnt;
                                    t = false;
                                }

                    } else {
                        t=true;
                        for (int dx = -1; dx <= 1 && t; dx++)
                            for (int dy = -1; dy <= 1 && t; dy++)
                                if ((a[i][j] + 1) % color_cnt == (a[i + dx][j + dy])) {
                                    b[i][j] = (a[i][j] + 1) % color_cnt;
                                    t = false;
                                }
                    }
            k = 0;
            for (int j = 0; j < n; j++)
                for (int i = 0; i < m; i++) {
                    a[j][i] = b[j][i];
                    pixels[k++] = color[a[j][i]];
                }
            bitmap.setPixels(pixels, 0, m, 0, 0, m, n);

            canvas.scale(_ratioWidth, _ratioHeight);
            canvas.drawBitmap(bitmap, 0, 0, null);

            FPS(canvas);

            invalidate();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new WhirlView(this));
    }
}
