package com.example.untitled;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: mv
 * Date: 06.09.13
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */
public class WhirlActivity extends Activity {

   /*
    class WhirlView extends View {
        private final int WIDTH;
        private final int HEIGHT;
        private final int N = 320;
        private final int M = 240;
        private final int MAX_COLORS = 10;
        private final Paint FOR_FPS = new Paint();
        private Bitmap bitmap = Bitmap.createBitmap(M, N, Bitmap.Config.ARGB_8888);
        private long frames, lastTime, currentTime, fps = 10;
        int[] resClr = new int[N * M];
        int[][] field = new int[N][M];
        int[][] newField = new int[N][M];
        int prevI, prevJ, nextI, nextJ, nextColor;
        int[] palette = {Color.parseColor("#FF832fe2"),
                Color.parseColor("#FFba2fe2"),
                Color.parseColor("#FFe22fa9"),
                Color.parseColor("#FFe22f2f"),
                Color.parseColor("#FFe2772f"),
                Color.parseColor("#FFe2d72f"),
                Color.parseColor("#FF87e22f"),
                Color.parseColor("#FF2fe259"),
                Color.parseColor("#FF2fcfe2"),
                Color.parseColor("#FF2f6ee2")} ;


        public WhirlView(Context context) {
            super(context);
            Display display = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            WIDTH = point.x;
            HEIGHT = point.y;
            Random random = new Random();
            FOR_FPS.setTextSize(50);
            FOR_FPS.setColor(Color.BLACK);
            lastTime = System.currentTimeMillis();
            for (int i = 0; i < N; i++)  {
                for (int j = 0; j < M; j++)  {
                    field[i][j] = random.nextInt(MAX_COLORS);
                    resClr[i * M + j] = palette[field[i][j]];
                }
            }
        }


        @Override
        public void onDraw(Canvas canvas) {
            bitmap.setPixels(resClr, 0, M, 0, 0, M, N);
            canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, false), 0, 0, null);

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    prevI = (i - 1 + N) % N;
                    prevJ = (j - 1 + M) % M;
                    nextI = (i + 1) % N;
                    nextJ = (j + 1) % M;
                    nextColor = (field[i][j] + 1) % MAX_COLORS;
                    if (field[prevI][prevJ] == nextColor ||
                            field[prevI][j] == nextColor ||
                            field[prevI][nextJ] == nextColor ||
                            field[i][prevJ] == nextColor ||
                            field[i][nextJ] == nextColor ||
                            field[nextI][nextJ] == nextColor ||
                            field[nextI][j] == nextColor ||
                            field[nextI][nextJ] == nextColor) {
                        newField[i][j] = nextColor;
                        resClr[i * M + j] = palette[nextColor];
                    } else {
                        newField[i][j] = field[i][j];
                    }
                }
            }

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    field[i][j] = newField[i][j];
                }
            }

            frames++;
            canvas.drawText(fps + " fps", 100, 100, FOR_FPS);
            currentTime = System.currentTimeMillis();
            if (currentTime - lastTime > 1000) {
                fps = frames;
                frames = 0;
                lastTime = currentTime;
            }

            invalidate();
        }
    }
   */
    public class DrawThread extends Thread {

     private SurfaceHolder surfaceHolder;
     private MySurfaceView surfaceView;
     private boolean run = false;

     public DrawThread(SurfaceHolder surfaceHolder, MySurfaceView surfaceView) {
         this.surfaceHolder = surfaceHolder;
         this.surfaceView = surfaceView;
     }

     public void setRunning(boolean b) {
         this.run = b;
     }

     @Override
     public void run() {
         while (run) {
             Canvas canvas = null;
             try {
                 canvas = surfaceHolder.lockCanvas(null);
                 synchronized (surfaceHolder) {
                     surfaceView.onDraw(canvas);
                 }
             } finally {
                 if (canvas != null) {
                     surfaceHolder.unlockCanvasAndPost(canvas);
                 }
             }
         }
     }
 }

    public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

         private final int WIDTH;
         private final int HEIGHT;
         private final int N = 320;
         private final int M = 240;
         private final int MAX_COLORS = 10;
         private final Paint FOR_FPS = new Paint();
         private Bitmap bitmap = Bitmap.createBitmap(M, N, Bitmap.Config.RGB_565);
         private long frames, lastTime, currentTime, fps = 10;
         int[] resClr = new int[N * M];
         int[][] field = new int[N][M];
         int[][] newField = new int[N][M];
         int prevI, prevJ, nextI, nextJ, nextColor;
         int[] palette = {Color.parseColor("#FF832fe2"),
             Color.parseColor("#FFba2fe2"),
             Color.parseColor("#FFe22fa9"),
             Color.parseColor("#FFe22f2f"),
             Color.parseColor("#FFe2772f"),
             Color.parseColor("#FFe2d72f"),
             Color.parseColor("#FF87e22f"),
             Color.parseColor("#FF2fe259"),
             Color.parseColor("#FF2fcfe2"),
             Color.parseColor("#FF2f6ee2")} ;

        private DrawThread thread;

        public MySurfaceView(Context context) {
            super(context);
            getHolder().addCallback(this);
            Display display = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            WIDTH = point.x;
            HEIGHT = point.y;
            Random random = new Random();
            FOR_FPS.setTextSize(50);
            FOR_FPS.setColor(Color.BLACK);
            lastTime = System.currentTimeMillis();
            for (int i = 0; i < N; i++)  {
                for (int j = 0; j < M; j++)  {
                    field[i][j] = random.nextInt(MAX_COLORS);
                    resClr[i * M + j] = palette[field[i][j]];
                }
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            bitmap.setPixels(resClr, 0, M, 0, 0, M, N);
            canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, false), 0, 0, null);

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    prevI = (i - 1 + N) % N;
                    prevJ = (j - 1 + M) % M;
                    nextI = (i + 1) % N;
                    nextJ = (j + 1) % M;
                    nextColor = (field[i][j] + 1) % MAX_COLORS;
                    if (field[prevI][prevJ] == nextColor ||
                            field[prevI][j] == nextColor ||
                            field[prevI][nextJ] == nextColor ||
                            field[i][prevJ] == nextColor ||
                            field[i][nextJ] == nextColor ||
                            field[nextI][nextJ] == nextColor ||
                            field[nextI][j] == nextColor ||
                            field[nextI][nextJ] == nextColor) {
                        newField[i][j] = nextColor;
                        resClr[i * M + j] = palette[nextColor];
                    } else {
                        newField[i][j] = field[i][j];
                    }
                }
            }

           for (int i = 0; i < N; i++) {
                //for (int j = 0; j < M; j++) {
                   // field[i][j] = newField[i][j];
                    System.arraycopy(newField[i], 0, field[i], 0, M);
              //  }
            }
            frames++;
            canvas.drawText(fps + " fps", 100, 100, FOR_FPS);
            currentTime = System.currentTimeMillis();
            if (currentTime - lastTime > 1000) {
                fps = frames;
                frames = 0;
                lastTime = currentTime;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            thread = new DrawThread(getHolder(), this);
            thread.setRunning(true);
            thread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            thread.setRunning(false);
            while (retry) {
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {

                }
            }
        }

 }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new MySurfaceView(this));
    }

}
