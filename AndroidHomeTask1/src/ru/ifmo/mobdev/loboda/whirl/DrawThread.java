package ru.ifmo.mobdev.loboda.whirl;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

class DrawThread extends Thread{
    private boolean runFlag = false;
    private SurfaceHolder surfaceHolder;
    private RescaleThread rescaleThread;
    private Bitmap bitmap;

    public DrawThread(SurfaceHolder surfaceHolder, int width, int height, int scaledWidth, int scaledHeight){
        this.surfaceHolder = surfaceHolder;
        runFlag = true;
        rescaleThread = new RescaleThread(width, height, scaledWidth, scaledHeight);
        rescaleThread.start();
    }

    public void finish(){
        runFlag = false;
        rescaleThread.finish();
    }

    @Override
    public void run() {
        Canvas canvas;
        Paint paint = new Paint();
        paint.setTextSize(36);
        paint.setARGB(255, 255, 255, 255);
        int cnt = 0;
        int fps = 0;
        long start = System.currentTimeMillis();
        while(runFlag){
            while(!rescaleThread.getReady() && runFlag){
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            bitmap = rescaleThread.getBitMap();
            rescaleThread.recalc();
            canvas = null;
            canvas = surfaceHolder.lockCanvas(null);
            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.drawText(new Integer(fps) + "FPS", 30, 30, paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
            ++cnt;
            if(cnt == 20){
                long time = System.currentTimeMillis() - start;
                fps = (int)((float)(20) / ((float)(time) / (float)(1000)));
                start = System.currentTimeMillis();
                cnt = 0;
            }
        }
    }
}
