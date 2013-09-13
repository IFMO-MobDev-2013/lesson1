package ru.ifmo.ivanov25392013.cellularautomaton;

import android.graphics.Bitmap;

public abstract class CellularAutomaton extends Thread {
    public abstract boolean isFinished();

    public abstract void kill();

    public abstract void recalculate();

    public abstract Bitmap getField();
}
