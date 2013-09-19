package ru.ifmo.stddev.solovyeva;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;
import java.util.Timer;

public class WhirlActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    class MyView extends View {

        final static int Width = 240;
        final static int Height = 320;
        int DisplayWidth;
        int DisplayHeight;
        int[][] pole1 = new int[Height][Width];
        int[][] pole2 = new int[Height][Width];
        int[] colors;
        int[] p;
        private final int numberOfColors = 15;
        private long fps, cnt = 0, tmp;
        Paint fpsPaint = new Paint();
        private static final int MAX_FRAME_COUNT = 10;
        private final long[] frames = new long[MAX_FRAME_COUNT];

        public MyView(Context context) {
            super(context);

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            DisplayWidth = size.x;
            DisplayHeight = size.y;

            p = new int[Width*Height];
            fpsPaint.setARGB(255, 255, 255, 255);
            fps = SystemClock.uptimeMillis();

            Random rand = new Random();
            colors = new int[numberOfColors];
            for(int i = 0; i < numberOfColors; ++i) {
                colors[i] = rand.nextInt(65536);
            }


            for(int i = 0; i < Height; i++) {
                for(int j = 0; j < Width; j++) {
                    pole1[i][j] = rand.nextInt(15);
                }
            }

        }

        @Override
        protected void onDraw(Canvas canvas) {
            int k = 0;
            for(int i = 0; i < Height; i++) {
                int prevX = (i-1==-1)?(Height-1):(i-1);
                int nextX = (i+1==Height)?(0):(i+1);
                for(int j = 0; j < Width; j++) {
                    int prevY = (j-1==-1)?(Width-1):(j-1);
                    int nextY = (j+1==Width)?(0):(j+1);
                    int prevColor = pole1[i][j];
                    int nextColor = (prevColor +1 == numberOfColors) ? 0 : (prevColor +1);
                    if(     pole1[prevX][prevY] == nextColor ||
                            pole1[prevX][j] == nextColor ||
                            pole1[prevX][nextY] == nextColor ||

                            pole1[i][prevY] == nextColor ||
                            pole1[i][nextY] == nextColor ||

                            pole1[nextX][prevY] == nextColor ||
                            pole1[nextX][j] == nextColor ||
                            pole1[nextX][nextY] == nextColor ) {
                        p[k++] = colors[nextColor];
                        pole2[i][j] = nextColor;
                    } else {
                        p[k++] = colors[prevColor];
                        pole2[i][j] = prevColor;
                    }
                }
            }

            int[][] t = pole1;
            pole1 = pole2;
            pole2 = t;


            Matrix matrix = new Matrix();
            matrix.setScale((float) DisplayWidth / Width, (float) DisplayHeight / Height);
            canvas.setMatrix(matrix);
            canvas.drawBitmap(p, 0, Width, 0, 0, Width, Height, false, null);

            System.arraycopy(frames, 0, frames, 1, MAX_FRAME_COUNT - 1);
            frames[0] = SystemClock.uptimeMillis();

            canvas.drawText(Long.toString(1000 * MAX_FRAME_COUNT  / (frames[0] - frames[MAX_FRAME_COUNT - 1])), 5, 10, fpsPaint);

            invalidate();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyView view = new MyView(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(view);
    }
}
