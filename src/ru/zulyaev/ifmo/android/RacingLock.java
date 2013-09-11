package ru.zulyaev.ifmo.android;

/**
 * @author Никита
 */
public class RacingLock {
    private volatile boolean ended;
    private volatile int gap;
    private final int maxGap;

    public RacingLock(int maxGap) {
        this.maxGap = maxGap;
    }

    public synchronized void lead() throws InterruptedException {
        if (ended) return;
        if (gap == maxGap) {
            wait();
        }
        ++gap;
        notify();
    }

    public synchronized void overtake() throws InterruptedException {
        if (ended) return;
        if (gap == 0) {
            wait();
        }
        --gap;
        notify();
    }

    public synchronized void endRace() {
        ended = true;
        notify();
    }
}
