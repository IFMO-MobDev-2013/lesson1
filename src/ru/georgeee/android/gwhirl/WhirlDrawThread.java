package ru.georgeee.android.gwhirl;

import android.graphics.Canvas;
import android.view.SurfaceHolder;


public class WhirlDrawThread extends Thread {
    private boolean runFlag = false;
    private SurfaceHolder surfaceHolder;
    private WhirlManager whirlManager;

    public WhirlDrawThread(WhirlManager whirlManager, SurfaceHolder surfaceHolder) {
        this.whirlManager = whirlManager;
        this.surfaceHolder = surfaceHolder;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public void setRunning(boolean runFlag) {
        this.runFlag = runFlag;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (runFlag) {
            canvas = null;
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    if (canvas != null) whirlManager.drawWhirl(canvas);
                }
            } finally {
                if (canvas != null) {
                    // отрисовка выполнена. выводим результат на экран
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
