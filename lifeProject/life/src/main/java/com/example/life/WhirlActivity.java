package com.example.life;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;
import android.graphics.Color;
import android.view.WindowManager;

import java.util.Random;



public class WhirlActivity extends Activity {


    class WhirlView extends SurfaceView implements Runnable {
        Paint p;
        final int WIDTH = 240;
        final int HEIGHT = 320;
        final int num_of_colors = 12;
        int[][] field;
        int cnt = 0, fps = 0;
        int[] colors = {Color.GREEN, Color.YELLOW, Color.BLUE, Color.GRAY, Color.CYAN, Color.RED, Color.WHITE, Color.BLACK, Color.DKGRAY, Color.LTGRAY, Color.MAGENTA,
                Color.TRANSPARENT};
        int[] colored_field;
        Thread thread;
        volatile boolean thread_is_running = true;
        SurfaceHolder surfaceHolder;
        long start_time, end_time;
        Canvas canvas;

        public WhirlView(Context context) {
            super(context);
            p = new Paint();
            field = new int[WIDTH][HEIGHT];
            colored_field = new int[320 * 240];
            Random rand = new Random();
            for (int i = 0; i < WIDTH; ++i)
                for (int j = 0; j < HEIGHT; ++j) {
                    field[i][j] = rand.nextInt(num_of_colors);
                }
            surfaceHolder = getHolder();
            thread = new Thread(this);
            start_time = System.currentTimeMillis();
            thread.start();
        }

        @Override
        public void onDraw(Canvas canvas) {

            int k = 0;
            for (int i = 0; i < HEIGHT; ++i) {
                for (int j = 0; j < WIDTH; ++j) {
                    colored_field[k++] = colors[field[j][i]];
                }
            }
            canvas.scale(3, 4);
            canvas.drawBitmap(colored_field, 0, 240, 0, 0, 240, 320, true, p);
            p.setColor(Color.BLACK);
            canvas.drawRect(0, 0, 50, 15, p);
            p.setColor(Color.YELLOW);
            p.setTextSize(10);
            end_time = System.currentTimeMillis();
            if(cnt >= fps){
                fps = (int)(1000 / (end_time - start_time));

                cnt = 0;
            }
            canvas.drawText("FPS: " + fps, 5, 10, p);
            start_time = end_time;
            cnt++;
        }


        public void recount() {
            int[][] new_step_field = new int[WIDTH][HEIGHT];
            for (int i = 0; i < WIDTH; ++i) {
                for (int j = 0; j < HEIGHT; ++j) {
                    new_step_field[i][j] = field[i][j];
                    if (i < WIDTH - 1 && (field[i][j] + 1) % num_of_colors == field[i + 1][j]) {
                        new_step_field[i][j] = field[i + 1][j];
                    } else if (j < HEIGHT - 1 && (field[i][j] + 1) % num_of_colors == field[i][j + 1]) {
                        new_step_field[i][j] = field[i][j + 1];
                    } else if (i < WIDTH - 1 && j < HEIGHT - 1 && (field[i][j] + 1) % num_of_colors == field[i + 1][j + 1]) {
                        new_step_field[i][j] = field[i + 1][j + 1];
                    } else if (i > 0 && (field[i][j] + 1) % num_of_colors == field[i - 1][j]) {
                        new_step_field[i][j] = field[i - 1][j];
                    } else if (j > 0 && (field[i][j] + 1) % num_of_colors == field[i][j - 1]) {
                        new_step_field[i][j] = field[i][j - 1];
                    } else if (i > 0 && j > 0 && (field[i][j] + 1) % num_of_colors == field[i - 1][j - 1]) {
                        new_step_field[i][j] = field[i - 1][j - 1];
                    } else if (i > 0 && j < HEIGHT - 1 && (field[i][j] + 1) % num_of_colors == field[i - 1][j + 1]) {
                        new_step_field[i][j] = field[i - 1][j + 1];
                    } else if (i < WIDTH - 1 && j > 0 && (field[i][j] + 1) % num_of_colors == field[i + 1][j - 1]) {
                        new_step_field[i][j] = field[i + 1][j - 1];
                    }
                }
            }
            field = new_step_field;
        }

        public void run() {
            while (thread_is_running) {
                if (surfaceHolder.getSurface().isValid()) {
                    canvas = surfaceHolder.lockCanvas();
                    recount();
                    onDraw(canvas);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(new WhirlView(this));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.whirl, menu);
        return true;
    }

}
