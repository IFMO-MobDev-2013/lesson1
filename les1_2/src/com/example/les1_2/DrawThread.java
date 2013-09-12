package com.example.les1_2;

import android.content.res.Resources;
import android.graphics.*;
import android.os.SystemClock;
import android.view.SurfaceHolder;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: kris13
 * Date: 11.09.13
 * Time: 2:39
 * To change this template use File | Settings | File Templates.
 */
public class DrawThread extends Thread {
    private boolean runFlag = false;
    private int w = 240;//MyActivity.screen_w;
    private int h = 320;//MyActivity.screen_h;
    private SurfaceHolder surfaceHolder;
    private int[] pixels = new int[w*h];
    private int[][] table = new int[w][h];
    private int[][] table2 = new int[w][h];
    public int[] red = {255,0,255,255,255,0,0,0,100,200,100,255,255,200,200,255};
    public int[] gre = {255,255,0,255,0,255,0,0,100,200,255,255,100,200,255,200};
    public int[] blu = {255,255,255,0,0,0,255,0,100,200,255,100,255,255,200,200};
    public int kolCol = 16;
    Random random = new Random();
    Color color = new Color();
    boolean flag;
    Paint p = new Paint();

    private long lastFpsCalcUptime;
    private long frameCounter;
    private static final long FPS_CALC_INTERVAL = 1000L;
    private long fps;

    public DrawThread(SurfaceHolder surfaceHolder, Resources resources){
        this.surfaceHolder = surfaceHolder;

        int[] rand = new int[w * h];
        for (int i = 0;i < w * h;i++){
            rand[i] = random.nextInt(923421);
        }
        int kol=0;
        for (int i = 0;i < w;i++){
            for (int j = 0;j < h;j++){
                table2[i][j] = rand[kol] % kolCol;
                kol++;
                table[i][j] = table2[i][j];
                //sceen.setPixel(i,j,color.rgb(red[table[i][j]], gre[table[i][j]], blu[table[i][j]]));
            }
        }
        for (int i = 0;i<h;i++){
            for (int j = 0;j<w;j++){
                table[j][i] = table2[j][i];
                pixels[i*w+j]=color.rgb(red[table2[j][i]], gre[table2[j][i]], blu[table2[j][i]]);
            }
        }
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (runFlag) {
            measureFps();

            for (int i = 0;i<w;i++){
                for (int j = 0;j<h;j++){
                    flag = true;
                    for (int x = -1;x <= 1 && flag;x++)
                        for (int y = -1;y <= 1 && flag;y++)
                            if ((table[i][j] + 1)%kolCol == table[(i + x + w) % w][(j + y + h) % h]){
                                table2[i][j] = (table[i][j] + 1)%kolCol;
                                flag = false;
                            }
                    //sceen.setPixel(i,j,color.rgb(red[table2[i][j]], gre[table2[i][j]], blu[table2[i][j]]));
                }
            }

            for (int i = 0;i<h;i++){
                for (int j = 0;j<w;j++){
                    table[j][i] = table2[j][i];
                    pixels[i*w+j]=color.rgb(red[table2[j][i]], gre[table2[j][i]], blu[table2[j][i]]);
                }
            }

            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                canvas.scale((float)MyActivity.screen_w/w,(float)MyActivity.screen_h/h);
                synchronized (surfaceHolder) {
                    canvas.drawColor(Color.BLACK);
                    canvas.drawBitmap(pixels, 0, w, 0, 0, w, h, false, null);
                    p.setColor(Color.BLACK);
                    p.setTextSize(20);
                    canvas.drawText("fps=" + fps, 5, 30, p);
                }
            }
            finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void measureFps() {
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
