package com.blumonk.Whirl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

public class WhirlActivity extends Activity {

    public class WhirlView extends View {
        public WhirlView(Context context) {
            super(context);
            /*
             Filling up the matrix with random values
             */
            Random generator = new Random(System.nanoTime());
            for (int i = 0; i < rowCount; ++i) {
                for (int j = 0; j < colCount; ++j) {
                    matrix[i][j] = generator.nextInt(colorCount);
                    matrixBuffer[i][j] = matrix[i][j];
                }
            }
            /*
             Getting display dimensions,
             Calculating the scale
             */
            Display display = getWindowManager().getDefaultDisplay();
            Point dimensions = new Point();
            display.getSize(dimensions);
            scaleX = (float) dimensions.x / colCount;
            scaleY = (float) dimensions.y / rowCount;
            Log.d("SCALE", Float.toString(scaleX) + " " + Float.toString(scaleY));
            startTimer = System.nanoTime();
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.scale(scaleX, scaleY);
            updateState();
            ++frameCount;
            canvas.drawBitmap(stretchedMatrix, 0, colCount, 0, 0, colCount, rowCount, false, null);
            textPaint.setARGB(127, 0, 0, 0);
            canvas.drawRect(0, 0, colCount, 30, textPaint);
            textPaint.setARGB(255, 255, 255, 255);
            textPaint.setTextSize(20);
            fps = frameCount * 1e9 / (System.nanoTime() - startTimer);
            canvas.drawText("FPS: " + Double.toString(fps).substring(0, 10), 20, 20, textPaint);
            if (frameCount == 20) {
                frameCount = 0;
                startTimer = System.nanoTime();
            }
            invalidate();
        }
    }

    private final int rowCount = 320;
    private final int colCount = 240;
    private final int colorCount = 15;
    private float scaleX;
    private float scaleY;
    private int[][] matrix = new int[rowCount][colCount];
    private int[][] matrixBuffer = new int[rowCount][colCount];
    private int[] stretchedMatrix = new int[rowCount * colCount];
    private final int[] colorChart = {
            0x00CCFF, 0x000066, 0x00FF00, 0x009900, 0xFF0000, 0xFF9900,
            0xFF3399, 0x9900FF, 0x00CC99, 0xFF99FF, 0xFFFF66, 0x660033,
            0x660066, 0x00FFFF, 0xFFFF00

    };

    private int frameCount = 0;
    private long startTimer;
    private double fps;
    private Paint textPaint = new Paint();

    private void updateState() {
        int lineIndex = 0; // New index of the cell in the stretchedMatrix
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < colCount; ++j) {
                updateCell(i, j, lineIndex++);
            }
        }
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < colCount; ++j) {
                matrix[i][j] = matrixBuffer[i][j];
            }
        }
    }

    /*
     centerRow, centerCol - indexes of the cell
     lineIndex - index of the cell in the stretchedMatrix
    */
    private void updateCell(int centerRow, int centerCol, int lineIndex) {
        int upRow = (centerRow == 0 ? rowCount - 1 : centerRow - 1);
        int downRow = (centerRow == rowCount - 1 ? 0 : centerRow + 1);
        int leftCol = (centerCol == 0 ? colCount - 1 : centerCol - 1);
        int rightCol = (centerCol == colCount - 1 ? 0 : centerCol + 1);
        int curColor = matrix[centerRow][centerCol];
        int nextColor = (curColor == colorCount - 1 ? 0 : curColor + 1);
        if (matrix[upRow][leftCol] == nextColor ||
                matrix[upRow][centerCol] == nextColor ||
                matrix[upRow][rightCol] == nextColor ||
                matrix[centerRow][leftCol] == nextColor ||
                matrix[centerRow][rightCol] == nextColor ||
                matrix[downRow][leftCol] == nextColor ||
                matrix[downRow][centerCol] == nextColor ||
                matrix[downRow][rightCol] == nextColor) {
            matrixBuffer[centerRow][centerCol] = nextColor;
            stretchedMatrix[lineIndex] = colorChart[matrixBuffer[centerRow][centerCol]];
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(new WhirlView(this));
    }
}
