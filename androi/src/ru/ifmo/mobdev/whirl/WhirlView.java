package ru.ifmo.mobdev.whirl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: slavian
 * Date: 10.09.13
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 */
public class WhirlView extends View {

    private final int COLORS_NUMBER = 13;
    private final int WIDTH = 240;
    private final int HEIGHT = 320;
    private final int NUM = WIDTH * HEIGHT;
    private int[] colors = new int[COLORS_NUMBER];
    private int[] toDraw = new int[NUM];
    private int[][] field = new int[HEIGHT][WIDTH];
    private int[][] fieldCopy = new int[HEIGHT][WIDTH];
    private long lastFpsCountTime = 0;
    private double lastFpsValue = 0.0;
    private int frames = 0;

    float sx;
    float sy;


    public WhirlView(Context context, int width, int height) {
        super(context);

        sx = ((float) width) / WIDTH;
        sy = ((float) height) / HEIGHT;


        initField();
        initFps();
    }

    @Override
    public void onDraw(Canvas canvas) {
        recount();

        canvas.scale(sx, sy);
        canvas.drawBitmap(toDraw, 0, WIDTH, 0, 0, WIDTH, HEIGHT, false, null);

        fps(canvas);

        postInvalidate();
    }

    private boolean willChangeColor(int i, int j) {
        //пройти по соседям и посмотреть теги
        int ni = 0, nj = 0;
        int cur = field[i][j];
        for (int di = -1; di < 2; ++di) {
            for (int dj = -1; dj < 2; ++dj) {
                if (di == dj && di == 0) {
                    continue;
                }
                ni = i + di;
                nj = j + dj;
                if (ni < 0) ni = HEIGHT - 1;
                if (nj < 0) nj = WIDTH - 1;
                if (ni == HEIGHT) ni = 0;
                if (nj == WIDTH) nj = 0;

                if (cur + 1 == field[ni][nj] ||
                        (cur == COLORS_NUMBER - 1 && field[ni][nj] == 0)) {
                    return true;
                }
            }
        }
        return false; //красить не надо
    }

    void recount() {
        int newTag = -1;
        for (int p = 0, i = 0, j = 0; p < NUM; ++p, ++j) {
            if (j == WIDTH) {
                ++i;
                j = 0;
            }

            toDraw[p] = colors[field[i][j]];

            if (willChangeColor(i, j)) {
                fieldCopy[i][j]++;
                if (fieldCopy[i][j] == COLORS_NUMBER)
                    fieldCopy[i][j] = 0;
            }

        }


        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {

                field[i][j] = fieldCopy[i][j];
            }
        }
    }

    void fps(Canvas canvas) {
        ++frames;
        long time;
        Paint p = new Paint();
        p.setARGB(255, 0, 0, 0);
        if (frames >= 10) {

            time = System.currentTimeMillis();
            lastFpsValue = (frames * 1000) / ((double) time - lastFpsCountTime);
            lastFpsCountTime = time;
            frames = 0;
        }
        canvas.drawText("FPS: " + lastFpsValue, 10, 10, p);
    }

    void initFps() {
        lastFpsCountTime = System.currentTimeMillis();
        lastFpsValue = 0.0;
        frames = 0;
    }

    void initField() {
        for (int i = 0; i < COLORS_NUMBER; ++i) {
            colors[i] = (int) (Math.random() * 0xFFFFFF + 1);
        }


        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                field[i][j] = (int) (Math.random() * COLORS_NUMBER);
                fieldCopy[i][j] = field[i][j];
            }
        }

    }


}
