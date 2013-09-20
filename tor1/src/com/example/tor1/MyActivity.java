package com.example.tor1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import java.util.Random;

public class MyActivity extends Activity {

    static int colorsCount = 16;
    //Display display = getWindowManager().getDefaultDisplay();
    int height = 320;
    int width = 240;//(int)(height * 1.0 * display.getWidth() / display.getHeight());
    //float sx = (float)(display.getWidth() * 1.0 / width);
    //float sy = (float)(display.getHeight() * 1.0 / height);
    static int[] colors = new int[Math.max(16, colorsCount)];
    static Random random = new Random();
    int[] pixels = new int[width * height];
    int[][] pixels1 = new int[height][width];
    int[][] pixels2 = new int[height][width];
    static int[] vx = {-1, 0, 1, 1, 1, 0, -1, -1};
    static int[] vy = {-1, -1, -1, 0, 1, 1, 1, 0};
    static long curTime = 0;
    static int curIterations = 0;
    static int curFPS = 0;
    static int maxFPS = 0;
    static Paint textPaint = new Paint();


    class MyView extends View {
        public MyView(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {

            for(int i = 0; i < height; i++)
                for(int j = 0; j < width; j++)
                    pixels2[i][j] = pixels1[i][j];
            for(int i = 1; i < height - 1; i++)
                for(int j = 1; j < width - 1; j++)
                {
                    for(int h = 0; h < 8; h++)
                        if(pixels2[i][j] + 1 < colorsCount)
                        {
                            if(pixels2[i][j] + 1 == pixels2[i + vy[h]][j + vx[h]])
                            {
                                pixels1[i][j]++;
                                if(pixels1[i][j] >= colorsCount)
                                    pixels1[i][j] = 0;
                                pixels[i * width + j] = colors[pixels1[i][j]];
                                break;
                            }
                        }
                        else
                            if(pixels2[i + vy[h]][j + vx[h]] == 0)
                            {
                                pixels1[i][j]++;
                                if(pixels1[i][j] >= colorsCount)
                                    pixels1[i][j] = 0;
                                pixels[i * width + j] = colors[pixels1[i][j]];
                                break;
                            }
                }
            for(int i = 0; i < height; i++)
                for(int j = 0; j < width; j += width - 1)
                {
                    for(int h = 0; h < 8; h++)
                        if((pixels2[i][j] + 1) % colorsCount == pixels2[(i + height + vy[h]) % height][(j + width + vx[h]) % width])
                        {
                            pixels1[i][j]++;
                            if(pixels1[i][j] >= colorsCount)
                                pixels1[i][j] = 0;
                            pixels[i * width + j] = colors[pixels1[i][j]];
                            break;
                        }
                }
            for(int i = 0; i < height; i += height - 1)
                for(int j = 1; j < width - 1; j++)
                {
                    for(int h = 0; h < 8; h++)
                        if((pixels2[i][j] + 1) % colorsCount == pixels2[(i + height + vy[h]) % height][(j + width + vx[h]) % width])
                        {
                            pixels1[i][j]++;
                            if(pixels1[i][j] >= colorsCount)
                                pixels1[i][j] = 0;
                            pixels[i * width + j] = colors[pixels1[i][j]];
                            break;
                        }
                }
            canvas.scale(4, 3);
            canvas.drawBitmap(pixels, 0, width, 0, 0, width, height, false, textPaint);
            curIterations++;
            if(System.currentTimeMillis() - curTime >= 1000)
            {
                curTime = System.currentTimeMillis();
                curFPS = curIterations;
                maxFPS = Math.max(maxFPS, curFPS);
                curIterations = 0;
            }
            canvas.drawText("fps: " + curFPS, 20, 35, textPaint);
            canvas.drawText("maxfps: " + maxFPS, 20, 65, textPaint);
            invalidate();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textPaint.setARGB(255, 0, 0, 0);
        textPaint.setTextSize(30);
        textPaint.setFakeBoldText(true);
        System.out.println(width + "       " + height);
        colors[0] = Color.argb(250, 255, 218, 185);
        colors[1] = Color.argb(250, 65, 105, 225);
        colors[2] = Color.argb(250, 0, 191, 255);
        colors[3] = Color.argb(250, 0, 100, 0);
        colors[4] = Color.argb(250, 124, 252, 0);
        colors[5] = Color.argb(250, 189, 183, 107);
        colors[6] = Color.argb(250, 255, 215, 0);
        colors[7] = Color.argb(250, 184, 134, 11);
        colors[8] = Color.argb(250, 205, 92, 92);
        colors[9] = Color.argb(250, 139, 69, 19);
        colors[10] = Color.argb(250, 178, 34, 34);
        colors[11] = Color.argb(250, 176, 48, 96);
        colors[12] = Color.argb(250, 0, 245, 255);
        colors[13] = Color.argb(250, 84, 255, 159);
        colors[14] = Color.argb(250, 255, 0, 255);
        colors[15] = Color.argb(250, 79, 79, 79);
        for(int i = 16; i < colorsCount; i++)
            colors[i] = Color.argb(250, 255, 0, 0);
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
            {
                pixels1[i][j] = random.nextInt(colorsCount);
                pixels[i * width + j] = colors[pixels1[i][j]];
            }
        curTime = System.currentTimeMillis();
        setContentView(new MyView(this));
    }
}


// 640*480   more than 20 fps