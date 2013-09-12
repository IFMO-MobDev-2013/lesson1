package com.polarnick.day01;

import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class FieldUpdater implements Runnable {

    private Profiler<FieldUpdaterThreadFunctions> profiler;

    private final int width;
    private final int height;

    private int[] field;
    private int[] tmp;

    private int[] fieldNext;
    private int[] tmpNext;

    private final int fromY;
    private final int toY;
    private final boolean cornerCases;
    private CountDownLatch counter;

    private final static int[] dx = new int[]{-1, 0, 1, -1, 0, 1, -1, 1};
    private final static int[] dy = new int[]{-1, -1, -1, 1, 1, 1, 0, 0};

    private final int[] dIndex;

    private final int[] cornerY;
    private final int[] cornerX;

    public FieldUpdater(int threadNum, int fromY, int toY, boolean cornerCases, int width, int height, int[] field, int[] tmp, int[] fieldNext, int[] tmpNext) {
        this.profiler = new Profiler<FieldUpdaterThreadFunctions>(this.getClass(), "thread=" + threadNum);

        this.fromY = fromY;
        this.toY = toY;
        this.cornerCases = cornerCases;
        this.width = width;
        this.height = height;

        this.field = field;
        this.tmp = tmp;
        this.fieldNext = fieldNext;
        this.tmpNext = tmpNext;

        this.dIndex = new int[]{-width - 1, -width, -width + 1, width - 1, width, width + 1, -1, 1};
        this.cornerY = new int[]{0, height - 1};
        this.cornerX = new int[]{0, width - 1};
    }

    public void setCounter(CountDownLatch counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        updateField5(fromY, toY, cornerCases);
        counter.countDown();
        if (profiler.isToLogNextStep()) {
            Log.d(this.getClass().getName(), profiler.getAverangeLog());
        }
    }

    private void updateField4(int fromY, int toY, boolean cornerCases) {
        profiler.in(FieldUpdaterThreadFunctions.updateField4);
        {
            fromY = Math.max(fromY, cornerY[0] + 1);
            toY = Math.min(toY, cornerY[1]);
            int[] inds = new int[dIndex.length];
            int index = width * fromY + 1;
            for (int k = 0; k < inds.length; k++) {
                inds[k] = index + dIndex[k];
            }
            for (int y = fromY; y < toY; ++y) {
                for (int x = 1; x < width - 1; ++x) {
                    boolean notChanged = true;
                    for (int ind : inds) {
                        if (field[ind] == fieldNext[index]) {
                            tmp[index] = fieldNext[index];
                            tmpNext[index] = fieldNext[ind];
                            notChanged = false;
                            break;
                        }
                    }
                    if (notChanged) {
                        tmp[index] = field[index];
                        tmpNext[index] = fieldNext[index];
                    }
                    ++index;
                    for (int k = 0; k < inds.length; k++) {
                        ++inds[k];
                    }
                }
                ++index;
                ++index;
                for (int k = 0; k < inds.length; k++) {
                    ++inds[k];
                    ++inds[k];
                }
            }
        }
        if (cornerCases) {
            calculateCornerCases();
        }
        swapFields();
        profiler.out(FieldUpdaterThreadFunctions.updateField4);
    }

    private void updateField5(int fromY, int toY, boolean cornerCases) {
        profiler.in(FieldUpdaterThreadFunctions.updateField5);
        {
            fromY = Math.max(fromY, cornerY[0] + 1);
            toY = Math.min(toY, cornerY[1]);
            int index = width * fromY + 1;
            for (int y = fromY; y < toY; ++y) {
                for (int x = 1; x < width - 1; ++x) {
                    //-width - 1, -width, -width + 1, width - 1, width, width + 1, -1, 1
                    if (field[index - width - 1] == fieldNext[index]) {
                        tmp[index] = fieldNext[index];
                        tmpNext[index] = fieldNext[index - width - 1];

                    } else if (field[index - width] == fieldNext[index]) {
                        tmp[index] = fieldNext[index];
                        tmpNext[index] = fieldNext[index - width];

                    } else if (field[index - width + 1] == fieldNext[index]) {
                        tmp[index] = fieldNext[index];
                        tmpNext[index] = fieldNext[index - width + 1];

                    } else if (field[index + width - 1] == fieldNext[index]) {
                        tmp[index] = fieldNext[index];
                        tmpNext[index] = fieldNext[index + width - 1];

                    } else if (field[index + width] == fieldNext[index]) {
                        tmp[index] = fieldNext[index];
                        tmpNext[index] = fieldNext[index + width];

                    } else if (field[index + width + 1] == fieldNext[index]) {
                        tmp[index] = fieldNext[index];
                        tmpNext[index] = fieldNext[index + width + 1];

                    } else if (field[index - 1] == fieldNext[index]) {
                        tmp[index] = fieldNext[index];
                        tmpNext[index] = fieldNext[index - 1];

                    } else if (field[index + 1] == fieldNext[index]) {
                        tmp[index] = fieldNext[index];
                        tmpNext[index] = fieldNext[index + 1];
                    } else {
                        tmp[index] = field[index];
                        tmpNext[index] = fieldNext[index];
                    }
                    ++index;
                }
                ++index;
                ++index;
            }
        }
        if (cornerCases) {
            calculateCornerCases();
        }
        swapFields();
        profiler.out(FieldUpdaterThreadFunctions.updateField5);
    }

    private void calculateCornerCases() {
        for (int y : cornerY) {
            int index = width * y;
            for (int x = 0; x < width; x++) {
                boolean hasNeib = false;
                for (int k = 0; k < dIndex.length; k++) {
                    int neib = ((x + dx[k] + width) % width) + ((y + dy[k] + height) % height) * width;
                    if (field[neib] == fieldNext[index]) {
                        tmp[index] = fieldNext[index];
                        tmpNext[index] = fieldNext[neib];
                        hasNeib = true;
                        break;
                    }
                }
                if (!hasNeib) {
                    tmp[index] = field[index];
                    tmpNext[index] = fieldNext[index];
                }
                ++index;
            }
        }
        for (int x : cornerX) {
            int index = x;
            for (int y = 0; y < height; y++) {
                boolean hasNeib = false;
                for (int k = 0; k < dIndex.length; k++) {
                    int neib = ((x + dx[k] + width) % width) + ((y + dy[k] + height) % height) * width;
                    if (field[neib] == fieldNext[index]) {
                        tmp[index] = fieldNext[index];
                        tmpNext[index] = fieldNext[neib];
                        hasNeib = true;
                        break;
                    }
                }
                if (!hasNeib) {
                    tmp[index] = field[index];
                    tmpNext[index] = fieldNext[index];
                }
                index += width;
            }
        }
    }

    private void swapFields() {
        int[] tmp2 = tmp;
        tmp = field;
        field = tmp2;
        tmp2 = tmpNext;
        tmpNext = fieldNext;
        fieldNext = tmp2;
    }

    private enum FieldUpdaterThreadFunctions {
        updateField5, updateField4
    }

}
