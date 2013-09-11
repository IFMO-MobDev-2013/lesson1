package ru.zulyaev.ifmo.android;

import java.util.Random;

/**
 * @author Никита
 */
public class UpdaterRunnable implements Runnable {
    private static final Random random = new Random();
    private static final int COLOR_NUM = 15;
    private static final int[] COLORS = new int[COLOR_NUM];
    private static final int CACHE_SIZE_BITS = 4;
    private static final int CACHE_SIZE = 1 << CACHE_SIZE_BITS;
    private static final int CACHE_MASK = CACHE_SIZE - 1;
    private static final int[] NEXT_COLOR = new int[CACHE_SIZE];

    static {
        if (CACHE_SIZE < COLOR_NUM) {
            throw new RuntimeException("Cache size is too small for such number of colors");
        }

        boolean[] was = new boolean[CACHE_SIZE];
        for (int i = 0; i < COLOR_NUM; ++i) {
            int color;
            do {
                color = random.nextInt(1 << 24);
            } while (was[color & CACHE_MASK]);
            was[color & CACHE_MASK] = true;
            COLORS[i] = color;
        }
        for (int i = 0; i < COLOR_NUM; ++i) {
            NEXT_COLOR[COLORS[i] & CACHE_MASK] = COLORS[(i + 1) % COLOR_NUM];
        }
    }

    static final int BUFFERS = COLOR_NUM;

    private final boolean hack;

    private final int width;
    private final int cells;
    private final int maxX;
    private final int maxOffset;

    private int currentBuffer = 0;
    private int cycle = 0;

    private final int[][] buffers;
    volatile int currentStep = -1;
    private final RacingLock lock = new RacingLock(BUFFERS - 1);


    public UpdaterRunnable(int width, int height, boolean hack) {
        this.hack = hack;
        this.width = width;

        this.cells = width * height;
        this.maxX = width - 1;
        this.maxOffset = cells - width;
        this.buffers = new int[BUFFERS][cells];

        for (int i = 0; i < cells; ++i) {
            int j = random.nextInt(COLORS.length);
            buffers[0][i] = COLORS[j];
        }
    }

    public int[] getBuffer() throws InterruptedException {
        lock.overtake();
        if (++currentStep == BUFFERS) currentStep = 0;
        return buffers[currentStep];
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                final int[] old = buffers[currentBuffer % BUFFERS];
                ++currentBuffer;
                final int[] buffer = buffers[currentBuffer % BUFFERS];

                if (cycle == 0) {
                    boolean all = true;
                    for (int offset = 0, offset1 = maxOffset, offset2 = width, x = 0; offset < cells; ) {
                        final int i = offset + x;
                        final int should = NEXT_COLOR[old[i] & CACHE_MASK];

                        final int x1 = x == 0 ? maxX : x - 1;
                        final int x2 = x == maxX ? 0 : x + 1;

                        if (old[offset1 + x1] == should || old[offset1 + x] == should || old[offset1 + x2] == should
                                || old[offset + x1] == should || old[offset + x2] == should
                                || old[offset2 + x1] == should || old[offset2 + x] == should || old[offset2 + x2] == should) {
                            buffer[i] = should;
                        } else {
                            buffer[i] = old[i];
                            all = false;
                        }

                        if (x == maxX) {
                            x = 0;
                            if (offset == 0) {
                                offset1 = 0;
                            } else {
                                offset1 += width;
                            }
                            offset += width;
                            if (offset == maxOffset) {
                                offset2 = 0;
                            } else {
                                offset2 += width;
                            }
                        } else {
                            x += 1;
                        }
                    }
                    if (all && hack) ++cycle;
                } else if (cycle++ < BUFFERS) {
                    for (int i = 0; i < cells; ++i) {
                        buffer[i] = NEXT_COLOR[old[i] & CACHE_MASK];
                    }
                } else {
                    lock.endRace();
                    break;
                }

                lock.lead();
            }
        } catch (InterruptedException e) {
            // done.
        }
    }
}
