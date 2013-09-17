package ru.ifmo.ctddev.igushkin.cells;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.util.Random;

public class MainActivity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(ru.ifmo.ctddev.igushkin.cells.R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == ru.ifmo.ctddev.igushkin.cells.R.id.mnuNoise) {
            randomSeed = !randomSeed;
            item.setTitle(randomSeed ? R.string.mnuNoiseTitleDisable : R.string.mnuNoiseTitleEnable);
        }
        return super.onOptionsItemSelected(item);
    }

    int[] colors = new int[16];

    {
        for (int i = 0; i < colors.length / 2; i++)
            colors[i] = Color.argb(255, 128 - (255 / colors.length) * i, 128 - (255 / colors.length) * i, 128 - (255 / colors.length) * i);
        for (int i = 0; i < colors.length / 2; i++)
            colors[colors.length - 1 - i] = Color.argb(255, 128 - (255 / colors.length) * i, 128 - (255 / colors.length) * i, 128 - (255 / colors.length) * i);
    }

    private int[][] map = new int[322][242];
    boolean randomSeed = false;

    Random rng = new Random();

    {
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++)
                map[i][j] = rng.nextInt(colors.length);
    }

    CellView cv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        cv = new CellView(this, colors);
        setContentView(cv);
    }

    class CellView extends SurfaceView implements SurfaceHolder.Callback {

        private final int[] colors;

        UpdateDispatcher ud;

        public CellView(Context context, int[] colors) {
            super(context);
            getHolder().addCallback(this);
            this.colors = colors;
            new Thread(new FPSUpdater()).start();
        }

        int frames = 0;
        int fps = 0;

        Paint p = new Paint();

        {
            p.setColor(Color.WHITE);
            p.setTextSize(25);
        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            ud = new UpdateDispatcher();
            new Thread(ud).start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            ud.needsStop = true;
        }

        private class UpdateDispatcher implements Runnable {
            boolean needsStop = false;
            int[] bmp = new int[map.length * map[0].length];

            SurfaceHolder sh = CellView.this.getHolder();

            boolean[][] inc;

            Canvas c;

            void start() {
                while (!needsStop) {
                    for (int y = 1; y < map.length - 1; y++)
                        for (int x = 1; x < map[0].length - 1; x++) {
                            int next = map[y][x] == colors.length - 1 ? 0 : map[y][x] + 1;
                            inc[y][x] = (map[y - 1][x - 1] == next ||
                                         map[y - 1][x] == next ||
                                         map[y - 1][x + 1] == next ||
                                         map[y][x - 1] == next ||
                                         map[y][x] == next ||
                                         map[y][x + 1] == next ||
                                         map[y + 1][x - 1] == next ||
                                         map[y + 1][x] == next ||
                                         map[y + 1][x + 1] == next);
                        }
                    synchronized (map) {
                        for (int y = 1; y < map.length - 1; y++)
                            for (int x = 0; x < map[0].length - 1; x++) {
                                if (inc[y][x]) {
                                    map[y][x]++;
                                    if (map[y][x] >= colors.length)
                                        map[y][x] = 0;
                                }
                            }
                    }
                    for (int y_offset = map[0].length, y = 1; y < map.length; y_offset += map[0].length, y++)
                        for (int x = 1; x < map[0].length - 1; x++)
                            bmp[y_offset + x] = colors[map[y][x]];
                    if (randomSeed) doRandomSeed();
                    frames++;

                    for (int i = 0; i < map[0].length; i++) {
                        map[0][i] = map[map.length - 2][i];
                        map[map.length - 1][i] = map[1][i];
                    }
                    for (int i = 0; i < map.length; i++) {
                        map[i][0] = map[i][map[0].length - 2];
                        map[i][map[0].length - 1] = map[i][1];
                    }
                    map[0][0] = map[map.length - 2][map[0].length - 2];
                    map[0][map[0].length - 1] = map[map.length - 2][1];
                    map[map.length - 1][0] = map[1][map[0].length - 2];
                    map[map.length - 1][map[0].length - 1] = map[0][0];

                    try {
                        c = sh.lockCanvas();
                        if (c != null)
                            synchronized (sh) {
                                c.scale(scaleX, scaleY);
                                c.drawBitmap(bmp, 0, map[0].length, 0, 0, map[0].length, map.length, false, p);
                                c.scale(1 / scaleX, 1 / scaleY);
                                c.drawText(Integer.toString(fps) + " FPS", 35, 35, p);
                            }
                    } finally {
                        if (c != null) {
                            sh.unlockCanvasAndPost(c);
                        }
                    }

                }
            }

            void doRandomSeed() {
                for (int i = 0; i < 100; i++)
                    map[MainActivity.this.rng.nextInt(map.length)][MainActivity.this.rng.nextInt(map[0].length)] = 0;
            }

            float scaleX, scaleY;

            @Override
            public void run() {
                inc = new boolean[map.length][map[0].length];
                scaleX = (float) getWidth() / map[0].length;
                scaleY = (float) getHeight() / map.length;
                start();
            }
        }

        private class FPSUpdater implements Runnable {
            boolean needsStop = false;

            @Override
            public void run() {
                try {
                    while (!needsStop) {
                        synchronized (this) {
                            this.wait(1000);
                        }
                        CellView.this.fps = CellView.this.frames;
                        CellView.this.frames = 0;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
