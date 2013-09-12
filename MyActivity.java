package com.tourist.CellularAutomaton;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.util.Random;

public class MyActivity extends Activity {

    class MyView extends View {
        final int h = 320, w = 240;
        final int[] colors = new int[]{0xFFFFFFFF, 0xFFEEFFFF, 0xFFDDFFFF, 0xFFCCFFFF,
                                       0xFFBBFFFF, 0xFFAAFFFF, 0xFF99FFFF, 0xFF88FFFF,
                                       0xFF77FFFF, 0xFF66FFFF, 0xFF55FFFF, 0xFF44FFFF,
                                       0xFF33FFFF, 0xFF22FFFF, 0xFF11FFFF, 0xFF00FFFF};
        final int colorsCount = colors.length;
        Random rnd = new Random();

        int[] field;
        int[] c;
        int[] newField;

        long startTime;
        int iterations;
        int total;
        int fps;
        boolean period;

        public MyView(Context context) {
            super(context);
            initField();
        }

        void initField() {
            field = new int[(h + 2) * (w + 2)];
            for (int i = 0; i < field.length; i++) {
                field[i] = rnd.nextInt(colorsCount);
            }
            period = false;
            c = new int[h * w];
            newField = new int[(h + 2) * (w + 2)];
            iterations = 0;
            total = 0;
            fps = 0;
        }

        void updateField() {
            if (!period) {
                for (int i = 1; i <= h; i++) {
                    field[i * (w + 2) + 0] = field[i * (w + 2) + w];
                    field[i * (w + 2) + (w + 1)] = field[i * (w + 2) + 1];
                }
                for (int j = 0; j <= w + 1; j++) {
                    field[0 * (w + 2) + j] = field[h * (w + 2) + j];
                    field[(h + 1) * (w + 2) + j] = field[1 * (w + 2) + j];
                }
                int cnt = 0;
                for (int i = 1; i <= h; i++) {
                    for (int j = 1; j <= w; j++) {
                        int u = i * (w + 2) + j;
                        int need = field[u] + 1;
                        if (need == colorsCount) need = 0;
                        if (field[u - 1] == need || field[u + 1] == need || field[u - w - 2] == need || field[u + w + 2] == need
                                || field[u - w - 3] == need || field[u - w - 1] == need || field[u + w + 1] == need || field[u + w + 3] == need) {
                            newField[u] = need;
                            cnt++;
                        }
                        else newField[u] = field[u];
                    }
                }
                System.arraycopy(newField, 0, field, 0, field.length);
                if (cnt == h * w) period = true;
            } else {
                for (int i = 0; i < field.length; i++) {
                    field[i]++;
                    if (field[i] == colorsCount) field[i] = 0;
                }
            }
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    c[i * w + j] = colors[field[(i + 1) * (w + 2) + (j + 1)]];
                }
            }
        }

        @Override
        public void onDraw(Canvas canvas) {
            Display display = getWindowManager().getDefaultDisplay();
            Point p = new Point();
            display.getSize(p);
            canvas.scale((float)p.x / w, (float)p.y / h);
            canvas.drawBitmap(c, 0, w, 0, 0, w, h, false, null);

            iterations++;
            total++;
            if (total == 1) {
                startTime = System.currentTimeMillis();
            } else {
                long curTime = System.currentTimeMillis();
                while (curTime > startTime + 1000) {
                    startTime += 1000;
                    fps = iterations;
                    iterations = 0;
                }
                if (total == iterations) fps = (int)(1000.0 * (total - 1) / (curTime - startTime));
            }

            Paint paint = new Paint();
            paint.setColor(0x7F000000);
            paint.setTextSize(h / 20);
            canvas.drawRect(0, 0, w, h / 10, paint);
            paint.setColor(0xFFFFFFFF);
            canvas.drawText("fps: " + fps, w / 10, 3 * h / 40, paint);

            updateField();
            invalidate();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new MyView(this));
    }
}
