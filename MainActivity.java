package com.example.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import java.util.Random;

public class MainActivity extends Activity {
    final int WIDTH = 240;
    final int HEIGHT = 320;

    int[] colors = new int[10];
    int[][] screen = new int[HEIGHT][WIDTH];
    Bitmap bitmap;
    Matrix matrix = new Matrix();
    Paint paint = new Paint();
    long frames = 0;

    class WhirlView extends View {
        public WhirlView(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            int screenHeight = canvas.getHeight();
            int screenWidth = canvas.getWidth();
            canvas.scale((float)screenWidth / WIDTH, (float)screenHeight / HEIGHT);
            canvas.drawBitmap(bitmap, matrix, paint);

            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    int tempcolour = screen[i][j] + 1; tempcolour = (tempcolour == 10) ? 0 : tempcolour;
                    
                    if (screen[(i-1 < 0) ? HEIGHT-1 : i-1][(j-1 < 0) ? WIDTH-1 : j-1] == tempcolour || screen[i][(j-1 < 0) ? WIDTH-1 : j-1] == tempcolour || screen[(i+1 >= HEIGHT) ? 0 : i+1][(j-1 < 0) ? WIDTH-1 : j-1] == tempcolour
                            || screen[(i-1 < 0) ? HEIGHT-1 : i-1][j] == tempcolour || screen[(i+1 >= HEIGHT) ? 0 : i+1][j] == tempcolour || screen[(i-1 < 0) ? HEIGHT-1 : i-1][(j+1 >= WIDTH) ? 0 : j+1] == tempcolour 
                            || screen[i][(j+1 >= WIDTH) ? 0 : j+1] == tempcolour || screen[(i+1 >= HEIGHT) ? 0 : i+1][(j+1 >= WIDTH) ? 0 : j+1] == tempcolour) {
                        screen[i][j] = tempcolour;
                        bitmap.setPixel(j, i, colors[tempcolour]);
                    }
                }
            }

            canvas.drawText(Long.toString(1000 * (frames++) / SystemClock.currentThreadTimeMillis()), 10, 10, paint);
            invalidate();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        colors[0] = Color.BLACK;
        colors[1] = Color.BLUE;
        colors[2] = Color.CYAN;
        colors[3] = Color.DKGRAY;
        colors[4] = Color.GRAY;
        colors[5] = Color.GREEN;
        colors[6] = Color.LTGRAY;
        colors[7] = Color.MAGENTA;
        colors[8] = Color.RED;
        colors[9] = Color.YELLOW;

        bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_4444);
        Random rand = new Random();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                screen[i][j] = rand.nextInt(10);
                bitmap.setPixel(j, i, colors[screen[i][j]]);
            }
        }



        super.onCreate(savedInstanceState);
        setContentView(new WhirlView(this));
    }
}