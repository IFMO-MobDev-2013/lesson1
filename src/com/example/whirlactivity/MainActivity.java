package com.example.whirlactivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.graphics.Matrix;
import java.util.Random;

public class MainActivity extends Activity {
    private class WhirlView extends View {
        private final int n = 320;
        private final int m = 240;
        private final int SizeCol = 12;
        private final int[] Colors = {Color.GREEN, Color.YELLOW, Color.BLUE, Color.GRAY, Color.CYAN, Color.RED, Color.WHITE, Color.BLACK, Color.DKGRAY, Color.LTGRAY, Color.MAGENTA,
                Color.TRANSPARENT};
        private final Paint fpsPaint = new Paint();
        private int[][] matrixOld = new int[n][m];
        private int[][] matrixNew = new int[n][m];
        private int[] colorBit = new int[n*m];
        private int CntFps;
        private long Fps = 0;
        long startTime,spentTime;

        public WhirlView(Context context) {
            super(context);
            fpsPaint.setColor(Color.WHITE);
            fpsPaint.setTextSize(15);
            startTime = System.currentTimeMillis();
            Random rand = new Random();
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < m; j++) {
                    matrixOld[i][j] = rand.nextInt(SizeCol);
                }
            }
        }

        @Override
        public void onDraw(Canvas canvas) {
            int pr = 0;
            for(int i = 0; i < n; i++) {
                int nexti = i + 1; if (nexti == n) nexti = 0;
                int previ = i - 1; if (previ == -1) previ = n - 1;
                for(int j = 0; j < m; j++) {
                    int nextj = j + 1; if (nextj == m) nextj = 0;
                    int prevj = j - 1; if (prevj == -1) prevj = m - 1;
                    int ans = matrixOld[i][j];
                    colorBit[pr++] = Colors[ans];
                    int value = ans + 1;
                    if (value == SizeCol)
                        value = 0;
                    if ((matrixOld[previ][prevj] == value) ||
                            (matrixOld[previ][j] == value) ||
                            (matrixOld[previ][nextj] == value) ||
                            (matrixOld[i][prevj] == value) ||
                            (matrixOld[i][nextj] == value) ||
                            (matrixOld[nexti][prevj] == value) ||
                            (matrixOld[nexti][j] == value) ||
                            (matrixOld[nexti][nextj] == value)) {
                        ans = value;
                    }
                    matrixNew[i][j] = ans;
                }
            }
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < m; j++) {
                    matrixOld[i][j] = matrixNew[i][j];
                }
            }
            Matrix matrix = new Matrix();
            matrix.setScale(3,4);
            canvas.setMatrix(matrix);
            canvas.drawBitmap(colorBit, 0, m, 0, 0, m, n, false, null);
            spentTime = System.currentTimeMillis();
            CntFps++;
            if (CntFps >= 25) {
                Fps = 1000 / (spentTime - startTime);
                CntFps = 0;
            }
            canvas.drawText("FPS=" + Fps,30,60,fpsPaint);
            startTime = spentTime;
            invalidate();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new WhirlView(this));
    }


}