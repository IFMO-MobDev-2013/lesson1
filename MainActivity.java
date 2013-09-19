package com.example.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import java.util.Random;

public class MainActivity extends Activity {
    public static final int WIDTH = 240;
    public static final int HEIGHT = 320;
    public static final int COUNT_COLOURS = 10;
    public static final int[] colors =  {Color.BLACK, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY, Color.GREEN, Color.LTGRAY, Color.MAGENTA, Color.RED, Color.YELLOW};
    
    int[][] tempscreen = new int[HEIGHT][WIDTH]; int [][] screen = new int[HEIGHT][WIDTH];
    int[] forbitmap = new int[HEIGHT*WIDTH+1]; int ifb = 0; // iterator for forbitmap
    Bitmap bitmap;
    Paint paint = new Paint();
    long frames = 0;

    class WhirlView extends View {
        public WhirlView(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            ifb = 0;

            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    int tempcolour = screen[i][j] + 1; tempcolour = (tempcolour == COUNT_COLOURS) ? 0 : tempcolour;
                    
                    if (screen[(i-1 < 0) ? HEIGHT-1 : i-1][(j-1 < 0) ? WIDTH-1 : j-1] == tempcolour || screen[i][(j-1 < 0) ? WIDTH-1 : j-1] == tempcolour || screen[(i+1 >= HEIGHT) ? 0 : i+1][(j-1 < 0) ? WIDTH-1 : j-1] == tempcolour
                            || screen[(i-1 < 0) ? HEIGHT-1 : i-1][j] == tempcolour || screen[(i+1 >= HEIGHT) ? 0 : i+1][j] == tempcolour || screen[(i-1 < 0) ? HEIGHT-1 : i-1][(j+1 >= WIDTH) ? 0 : j+1] == tempcolour 
                            || screen[i][(j+1 >= WIDTH) ? 0 : j+1] == tempcolour || screen[(i+1 >= HEIGHT) ? 0 : i+1][(j+1 >= WIDTH) ? 0 : j+1] == tempcolour) {
                        tempscreen[i][j] = tempcolour;
                        forbitmap[ifb++] = colors[tempscreen[i][j]];
                    }
                }
            }

            bitmap.setPixels(forbitmap, 1, WIDTH, 0, 0, WIDTH, HEIGHT);
            canvas.scale((float)canvas.getWidth() / WIDTH, (float)canvas.getHeight() / HEIGHT);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            canvas.drawText(Long.toString(1000 * (frames++) / SystemClock.currentThreadTimeMillis()), 20, 40, paint);
            invalidate();
            for (int i = 0; i < HEIGHT; ++i) for (int j = 0; j < WIDTH; ++j) screen[i][j] = tempscreen[i][j];
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.RGB_565);
        Random rand = new Random();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
            	screen[i][j] = rand.nextInt(COUNT_COLOURS);
            	forbitmap[ifb++] = colors[screen[i][j]];
            }
        }

        bitmap.setPixels(forbitmap, 1, WIDTH, 0, 0, WIDTH, HEIGHT);

        super.onCreate(savedInstanceState);
        setContentView(new WhirlView(this));
    }
}