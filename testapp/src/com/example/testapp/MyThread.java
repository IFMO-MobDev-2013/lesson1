package com.example.testapp;


import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MyThread extends Thread {
    private SurfaceHolder sh;
    private Canvas canvas;
    private MySurfaceView sv;
    private boolean run = false;
    public MyThread(SurfaceHolder _holder, MySurfaceView _sv) {
        sv = _sv;
        sh = _holder;
    }

    public void setRunnable(boolean _run) {

        run = _run;
    }
    @Override
    public void run() {

        while(run) {

            canvas = null;

            try {

                canvas = sh.lockCanvas(null);

                synchronized(sh) {

                    sv.doDraw(canvas);
                }

            } finally {

                if(canvas != null) {

                    sh.unlockCanvasAndPost(canvas);
                }

            }

        }
    }

    public Canvas getCanvas() {

        if(canvas != null) {

            return canvas;

        } else {

            return null;
        }
    }
}

