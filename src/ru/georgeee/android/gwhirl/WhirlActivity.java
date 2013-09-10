package ru.georgeee.android.gwhirl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class WhirlActivity extends Activity {
    public static final int THREAD_COUNT = 2 * Runtime.getRuntime().availableProcessors();
    private WhirlSurfaceView mainView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        boolean showFPS = getIntent().getExtras().getBoolean(MainActivity.SHOW_FPS_KEY);
        boolean doScale = getIntent().getExtras().getBoolean(MainActivity.DO_SCALE_KEY);
        int height = getIntent().getExtras().getInt(MainActivity.HEIGHT_KEY);
        int width = getIntent().getExtras().getInt(MainActivity.WIDTH_KEY);
        int colorCount = getIntent().getExtras().getInt(MainActivity.COLOR_COUNT_KEY);
        int algorithm = getIntent().getExtras().getInt(MainActivity.ALGORITHM_KEY);
        WhirlManager whirlManager = new WhirlManager(colorCount, width, height);
        whirlManager.algorithm = algorithm;
        whirlManager.doScale = doScale;
        whirlManager.showFPS = showFPS;
        mainView = new WhirlSurfaceView(this, whirlManager, THREAD_COUNT);
        setContentView(mainView);
    }

    public class WhirlSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
        public final int threadCount;
        private WhirlDrawThread[] drawThreads;
        private WhirlManager whirlManager;

        public WhirlSurfaceView(Context context, WhirlManager whirlManager, int threadCount) {
            super(context);
            this.whirlManager = whirlManager;
            this.threadCount = threadCount;
            getHolder().addCallback(this);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            drawThreads = new WhirlDrawThread[threadCount];
            for (int i = 0; i < threadCount; ++i) {
                WhirlDrawThread drawThread = drawThreads[i] = new WhirlDrawThread(whirlManager, holder);
                drawThread.setRunning(true);
                drawThread.start();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            for (int i = 0; i < threadCount; ++i) drawThreads[i].setRunning(false);
            while (retry) {
                try {
                    for (int i = 0; i < threadCount; ++i) drawThreads[i].join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
