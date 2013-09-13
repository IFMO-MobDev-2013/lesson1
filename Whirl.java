package ru.ifmo.mobdev.whirl;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.*;
import android.os.Bundle;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: asus
 * Date: 06.09.13
 * Time: 14:05
 * To change this template use File | Settings | File Templates.
 */

class WhirlView extends View {
    private int n = 240;
    private int m = 320;
    private int[][] colorNet = new int[n][m];
    private int[][] colorNetClone = new int[n][m];
    private int[] xShift = new int[]{-1, 0, 1, 1, 1, 0, -1, -1, -1, 0, 1, 1, 1, 0, -1, -1};
    private int[] yShift = new int[]{-1, -1, -1, 0, 1, 1, 1, 0, -1, -1, -1, 0, 1, 1, 1, 0};
    private int[] colorPalette = new int[16];
    private long lastFrameTime = System.currentTimeMillis();
    private int framesShowed = 0;
    private int fps;
    private Bitmap Net;
    private boolean firstFrame = true;
    private Rect canvasRect;
    private Paint indicatorBackGround = new Paint();

    public WhirlView(Context context){
        super(context);

        for (int i = 0; i < n; i++){
            for (int j = 0; j < m; j++){
                colorNet[i][j] = (int) (Math.random() * colorPalette.length - 0.001);
            }
        }

        colorPalette[0] = 0xFF000000;
        colorPalette[1] = 0xFF0000FF;
        colorPalette[2] = 0xFF00FF00;
        colorPalette[3] = 0xFF00FFFF;
        colorPalette[4] = 0xFFFF0000;
        colorPalette[5] = 0xFFFF00FF;
        colorPalette[6] = 0xFFFF00FF;
        colorPalette[7] = 0xFFFFFF00;
        colorPalette[8] = 0xFFFFFFFF;
        colorPalette[9] = 0xFFFF00C0;
        colorPalette[10] = 0xFFC000FF;
        colorPalette[11] = 0xFF40D0E0;
        colorPalette[12] = 0xFF8080C0;
        colorPalette[13] = 0xFFC0FF00;
        colorPalette[14] = 0xFFB09050;
        colorPalette[15] = 0xFF8050A8;

        Net = Bitmap.createBitmap(n, m,  Bitmap.Config.ARGB_8888);
    }

    private void checkNeighbours(int left, int right, int top, int down, int startShift, int endShift){
        for (int i = left; i <= right; i++){
            for (int j = top; j <= down; j++){
                boolean found = false;
                for (int k = startShift; k <= endShift; k++){
                    if (colorNet[i + xShift[k]][j + yShift[k]] == colorNet[i][j] + 1 || colorNet[i + xShift[k]][j + yShift[k]] == colorNet[i][j] + 1 - colorPalette.length){
                        found = true;
                    }
                }
                if (found){
                    if (colorNet[i][j] + 1 == colorPalette.length){
                        colorNetClone[i][j] = 0;
                    } else {
                        colorNetClone[i][j] = colorNet[i][j] + 1;
                    }
                } else {
                    colorNetClone[i][j] = colorNet[i][j];
                }
            }
        }

    }


    @Override
    public void onDraw(Canvas canvas) {
        //  0  1  2
        //  7  *  3
        //  6  5  4

        checkNeighbours(1, n - 2, 1, m - 2, 0, 7);

        checkNeighbours(0, 0, 0, 0, 3, 5);
        checkNeighbours(1, n - 2, 0, 0, 3, 7);

        checkNeighbours(n - 1, n - 1, 0, 0, 5, 7);
        checkNeighbours(n - 1, n - 1, 1, m - 2, 5, 9);

        checkNeighbours(n - 1, n - 1, m - 1, m - 1, 7, 9);
        checkNeighbours(1, n - 2, m - 1, m - 1, 7, 11);

        checkNeighbours(0, 0, m - 1, m - 1, 9, 11);
        checkNeighbours(0, 0, 1, m - 2, 9, 13);


        if (firstFrame){
            firstFrame = false;
            canvasRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        }

        for (int i = 0; i < n; i++){
            for (int j = 0; j < m; j++){
                colorNet[i][j] = colorNetClone[i][j];
                Net.setPixel(i, j, colorPalette[colorNet[i][j]]);
            }
        }


        canvas.drawBitmap(Net, null, canvasRect, new Paint());


        framesShowed++;
            long curTime = System.currentTimeMillis();
            if (curTime - lastFrameTime >= 1000){
            fps = framesShowed;
            framesShowed = 0;
            lastFrameTime += (curTime - lastFrameTime) - (curTime - lastFrameTime) % 1000;
        }


        indicatorBackGround.setARGB(159, 255, 255, 255);
        canvas.drawRect(canvas.getWidth() - 45, canvas.getHeight() - 20, canvas.getWidth(), canvas.getHeight(), indicatorBackGround);
        canvas.drawText(fps + " fps.", canvas.getWidth() - 40, canvas.getHeight() - 6, new Paint());

        invalidate();

    }
}


public class WhirlActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new WhirlView(this));
    }
}
