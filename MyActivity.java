package com.example.Colorswirl;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.*;
import android.view.View;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 08.09.13
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */

public class MyActivity extends Activity {


    class MyView extends View {

        private static final int colorsNum = 20;
        private static final int rows = 320;
        private static final int columns = 240;
        private static final int frames = 20;
        private Paint textPaint;
        private int[][][] cellColors;
        private int currentState;
        private int currentFrame;
        private int currentFPS;
        private long lastTime;
        private int[] colors;
        private int[] bitmap;

        public MyView(Context context) {
            super(context);
            bitmap = new int[rows * columns];
            colors = new int[colorsNum];
            for (int i = 0; i < colorsNum; i++) {
                float[] hsv = new float[3];
                hsv[0] = i * 360.0f / colorsNum;
                hsv[1] = 0.5f;
                hsv[2] = 1f;
                colors[i] = Color.HSVToColor(hsv);
            }
            textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(30);
            cellColors = new int[2][rows + 2][columns + 2];
            Random rand = new Random();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    cellColors[0][i + 1][j + 1] = rand.nextInt(colorsNum);
                    bitmap[i * columns + j] = colors[cellColors[0][i + 1][j + 1]];
                }
            }
            for (int i = 0; i < rows + 2; i++) {
                cellColors[0][i][0] = cellColors[0][i][columns];
                cellColors[0][i][columns + 1] = cellColors[0][i][1];
            }
            for (int j = 0; j < columns + 2; j++) {
                cellColors[0][0][j] = cellColors[0][rows][j];
                cellColors[0][rows + 1][j] = cellColors[0][1][j];
            }
            currentState = 0;
            currentFrame = 1;
            currentFPS = 0;
            lastTime = getDrawingTime();
        }

        protected void recalculateColors(){
            final int nextState = 1 - currentState;
            int nextColor;
            int i = 0, j = 0;
            for (i = 1; i < rows + 1; i++) {
                for (j = 1; j < columns + 1; j++) {
                    nextColor = (cellColors[currentState][i][j] + 1) % colorsNum;
                    if ((cellColors[currentState][i - 1][j - 1] == nextColor)
                            || (cellColors[currentState][i - 1][j] == nextColor)
                            || (cellColors[currentState][i - 1][j + 1] == nextColor)
                            || (cellColors[currentState][i][j - 1] == nextColor)
                            || (cellColors[currentState][i][j + 1] == nextColor)
                            || (cellColors[currentState][i + 1][j - 1] == nextColor)
                            || (cellColors[currentState][i + 1][j] == nextColor)
                            || (cellColors[currentState][i + 1][j + 1] == nextColor)) {
                        cellColors[nextState][i][j] = nextColor;
                        bitmap[(i - 1) * columns + j - 1] = colors[cellColors[nextState][i][j]];
                    }
                    else
                    {
                        cellColors[nextState][i][j] = cellColors[currentState][i][j];
                    }
                }
            }
            for (i = 0; i < rows + 2; i++) {
                cellColors[nextState][i][0] = cellColors[nextState][i][columns];
                cellColors[nextState][i][columns + 1] = cellColors[nextState][i][1];
            }
            for (j = 0; j < columns + 2; j++) {
                cellColors[nextState][0][j] = cellColors[nextState][rows][j];
                cellColors[nextState][rows + 1][j] = cellColors[nextState][1][j];
            }
            currentState = nextState;
        }

        private void recalculateFPS() {
            currentFrame++;
            if (currentFrame % frames == 0) {
                long delay = getDrawingTime() - lastTime;
                currentFPS = (int)(1000 * frames / delay);
                lastTime += delay;
            }
        }

        @Override
        public void onDraw(Canvas canvas)
        {
            recalculateColors();
            canvas.scale(canvas.getWidth() * 1f / columns, canvas.getHeight() * 1f / rows);
            canvas.drawBitmap(bitmap, 0, columns, 0, 0, columns, rows, false, null);
            recalculateFPS();
            canvas.drawText(currentFPS + " FPS", 30, 30, textPaint);
            invalidate();
        }
    }

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

}
