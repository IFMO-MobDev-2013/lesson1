package ru.ifmo.ivanov25392013.cellularautomaton;

import android.graphics.Bitmap;

import java.util.Random;

public class ColoredCA extends CellularAutomaton {
    private boolean finished = false, running = true, recalculate = false;
    private int squaresWidth = 240, squaresHeight = 320, colorNumber = 20, ColorDifference = 13;
    private int[] colors = new int[colorNumber];
    private int[][][] field = new int[2][squaresHeight][squaresWidth];
    private long move = 0;
    private int[][] map = new int[2][squaresHeight * squaresWidth];

    public ColoredCA() {
        for (int i = 0; i < colorNumber / 2; i++) {
            colors[i] = ColorDifference * i + 256 * 65536 * 255;
            colors[colorNumber - 1 - i] = ColorDifference * (i + 1) + 256 * 65536 * 255;
        }
        Random random = new Random();
        for (int i = squaresHeight; --i >= 0; ) {
            for (int j = squaresWidth; --j >= 0; ) {
                field[0][i][j] = field[1][i][j] = random.nextInt(colorNumber);
                map[0][i * squaresWidth + j] = map[1][i * squaresWidth + j] = colors[field[0][i][j]];
            }
        }
    }

    public void run() {
        while (running) {
            while (!recalculate) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
            }
            finished = false;
            int mn = (int) move % 2, mne = (int) (move + 1) % 2;
            for (int i = squaresHeight; --i >= 0; ) {
                for (int j = squaresWidth; --j >= 0; ) {
                    int value = ((field[mn][i][j] + 1) % colorNumber);
                    boolean match = false;
                    for (int k = -1; k < 2; k++) {
                        int iPlusK = i + k;
                        if (iPlusK >= 0 && iPlusK < squaresHeight) {
                            for (int l = -1; l < 2; l++) {
                                if (j + l >= 0 && j + l < squaresWidth && field[mn][iPlusK][j + l] == value) {
                                    match = true;
                                    break;
                                }
                            }
                            if (match) {
                                break;
                            }
                        }
                    }
                    int mb = match ? value : field[mn][i][j];
                    if (field[mne][i][j] != mb) {
                        field[mne][i][j] = mb;
                        map[mne][i * squaresWidth + j] = colors[mb];
                    }
                }
            }
            move++;
            finished = true;
            recalculate = false;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void kill() {
        recalculate = true;
        running = false;
    }

    public void recalculate() {
        recalculate = true;
    }

    public Bitmap getField() {
        return Bitmap.createBitmap(map[(int) move % 2], squaresWidth, squaresHeight, Bitmap.Config.ARGB_8888);
    }
}