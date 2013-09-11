package ru.ifmo.mobdev.MatveevA.GameLife;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import java.util.Random;

public class MyActivity extends Activity {

    class WhirlView extends View {

        private static final int COUNT_OF_COLOR = 16;
        private static final int HIGH = 320;
        private static final int WEIGH = 240;

        private int[] colors = new int[COUNT_OF_COLOR];
        private int[] buf = new int[WEIGH * HIGH];
        private int[] newBuf = new int[WEIGH * HIGH];
        private int[] saveColor = new int[WEIGH * HIGH];
        Paint p;
        Paint p1;

        private int FPS;
        private int curFPS;
        private long start;

        public WhirlView(Context context) {
            super(context);

            Random rand = new Random();

            for (int i = 0; i < COUNT_OF_COLOR; i++) {
                colors[i] = rand.nextInt(Integer.MAX_VALUE);
                colors[i] = (colors[i] | (0xFF000000));
            }

            for (int i = 0; i < HIGH; i++) {
                for (int j = 0; j < WEIGH; j++) {
                    buf[i*WEIGH + j] = rand.nextInt(COUNT_OF_COLOR);
                }
            }

            p = new Paint();
            p1 = new Paint();
            FPS = 0;
            curFPS = 0;
            start = System.currentTimeMillis();

        }

        private void print(Canvas canvas) {

            curFPS++;

            if (System.currentTimeMillis() - start > 1000)  {
                FPS = curFPS;
                curFPS = 0;
                start = System.currentTimeMillis();
            }

            canvas.scale(3, 4);
            canvas.drawBitmap(saveColor, 0, WEIGH, 0, 0, WEIGH, HIGH, true, p);

            p1.setARGB(255, 255, 255, 0);
            p1.setTextSize(30);
            canvas.drawText("FPS = " + FPS, 10, 30, p1);
        }

        private boolean check(int i, int j) {
            int nextI, nextJ, prevI, prevJ;

            nextI = i + 1;
            nextJ = j + 1;
            prevI = i - 1;
            prevJ = j - 1;

            if (nextI >= HIGH) {
                nextI = 0;
            }
            if (nextJ >= WEIGH) {
                nextJ = 0;
            }
            if (prevI == -1) {
                prevI = HIGH - 1;
            }
            if (prevJ == -1) {
                prevJ = WEIGH - 1;
            }

            if ((buf[i*WEIGH + j] + 1) % COUNT_OF_COLOR == buf[prevI*WEIGH + j])
                return true;
            if ((buf[i*WEIGH + j] + 1) % COUNT_OF_COLOR == buf[prevI*WEIGH + nextJ])
                return true;
            if ((buf[i*WEIGH + j] + 1) % COUNT_OF_COLOR == buf[i*WEIGH + nextJ])
                return true;
            if ((buf[i*WEIGH + j] + 1) % COUNT_OF_COLOR == buf[nextI*WEIGH + nextJ])
                return true;
            if ((buf[i*WEIGH + j] + 1) % COUNT_OF_COLOR == buf[nextI*WEIGH + j])
                return true;
            if ((buf[i*WEIGH + j] + 1) % COUNT_OF_COLOR == buf[nextI*WEIGH + prevJ])
                return true;
            if ((buf[i*WEIGH + j] + 1) % COUNT_OF_COLOR == buf[i*WEIGH + prevJ])
                return true;
            if ((buf[i*WEIGH + j] + 1) % COUNT_OF_COLOR == buf[prevI*WEIGH + prevJ])
                return true;

            return false;
        }

        private void change() {

            for (int i = 0; i < HIGH; i++) {
                for (int j = 0; j < WEIGH; j++) {

                    if (check(i, j)) {
                        newBuf[i*WEIGH + j] = (buf[i*WEIGH + j] + 1) % COUNT_OF_COLOR;
                        saveColor[i*WEIGH + j] = colors[newBuf[i*WEIGH + j]];
                    }
                    else {
                        newBuf[i*WEIGH + j] = buf[i*WEIGH + j];
                        saveColor[i*WEIGH + j] = colors[newBuf[i*WEIGH + j]];
                    }
                }
            }

            for (int i = 0; i < HIGH; i++) {
                for (int j = 0; j < WEIGH; j++) {
                    buf[i*WEIGH + j] = newBuf[i*WEIGH + j];

                }
            }


        }

        @Override
        public void onDraw(Canvas canvas) {
            print(canvas);
            change();
            invalidate();
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new WhirlView(this));
    }
}