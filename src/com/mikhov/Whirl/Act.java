package com.mikhov.Whirl;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.view.*;
import android.os.Bundle;

import java.util.Random;

public class Act extends Activity {

    int[][] field = null, field2 = null;
    Display display;
    int w = 240, h = 320, max_color = 16, x2, y2;
    float px_w, px_h, scale, f2;
    long s;
    Paint p = new Paint();

    Bitmap bitmap = null;
    int[] bc = null;

    int[] palette = {
            Color.rgb(16, 16, 16),
            Color.rgb(32, 32, 32),
            Color.rgb(48, 48, 48),
            Color.rgb(64, 64, 64),
            Color.rgb(80, 80, 80),
            Color.rgb(96, 96, 96),
            Color.rgb(112, 112, 112),
            Color.rgb(128, 128, 128),
            Color.rgb(144, 144, 144),
            Color.rgb(160, 160, 160),
            Color.rgb(176, 176, 176),
            Color.rgb(192, 192, 192),
            Color.rgb(208, 208, 208),
            Color.rgb(224, 224, 224),
            Color.rgb(240, 240, 240),
            Color.rgb(250, 250, 250)
    };

    class wView extends View {

        public wView(Context context) {
            super(context);
            initField();
        }

        void initField() {
            field = new int[w][h];
            Random rand = new Random();
            for (int x = 0; x < w; ++x) {
                for (int y = 0; y < h; ++y) {
                    field[x][y] = rand.nextInt(max_color);
                }
            }
        }

        @Override
        public void onDraw(Canvas canvas) {
            s = System.nanoTime();

            field2 = new int[w][h];
            for (int x = 0; x < w; ++x) {
                for (int y=0; y < h; ++y) {
                    field2[x][y] = field[x][y];
                    f2 = (field[x][y] + 1) % max_color;
                    for (int dx = -1; dx <= 1; ++dx) {
                        x2 = x + dx;
                        if (x2 < 0) {
                            x2 += w;
                        }
                        if (x2 >= w) {
                            x2 -= w;
                        }
                        for (int dy = -1; dy <= 1; ++dy) {
                            y2 = y + dy;
                            if (y2 < 0) {
                                y2 += h;
                            }
                            if (y2 >= h) {
                                y2 -= h;
                            }
                            if (f2 == field[x2][y2]) {
                                field2[x][y] = field[x2][y2];
                            }
                        }
                    }
                }
            }
            field = field2;

            for (int x = 0; x < w; ++x) {
                for (int y = 0; y < h; ++y) {
                    bc[x + y * w] = palette[field[x][y]];
                }
            }
            bitmap.setPixels(bc, 0, w, 0, 0, w, h);

            canvas.scale(scale, scale);
            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.drawText("FPS: " + (int) (1000000000 / (System.nanoTime() - s)), 10, 15, p);
            invalidate();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        display = getWindowManager().getDefaultDisplay();
        px_w = display.getWidth() / 240;
        px_h = display.getHeight() / 320;
        scale = px_h > px_w ? px_h : px_w;
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bc = new int[w * h];
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new wView(this));
    }
}
