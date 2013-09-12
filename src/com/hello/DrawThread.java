package com.hello;
/*
    using Threads
    Manual : http://habrahabr.ru/post/126316/
 */

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawThread extends Thread{
    private boolean runFlag = false;
    private SurfaceHolder surfaceHolder;
    private WhirlMain currentWhirl;
    public DrawThread(SurfaceHolder surfaceHolder, WhirlMain currentWhirl) {
        this.surfaceHolder = surfaceHolder;
        this.currentWhirl = currentWhirl;
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    public void run() {
        Canvas canvas;
        while (runFlag) {
            canvas = null;
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    currentWhirl.draw(canvas);
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
