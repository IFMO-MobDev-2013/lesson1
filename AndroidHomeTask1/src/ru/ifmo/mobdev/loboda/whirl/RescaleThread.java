package ru.ifmo.mobdev.loboda.whirl;

import android.graphics.Bitmap;

class RescaleThread extends Thread{
    private boolean runFlag = false;
    private boolean ready;
    private Bitmap bitmap;
    private int scaledWidth;
    private int scaledHeigth;
    private Bitmap unscaledBitmap;
    CalcThread calcThread;

    public RescaleThread(int width, int height, int scaledWidth, int scaledHeight){
        runFlag = true;
        ready = false;
        this.scaledHeigth = scaledHeight;
        this.scaledWidth = scaledWidth;
        calcThread = new CalcThread(width, height);
        calcThread.start();
    }

    public void finish(){
        runFlag = false;
        calcThread.finish();
    }

    public Bitmap getBitMap(){
        return bitmap;
    }

    public void recalc(){
        ready = false;
    }

    public boolean getReady(){
        return ready;
    }

    @Override
    public void run() {
        while(runFlag){
            while(ready && runFlag){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            while(!calcThread.getReady() && runFlag){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            unscaledBitmap = calcThread.getBitMap();
            calcThread.recalc();
            bitmap = Bitmap.createScaledBitmap(unscaledBitmap, scaledWidth, scaledHeigth, false);
            ready = true;
        }
    }
}
