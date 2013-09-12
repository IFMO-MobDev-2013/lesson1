package ru.skipor.Whirl.WhirlView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Random;

import ru.skipor.Whirl.WhirlEngine.WhirlEngine;
import ru.skipor.Whirl.WhirlEngine.Exception.WhrilEngineWrongInputFormatException;

/**
 * Created by vladimirskipor on 9/11/13.
 */
class WhirlView extends View {
    final static int COLORS_NUM = 16;
    final static int HEIGHT = 320;
    final static int WIDTH = 240;
    final float scaleXProportion;
    final float scaleYProportion;

    final long MILI_SEC_INFO_UPDATE = 2000;
    final static int infoXOffset = 20;
    final static int infoYOffset = 0;
    long lastInfoUpdate;
    float fps;
    float timeForDraw;
    float timeForRender;
    float timeForRefresh;
    float timeForOther;
    long framesAfterInfoUpdate;
    long spentTimeForDraw;
    long spentTimeForRender;
    long spentTimeForRefresh;
    long refreshBegin;

    static final int infoTextColor = Color.WHITE;
    static final int fpsTextSize = 20;
    static final int otherInfoTextSize = 10;
    final Paint fpsPaint;
    final Paint otherInfoPaint;


    final Random random;

    final WhirlEngine whirlEngine;


    public WhirlView(Context context) throws WhrilEngineWrongInputFormatException {
        super(context);
        random = new Random();
        refreshBegin = timeMillis();

        fpsPaint = new Paint();
        fpsPaint.setColor(infoTextColor);
        fpsPaint.setTextSize(fpsTextSize);
        otherInfoPaint = new Paint();
        otherInfoPaint.setColor(infoTextColor);
        otherInfoPaint.setTextSize(otherInfoTextSize);


        whirlEngine = new WhirlEngine(HEIGHT, WIDTH, COLORS_NUM);
        whirlEngine.setRandomState();


        lastInfoUpdate = timeMillis();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        scaleXProportion = 1f * metrics.widthPixels / WIDTH;
        scaleYProportion = 1f * metrics.heightPixels / HEIGHT;


    }


    @Override
    public void onDraw(Canvas canvas) {
        spentTimeForRefresh += timeMillis() - refreshBegin;
        canvas.scale(scaleXProportion, scaleYProportion);



        final long renderBegin = timeMillis();
        whirlEngine.renderNextState();
        spentTimeForRender += timeMillis() - renderBegin;

        final long drawBegin = timeMillis();
        canvas.drawBitmap(whirlEngine.getCurrentState(), 0, WIDTH, 0, 0, WIDTH, HEIGHT, false, null);
        drawInfo(canvas);

        spentTimeForDraw += timeMillis() - drawBegin;


        refreshBegin = timeMillis();
        invalidate();

    }

    private long timeMillis() {
        return SystemClock.currentThreadTimeMillis();
    }


    private void drawInfo(Canvas canvas) {
        framesAfterInfoUpdate++;
        final long deltaTime = timeMillis() - lastInfoUpdate;
        if (deltaTime >= MILI_SEC_INFO_UPDATE || fps == 0) { //fps = 0 after launch
            fps = (framesAfterInfoUpdate * 1000f) / deltaTime;
            timeForDraw = (spentTimeForDraw / 1000f) / framesAfterInfoUpdate;
            timeForRender = (spentTimeForRender / 1000f) / framesAfterInfoUpdate;
            timeForRefresh = (spentTimeForRefresh / 1000f) / framesAfterInfoUpdate;
            timeForOther = ((deltaTime / 1000f) / framesAfterInfoUpdate) - (timeForDraw + timeForRender + timeForRefresh);
            framesAfterInfoUpdate = 0;
            spentTimeForDraw = 0;
            spentTimeForRender = 0;
            spentTimeForRefresh = 0;
            lastInfoUpdate = timeMillis();
        }
        canvas.drawText("FPS: " + Float.toString(fps), infoXOffset, infoYOffset + fpsTextSize, fpsPaint);
        drawMultiline("Render: " + Float.toString(timeForRender)
                + "\nRedraw: " + Float.toString(timeForDraw)
                + "\nRefresh: " + Float.toString(timeForRefresh)
                + "\nOther: " + timeForOther, infoXOffset, infoYOffset + fpsTextSize * 2.5f, otherInfoPaint, canvas);
    }


    public void drawMultiline(String str, float x, float y, Paint paint, Canvas canvas) {
        for (String line : str.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += -paint.ascent() + paint.descent();
        }
    }
}
