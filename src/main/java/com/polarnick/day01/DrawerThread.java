package com.polarnick.day01;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DrawerThread extends Thread {

    private final SurfaceHolder surfaceHolder;

    private final static int THREAD_COUNT = 2 * Runtime.getRuntime().availableProcessors();
    private final static int FIELD_UPDATER_COUNT = THREAD_COUNT - 1;

    //Default:     240 x 320
    //Xperia Sola: 480 x 854
    //Galaxy S3:   720 x 1280
    private final static int WIDTH = HelloAndroidActivity.getScreenWidth();
    private final static int HEIGHT = HelloAndroidActivity.getScreenHeight();

    private final static int COLOUR_COUNT = 16;

    private static final int UPDATE_FPS_AFTER_MS = 239;
    private static final int MS_IN_SECOND = 1000;
    private static final Paint BLACK_TEXT = new Paint();

    private static final float TEXT_SIZE = 30f;
    private static final int TEXT_ALPHA = 255;

    private static final int TEXT_OFFSET_X = 15;
    private static final int TEXT_OFFSET_Y = 35;

    private final static Random random = new Random();

    private volatile boolean runFlag = false;
    private final Profiler<WhirlViewFunctions> profiler = new Profiler<WhirlViewFunctions>(this.getClass());

    private final ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);
    private final List<FieldPeriodUpdater> updaters = new ArrayList<FieldPeriodUpdater>(FIELD_UPDATER_COUNT);

    private final PeriodHolder periodHolder = new PeriodHolder(COLOUR_COUNT, WIDTH, HEIGHT);

    private int[] field = new int[WIDTH * HEIGHT];
    private int[] tmp = new int[WIDTH * HEIGHT];

    private int[] fieldNext = new int[WIDTH * HEIGHT];
    private int[] tmpNext = new int[WIDTH * HEIGHT];

    public DrawerThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        initFieldUpdaters();
        initRandomField();
    }

    private void initFieldUpdaters() {
        boolean cornersWas = false;
        for (int i = 0; i < FIELD_UPDATER_COUNT; i++) {
            updaters.add(new FieldPeriodUpdater(i, (i * HEIGHT / FIELD_UPDATER_COUNT), ((i + 1) * HEIGHT / FIELD_UPDATER_COUNT),
                    !cornersWas, periodHolder, WIDTH, HEIGHT, field, tmp, fieldNext, tmpNext));
            if (!cornersWas) {
                cornersWas = true;
            }
        }
    }

    private void initRandomField() {
        List<Integer> colors = new ArrayList<Integer>(COLOUR_COUNT);
        for (int i = 0; i < COLOUR_COUNT; i++) {
            int color = random.nextInt(Integer.MAX_VALUE);
            while (colors.contains(color)) {
                color = random.nextInt(Integer.MAX_VALUE);
            }
            colors.add(color);
        }
        for (int index = 0; index < field.length; index++) {
            int ind = random.nextInt(COLOUR_COUNT);
            field[index] = colors.get(ind);
            fieldNext[index] = colors.get((ind + 1) % COLOUR_COUNT);
        }
    }

    @Override
    public void run() {
        while (runFlag) {
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    if (canvas != null) {
                        onDraw(canvas);
                    }
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            if (profiler.isToLogNextStep()) {
                Log.d(this.getClass().getName(), profiler.getAverangeLog());
            }
            sleepNano(1);
        }
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    public void onDraw(final Canvas canvas) {
        profiler.in(WhirlViewFunctions.onDraw);
        if (periodHolder.getState() == PeriodHolder.State.NOT_IN_PERIOD) {
            update(canvas);
            periodHolder.updateState();
        } else if (periodHolder.getState() == PeriodHolder.State.PERIOD_CALCULATES) {
            tmp = periodHolder.getNextToRender();
            for (FieldPeriodUpdater updater : updaters) {
                updater.setNewFieldWriteAt(periodHolder.getNextToRender());
            }
            update(canvas);
            periodHolder.updateState();
            if (periodHolder.getState() == PeriodHolder.State.PERIOD_CALCULATED) {
                freeMemory();
            }
        } else if (periodHolder.getState() == PeriodHolder.State.PERIOD_CALCULATED) {
            renderFieldBitmap(periodHolder.getCurFrame(), canvas);
            periodHolder.updateState();
        }
        profiler.out(WhirlViewFunctions.onDraw);
    }

    private void freeMemory() {
        updaters.clear();
        field = null;
        fieldNext = null;
        tmp = null;
        tmpNext = null;
        Runtime.getRuntime().gc();
    }

    private void update(final Canvas canvas) {
        final CountDownLatch counter = new CountDownLatch(THREAD_COUNT);
        periodHolder.setStillPeriodStarted(true);
        for (FieldPeriodUpdater updater : updaters) {
            updater.setCounter(counter);
            pool.execute(updater);
        }

        pool.execute(new Runnable() {
            @Override
            public void run() {
                renderFieldBitmap(field, canvas);
                counter.countDown();
            }
        });

        try {
            counter.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        swapFields();
    }

    private void sleepNano(int time) {
        try {
            Thread.sleep(0L, time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private long fps;

    private long frameStart = System.currentTimeMillis();
    private long frames;

    static {
        BLACK_TEXT.setARGB(TEXT_ALPHA, 0, 0, 0);
        BLACK_TEXT.setTextSize(TEXT_SIZE);
    }

    private void renderFieldBitmap(int[] colors, Canvas canvas) {
        profiler.in(WhirlViewFunctions.renderFieldBitmap);
        canvas.scale(1f * HelloAndroidActivity.getScreenWidth() / WIDTH, 1f * HelloAndroidActivity.getScreenHeight() / HEIGHT);
        canvas.drawBitmap(colors, 0, WIDTH, 0, 0, WIDTH, HEIGHT, false, null);
        final long delta = System.currentTimeMillis() - frameStart;
        frames++;
        if (delta > UPDATE_FPS_AFTER_MS) {
            fps = (frames * MS_IN_SECOND / delta);
            frameStart = System.currentTimeMillis();
            frames = 0;
        }
        canvas.drawText(fps + " FPS", TEXT_OFFSET_X, TEXT_OFFSET_Y, BLACK_TEXT);
        profiler.out(WhirlViewFunctions.renderFieldBitmap);
    }

    private void swapFields() {
        int[] tmp2 = tmp;
        tmp = field;
        field = tmp2;
        tmp2 = tmpNext;
        tmpNext = fieldNext;
        fieldNext = tmp2;
    }

    private enum WhirlViewFunctions {

        onDraw, updateField, getNewColor, updateField2, renderFieldBitmap, updateField3, updateField4, renderField

    }

}
