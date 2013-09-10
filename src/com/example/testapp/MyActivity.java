package com.example.testapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

public class MyActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new MyView(this));
    }

    private class MyView extends View {

        private final int W = 240, H = 320;
        private final int DW, DH;
        private final int numberOfColors = 20;
        private final int[] colors = new int[numberOfColors];
        private final Paint fpsColor = new Paint();
        private int[][] stepOne, stepTwo;
        int[] p = new int[W * H];
        private long getTime;
        private long fps;
        private int frameCount;
        private static final int MAX_FRAME_COUNT = 100;
        private final long[] frames = new long[MAX_FRAME_COUNT];
        private boolean COOL_HACKER = false;

        public MyView(Context context) {
            super(context);

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            DW = size.x;
            DH = size.y;

            fpsColor.setColor(Color.WHITE);
            fpsColor.setTextSize(12);
            fpsColor.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            frameCount = 0;
            getTime = SystemClock.currentThreadTimeMillis();

            int r[] = {0xff0000, 0x880000, 0x000000};
            int g[] = {0x00ff00, 0x008800, 0x000000};
            int b[] = {0x0000ff, 0x000088, 0x000000};
            for(int i = 0; i < numberOfColors; ++i) {
                colors[i] = ( (r[i%3]) | (g[(i/3 + 1)%3]) | b[(i/9 + 2) % 3] );
            }

            stepOne = new int[H][W];
            stepTwo = new int[H][W];

            Random random = new Random();
            for (int i = 0; i < H; i++) {
                for (int j = 0; j < W; j++) {
                    stepOne[i][j] = random.nextInt(numberOfColors);
                }
            }
            //setFocusable(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (COOL_HACKER) {
                int k = 0;
                for (int i = 0; i < H; ++i) {
                    for (int j = 0; j < W; ++j) {
                        int curColor = stepOne[i][j] + 1;
                        if(curColor >= numberOfColors)
                            curColor = 0;
                        p[k++] = colors[stepOne[i][j] = curColor];
                    }
                }
            } else {
                COOL_HACKER = true;
                int k = 0;
                for(int i = 0; i < H; i++) {
                    int prevX = (i-1==-1)?(H-1):(i-1);
                    int nextX = (i+1==H)?(0):(i+1);
                    for(int j = 0; j < W; j++) {
                        int curColor = stepOne[i][j] + 1;
                        if(curColor >= numberOfColors)
                            curColor = 0;
                        int prevY = (j-1==-1)?(W-1):(j-1);
                        int nextY = (j+1==W)?(0):(j+1);
                        if ( curColor == stepOne[nextX][nextY] ||
                                curColor == stepOne[nextX][j] ||
                                curColor == stepOne[nextX][prevY] ||

                                curColor == stepOne[prevX][nextY] ||
                                curColor == stepOne[prevX][j] ||
                                curColor == stepOne[prevX][prevY] ||

                                curColor == stepOne[i][nextY] ||
                                curColor == stepOne[i][prevY]) {
                            p[k++] = colors[stepTwo[i][j] = curColor];
                        } else {
                            p[k++] = colors[stepTwo[i][j] = stepOne[i][j]];
                            COOL_HACKER = false;
                        }
                    }
                }
                int[][] t = stepOne;
                stepOne = stepTwo;
                stepTwo = t;
            }

            Matrix matrix = new Matrix();
            matrix.setScale((float) DW / W, (float) DH / H);
            canvas.setMatrix(matrix);
            canvas.drawBitmap(p, 0, W, 0, 0, W, H, false, null);

            System.arraycopy(frames, 0, frames, 1, MAX_FRAME_COUNT - 1);
            frames[0] = SystemClock.uptimeMillis();

            canvas.drawText(Long.toString(1000 * MAX_FRAME_COUNT  / (frames[0] - frames[MAX_FRAME_COUNT - 1])), 5, 10, fpsColor);

            invalidate();
        }
    }
}