package ru.skipor.Whirl.WhirlEngine;

import java.util.Random;

import ru.skipor.Whirl.WhirlEngine.Exception.WhrilEngineWrongInputFormatException;

/**
 * Created by vladimirskipor on 9/12/13.
 */
public class WhirlEngine {
    public WhirlEngine(int HEIGHT, int WIDTH, int COLORS_NUM) throws WhrilEngineWrongInputFormatException {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        this.COLORS_NUM = COLORS_NUM;
        if (HEIGHT <= 1 | WIDTH <= 1 | COLORS_NUM <= 1) {
            throw new WhrilEngineWrongInputFormatException();
        }
        TOTAL_CELLS = HEIGHT * WIDTH;
        currentColors = new int[TOTAL_CELLS];
        futureColors = new int[TOTAL_CELLS];
        currentNextColors = new int[TOTAL_CELLS];
        futureNextColors = new int[TOTAL_CELLS];
        colorPalette = new int[COLORS_NUM];
        nextColorPalette = new int[COLORS_NUM];
        dp = new int[]{-WIDTH - 1, -WIDTH, -WIDTH + 1, -1, 1, WIDTH - 1, WIDTH, WIDTH + 1};
        dy = new int[]{-1, -1, -1, 0, 0, 1, 1, 1};
        dx = new int[]{-1, 0, 1, -1, 1, -1, 0, 1};

        initRandomColorPalette();
        initNextColorPalette();
    }


    private void initNextColorPalette() {
        nextColorPalette[COLORS_NUM - 1] = colorPalette[0];
        for (int i = 0; i < COLORS_NUM - 1; i++) {
            nextColorPalette[i] = colorPalette[i + 1];
        }
    }

    private void initRandomColorPalette() {
        for (int i = 0; i < COLORS_NUM; i++) {
            colorPalette[i] = random.nextInt() | 0xFF000000;  //creates opaque color |0xFF000000
        }
    }

    public final int HEIGHT;
    public final int WIDTH;
    final int TOTAL_CELLS;
    public final int COLORS_NUM;

    final int[] colorPalette;
    final int[] nextColorPalette;
    int[] currentColors;
    int[] futureColors;
    int[] currentNextColors;
    int[] futureNextColors;
    final int[] dp;
    final int[] dx;
    final int[] dy;

    final static Random random = new Random();


    private void swapStates() {
        final int[] tmp = currentColors;
        currentColors = futureColors;
        futureColors = tmp;
        final int[] tmp2 = currentNextColors;
        currentNextColors = futureNextColors;
        futureNextColors = tmp2;
    }

    public void setRandomState() {
        for (int i = 0; i < TOTAL_CELLS; i++) {
            final int colorIndex = random.nextInt(COLORS_NUM);
            currentColors[i] = colorPalette[colorIndex];
            currentNextColors[i] = nextColorPalette[colorIndex];
        }
    }

    public int[] getCurrentState() {
        return currentColors;
    }

    public void renderNextState() {
        renderMainCases();
        renderCornerCases();


        swapStates();

    }

    private void renderCornerCases() {

        for (int i = 0; i <= HEIGHT - 1; i += HEIGHT - 1) {
            for (int j = 0; j < WIDTH; j++) {
                boolean colorIsNotChanged = true;
                final int pos = i * WIDTH + j;
                for (int k = 0; k < dx.length; ++k) {
                    final int neighbourPos = (((i + dy[k] + HEIGHT) % HEIGHT) * WIDTH + (j + dx[k] + WIDTH) % WIDTH);
                    if (currentNextColors[pos] == currentColors[neighbourPos]) {
                        colorIsNotChanged = false;
                        futureColors[pos] = currentNextColors[pos];
                        futureNextColors[pos] = currentNextColors[neighbourPos];
                    }
                }
                if (colorIsNotChanged) {
                    futureColors[pos] = currentColors[pos];
                    futureNextColors[pos] = currentNextColors[pos];
                }
            }
        }
//
        for (int j = 0; j <= WIDTH - 1; j += WIDTH - 1) {
            for (int i = 0; i < HEIGHT; i++) {
                boolean colorIsNotChanged = true;
                final int pos = i * WIDTH + j;
                for (int k = 0; k < dx.length; ++k) {
                    final int neighbourPos = (((i + dy[k] + HEIGHT) % HEIGHT) * WIDTH + (j + dx[k] + WIDTH) % WIDTH);
                    if (currentNextColors[pos] == currentColors[neighbourPos]) {
                        colorIsNotChanged = false;
                        futureColors[pos] = currentNextColors[pos];
                        futureNextColors[pos] = currentNextColors[neighbourPos];
                    }
                }
                if (colorIsNotChanged) {
                    futureColors[pos] = currentColors[pos];
                    futureNextColors[pos] = currentNextColors[pos];
                }
            }
        }


    }

    private void renderMainCases() {
        for (int h = 1; h < HEIGHT - 1; h++) {
            for (int pos = h * WIDTH + 1; pos <= WIDTH * (h + 1) - 2; pos++) {
                boolean colorIsNotChanged = true;
                for (int i = 0; i < dp.length; i++) {
                    if (currentNextColors[pos] == currentColors[pos + dp[i]]) {
                        colorIsNotChanged = false;
                        futureColors[pos] = currentNextColors[pos];
                        futureNextColors[pos] = currentNextColors[pos + dp[i]];
                    }
                }
                if (colorIsNotChanged) {
                    futureColors[pos] = currentColors[pos];
                    futureNextColors[pos] = currentNextColors[pos];
                }
            }
        }


    }
}

