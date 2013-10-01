package com.example.Whirl;

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

class WhirlView extends View{

    int D_Height;
    int D_Width;
    final static int WIDTH = 240;
    final static int HEIGHT = 320;
    final static int COLOR_COUNT = 16;
    int[] paints = null;
    Random rand = new Random();
    int[] p = new int[WIDTH*HEIGHT];
    int[][] p_new = new int[WIDTH][HEIGHT];
    int[][] p_color = new int[WIDTH][HEIGHT];
    private static final long FPS_CALC_INTERVAL = 1000L;
    private long lastFpsCalcUptime;
    private long frameCounter;
    private long fps;
    Paint p_fps = new Paint();

    public WhirlView (Context context){

        super(context);

        paints = new int[16];

        paints[0] = Color.argb(255, 255, 0, 0);
        paints[1] = Color.argb(255, 0, 255, 0);
        paints[2] = Color.argb(255, 0, 0, 255);
        paints[3] = Color.argb(255, 0, 255, 255);
        paints[4] = Color.argb(255, 255, 255, 0);
        paints[5] = Color.argb(255, 255, 100, 10);
        paints[6] = Color.argb(255, 255, 10, 100);
        paints[7] = Color.argb(255, 184, 0, 255);
        paints[8] = Color.argb(255, 127, 255, 0);
        paints[9] = Color.argb(255, 127, 199, 255);
        paints[10] = Color.argb(255, 75, 0, 130);
        paints[11] = Color.argb(255, 255, 204, 0);
        paints[12] = Color.argb(255, 21, 96, 189);
        paints[13] = Color.argb(255, 52, 201, 36);
        paints[14] = Color.argb(255, 153, 0, 102);
        paints[15] = Color.argb(255, 165, 38, 10);

        for(int i = 0; i < WIDTH; i++)
            for(int j = 0; j < HEIGHT; j++){
                p_color[i][j] = rand.nextInt(COLOR_COUNT);
                p[j * WIDTH + i] = paints[p_color[i][j]];
                p_new[i][j] = 0;
            }

        p_fps.setTextSize(15.0f);
        p_fps.setColor(Color.BLACK);

    }

    @Override
    public void onDraw(Canvas canvas){

        D_Height = canvas.getHeight();
        D_Width = canvas.getWidth();

        Fps();

        MakeAlive();

        Matrix matrix = new Matrix();
        matrix.setScale((float) D_Width / WIDTH, (float) D_Height / HEIGHT);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(p, 0, WIDTH, 0, 0, WIDTH, HEIGHT, false, null);
        canvas.drawText("FPS: " + fps, 10, 15, p_fps);

        invalidate();

    }

    private void MakeAlive(){

        for(int i = 0; i < WIDTH; i++ )
            for(int j = 0; j < HEIGHT; j++){

                int i_left;
                int i_right;
                int j_up;
                int j_down;
                int color_tmp = (p_color[i][j] + 1) % COLOR_COUNT;

                if(i == 0) i_left = 239; else i_left = i - 1;
                if(i == 239) i_right = 0; else i_right = i + 1;
                if(j == 0) j_up = 319; else j_up = j - 1;
                if(j == 319) j_down = 0; else j_down = j + 1;

                //  1  2  3
                //  4  *  5
                //  6  7  8

                if((color_tmp + p_new[i_left][j_up]) % COLOR_COUNT == p_color[i_left][j_up] ||
                        (color_tmp + p_new[i][j_up]) % COLOR_COUNT == p_color[i][j_up] ||
                        (color_tmp + p_new[i_right][j_up]) % COLOR_COUNT == p_color[i_right][j_up] ||
                        (color_tmp + p_new[i_left][j]) % COLOR_COUNT == p_color[i_left][j] ||
                        (color_tmp + p_new[i_right][j]) % COLOR_COUNT == p_color[i_right][j] ||
                        (color_tmp + p_new[i_left][j_down]) % COLOR_COUNT == p_color[i_left][j_down] ||
                        (color_tmp + p_new[i][j_down]) % COLOR_COUNT == p_color[i][j_down] ||
                        (color_tmp + p_new[i_right][j_down]) % COLOR_COUNT == p_color[i_right][j_down]
                        )
                {
                    p_new[i][j] = 1;
                    p_color[i][j] = color_tmp;
                    p[j * WIDTH + i] = paints[p_color[i][j]];
                }

            }

        for(int i = 0; i < WIDTH; i++)
            for(int j = 0; j < HEIGHT; j++)
                p_new[i][j] = 0;

    }

    private void Fps() {
        frameCounter++;
        long now = SystemClock.uptimeMillis();
        long delta = now - lastFpsCalcUptime;
        if (delta > FPS_CALC_INTERVAL) {
            fps = frameCounter * FPS_CALC_INTERVAL / delta;

            frameCounter = 0;
            lastFpsCalcUptime = now;
        }
    }

}

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Display display = getWindowManager().getDefaultDisplay();

        super.onCreate(savedInstanceState);
        WhirlView view = new WhirlView(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(view);

    }
}
