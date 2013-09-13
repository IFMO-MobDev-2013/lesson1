package ru.ifmo.ivanov25392013.cellularautomaton;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CellularAutomatonActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CASurfaceView(this, new ColoredCA()));
    }

    public class CASurfaceView extends SurfaceView implements SurfaceHolder.Callback {
        private DrawThread drawThread;
        private CellularAutomaton cellularAutomaton;

        public CASurfaceView(Context context, CellularAutomaton cellularAutomaton) {
            super(context);
            getHolder().addCallback(this);
            this.cellularAutomaton = cellularAutomaton;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            drawThread = new DrawThread(getHolder(), cellularAutomaton);
            drawThread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            drawThread.finish();
        }

        class DrawThread extends Thread {
            private SurfaceHolder surfaceHolder;
            private boolean running = true;
            private CellularAutomaton cellularAutomaton;
            private int width, height;

            public DrawThread(SurfaceHolder surfaceHolder, CellularAutomaton cellularAutomaton) {
                this.surfaceHolder = surfaceHolder;
                this.cellularAutomaton = cellularAutomaton;
            }

            public void finish() {
                running = false;
                cellularAutomaton.kill();
            }

            public void run() {
                Paint paint = new Paint();
                paint.setARGB(255, 255, 255, 255);
                paint.setTextSize(36);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                width = size.x;
                height = size.y;
                Canvas canvas;
                Bitmap bitmap;
                cellularAutomaton.start();
                int fps = -1;
                long move = 0, last = System.currentTimeMillis();
                while (running) {
                    canvas = surfaceHolder.lockCanvas(null);
                    bitmap = cellularAutomaton.getField();
                    cellularAutomaton.recalculate();
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                    canvas.drawBitmap(bitmap, 0, 0, null);
                    if (move % 20 == 19) {
                        fps = 20000 / (int) (System.currentTimeMillis() - last);
                        last = System.currentTimeMillis();
                    }
                    canvas.drawText("FPS: ".concat(fps == -1 ? "?" : Integer.toString(fps)), 20, 50, paint);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    while (!cellularAutomaton.isFinished()) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                        }
                    }
                    move++;
                }
            }
        }
    }
}
