package ru.georgeee.android.gwhirl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;

import java.util.Random;

public class WhirlManager {
    public static final int SQUARE_ALGORITHM = 0;
    public static final int ROUND_ALGORITHM = 1;

    private static final long FPS_CALC_INTERVAL = 1000L;
    public boolean showFPS = true;
    public boolean doScale = true;
    public int algorithm = SQUARE_ALGORITHM;
    protected int matrixHeight;
    protected int matrixWidth;
    protected int colorPlaceCount;
    protected int[] colors;
    protected int[][] colorMap;
    protected int[][] colorMapCopy;
    protected int[] compiledColors;
    protected Paint paint;
    protected long lastFpsCalcUptime;
    protected long frameCounter;
    protected float fps;
    protected float avgFps = 0;
    protected int measureCount = 0;

    public WhirlManager(int colorCount, int width, int height) {
        Random rand = new Random(System.currentTimeMillis());
        colorPlaceCount = height * width;
        this.matrixWidth = width;
        this.matrixHeight = height;
        colors = new int[colorCount];
        for (int i = 0; i < colors.length; ++i) {
            colors[i] = rand.nextInt(0xffffff + 1);
        }
        colorMap = new int[matrixHeight][matrixWidth];
        colorMapCopy = new int[matrixHeight][matrixWidth];
        compiledColors = new int[colorPlaceCount];
        for (int i = 0; i < matrixHeight; ++i) {
            for (int j = 0; j < matrixWidth; ++j) {
                colorMap[i][j] = rand.nextInt(colors.length);
            }
        }
        paint = new Paint();
        lastFpsCalcUptime = SystemClock.uptimeMillis();
        frameCounter = 0;
    }

    protected void stepColorsRoundAlgo() {
        int i, j, i0, j0, j2, i5, next_color;
        i = j = 0;
        for (int _colorPlace = 0; _colorPlace < colorPlaceCount; ++_colorPlace, ++j) {
            if (j == matrixWidth) {
                j = 0;
                ++i;
            }
                /*
                0 1 2
                3 x 4
                5 6 7
                 */
            i0 = i == 0 ? matrixHeight - 1 : i - 1;
            j0 = j == 0 ? matrixWidth - 1 : j - 1;
            j2 = j == matrixWidth - 1 ? 0 : j + 1;
            i5 = i == matrixHeight - 1 ? 0 : i + 1;
            next_color = colorMap[i][j] + 1;
            if (next_color == colors.length) next_color = 0;
            if (colorMap[i0][j0] == next_color || colorMap[i0][j] == next_color
                    || colorMap[i0][j2] == next_color || colorMap[i][j0] == next_color
                    || colorMap[i][j2] == next_color || colorMap[i5][j0] == next_color
                    || colorMap[i5][j] == next_color || colorMap[i5][j2] == next_color) {
                colorMapCopy[i][j] = next_color;
                compiledColors[_colorPlace] = colors[next_color];
            }
        }
        int[][] x = colorMap;
        colorMap = colorMapCopy;
        colorMapCopy = x;
    }

    protected void stepColorsSquareAlgo() {
        int i, j, i0, j0, j2, i5, next_color, color;
        i = j = 0;
        for (int _colorPlace = 0; _colorPlace < colorPlaceCount; ++_colorPlace, ++j) {
            if (j == matrixWidth) {
                j = 0;
                ++i;
            }
                /*
                0 1 2
                3 x 4
                5 6 7
                 */
            i0 = i == 0 ? matrixHeight - 1 : i - 1;
            j0 = j == 0 ? matrixWidth - 1 : j - 1;
            j2 = j == matrixWidth - 1 ? 0 : j + 1;
            i5 = i == matrixHeight - 1 ? 0 : i + 1;
            color = colorMap[i][j];
            next_color = color + 1;
            if (next_color == colors.length) next_color = 0;
            if (colorMap[i0][j0] == next_color) {
                color = next_color;
                if (++next_color == colors.length) next_color = 0;
            }
            if (colorMap[i0][j] == next_color) {
                color = next_color;
                if (++next_color == colors.length) next_color = 0;
            }
            if (colorMap[i0][j2] == next_color) {
                color = next_color;
                if (++next_color == colors.length) next_color = 0;
            }
            if (colorMap[i][j0] == next_color) {
                color = next_color;
                if (++next_color == colors.length) next_color = 0;
            }
            if (colorMap[i][j2] == next_color) {
                color = next_color;
                if (++next_color == colors.length) next_color = 0;
            }
            if (colorMap[i5][j0] == next_color) {
                color = next_color;
                if (++next_color == colors.length) next_color = 0;
            }
            if (colorMap[i5][j] == next_color) {
                color = next_color;
                if (++next_color == colors.length) next_color = 0;
            }
            if (colorMap[i5][j2] == next_color) {
                color = next_color;
            }
            colorMapCopy[i][j] = color;
            compiledColors[_colorPlace] = colors[color];
        }
        int[][] x = colorMap;
        colorMap = colorMapCopy;
        colorMapCopy = x;
    }

    protected void measureFps() {
        frameCounter++;
        long now = SystemClock.uptimeMillis();
        long delta = now - lastFpsCalcUptime;
        if (delta > FPS_CALC_INTERVAL) {
            fps = (float) frameCounter * FPS_CALC_INTERVAL / delta;
            avgFps = (avgFps * measureCount + fps) / (++measureCount);
            frameCounter = 0;
            lastFpsCalcUptime = now;
            Log.i("GWhirl.FPS", "Fps=" + fps + " Average_fps=" + avgFps);
        }
    }

    synchronized public void drawWhirl(Canvas canvas) {
        measureFps();
        if(algorithm == SQUARE_ALGORITHM)
            stepColorsSquareAlgo();
        else if(algorithm == ROUND_ALGORITHM)
            stepColorsRoundAlgo();
        int cWidth = canvas.getWidth();
        int cHeight = canvas.getHeight();
        float sX = (float) cWidth / matrixWidth;
        float sY = (float) cHeight / matrixHeight;
        if(doScale) canvas.scale(sX, sY);
        canvas.drawBitmap(compiledColors, 0, matrixWidth, 0, 0, matrixWidth, matrixHeight, false, paint);
        if(doScale) canvas.scale((float) 1 / sX, (float) 1 / sY);
        if (showFPS) {
            paint.setTextSize(30);
            paint.setARGB(127, 0, 0, 0);
            canvas.drawRect(0, 0, 300, 100, paint);
            paint.setARGB(255, 255, 255, 255);
            canvas.drawText("fps=" + (float) Math.round(1000 * fps) / 1000, 10, 40, paint);
            canvas.drawText("average fps=" + (float) Math.round(1000 * avgFps) / 1000, 10, 85, paint);
        }
    }
}
