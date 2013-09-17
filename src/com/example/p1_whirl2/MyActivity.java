package com.example.p1_whirl2;

import android.*;
import android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

public class MyActivity extends Activity implements Runnable{
    @Override
    public void run() {

    }

    class WhirlView extends View {
        public WhirlView(Context context) {
            super(context);
            display.getSize(p);
            for (int i = 0; i < 320; i++) {
                for (int j = 0; j < 240; j++) {
                    buf[i][j] = change[i][j] = (int)(Math.random()*10);
                }
            }
            starting_time = System.nanoTime();
        }
        //
        long frames = 0;
        long starting_time;
        Point p = new Point();
        Paint fortext = new Paint();
        int[] col = {Color.BLACK, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY, Color.GREEN, Color.LTGRAY, Color.MAGENTA, Color.WHITE, Color.RED};
        int[][] buf = new int[320][240];
        int[][] change = new int[320][240];
        int[] toBitmap = new int[76800];
        int[][] toMemory = new int[200][76800];
        int x1,x2,x3,y1,y2,y3,next_col;
        Display display = getWindowManager().getDefaultDisplay();
        //
        @Override
        public void onDraw (Canvas canvas){

            canvas.scale((float) (p.x / 240.0), (float) (p.y / 320.0));

            for (int i = 0; i < 320; i++) {
                for (int j = 0; j < 240; j++) {
                    x1 = (i != 0? i-1 : 319);
                    x2 = i;
                    x3 = (i != 319? i+1 : 0);
                    y1 = (j != 0? j-1 : 239);
                    y2 = j;
                    y3 = (j != 239? j+1 : 0);
                    next_col = (buf[i][j] + 1) % 10;
                    if ((next_col == buf[x1][y1])||
                            (next_col == buf[x1][y2])||
                            (next_col == buf[x1][y3])||
                            (next_col == buf[x2][y1])||
                            (next_col == buf[x2][y3])||
                            (next_col == buf[x3][y1])||
                            (next_col == buf[x3][y2])||
                            (next_col == buf[x3][y3]))
                    {
                        change[i][j] = next_col;
                        toBitmap[i*240 + j] = col[next_col];
                    }
                }
            }
            for (int i = 0; i < 320; i++) {
                for (int j = 0; j < 240; j++) {
                    buf[i][j] = change[i][j];
                }
            }
            canvas.drawBitmap(toBitmap, 0, 240, 0, 0, 240, 320, false, null);
            fortext.setARGB(234, 234, 23, 65);
            fortext.setTextSize(50);
            frames++;
            canvas.drawText(Float.toString(frames * 1000000000 / (System.nanoTime() - starting_time)) + " fps", 40, 70, fortext);
            invalidate();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new WhirlView(this));
    }
}

/**
 * Called when the activity is first created.
 */

