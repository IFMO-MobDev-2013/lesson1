package com.polarnick.day01;

import java.util.ArrayList;
import java.util.List;

public class PeriodHolder {

    private boolean isStillPeriodStarted;
    private State state = State.NOT_IN_PERIOD;
    private final List<int[]> frames;
    private int nextToRender = 0;
    private int cur = 0;

    public PeriodHolder(int periodLength, int width, int height) {
        frames = new ArrayList<int[]>(periodLength);
        for (int i = 0; i < periodLength; i++) {
            frames.add(new int[width * height]);
        }
    }

    public boolean isStillPeriodStarted() {
        return isStillPeriodStarted;
    }

    public void setStillPeriodStarted(boolean stillPeriod) {
        isStillPeriodStarted = stillPeriod;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int[] getNextToRender() {
        return frames.get(nextToRender);
    }

    private void moveToNextToRender() {
        nextToRender++;
    }

    public int[] getCurFrame() {
        return frames.get(cur);
    }

    private void goToNextFrame() {
        cur = (cur + 1) % frames.size();
    }

    public void updateState() {
        switch (state) {
            case NOT_IN_PERIOD: {
                if (isStillPeriodStarted) {
                    state = State.PERIOD_CALCULATES;
                }
                break;
            }
            case PERIOD_CALCULATES: {
                moveToNextToRender();
                if (nextToRender == frames.size()) {
                    state = State.PERIOD_CALCULATED;
                }
                break;
            }
            case PERIOD_CALCULATED: {
                goToNextFrame();
                break;
            }
        }
    }

    public enum State {
        NOT_IN_PERIOD,
        PERIOD_CALCULATES,
        PERIOD_CALCULATED,
    }
}
