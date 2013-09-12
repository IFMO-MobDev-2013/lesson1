package com.gmail.mazinva.ColourfulTable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    Matrix matrix = new Matrix();
    Paint p = new Paint();

    int[] cells = new int[76800];
    int[] cells_buf = new int[76800];
    int[] tmp;

    int seed; // starting number of colours. Set in method onCreate.
    int step; // difference between two neighbour colours.

    int startTime;
    int endTime;
    double curTime;
    String fps = new String();

    class WhirlView extends View {



        public WhirlView(Context context) {
            super (context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.setMatrix(matrix);

            for (int j = 1; j < 319; j++) {
                for (int i = 1; i < 239; i++) {

                    if (    ((cells[240 * (j - 1) + i - 1] - cells[240 * j + i]) == step) ||
                            ((cells[240 * j + i - 1] - cells[240 * j + i]) == step) ||
                            ((cells[240 * (j + 1) + i - 1] - cells[240 * j + i]) == step) || // left part

                            ((cells[240 * (j - 1) + i] - cells[240 * j + i]) == step) ||
                            ((cells[240 * (j + 1) + i] - cells[240 * j + i]) == step) || // up&down part

                            ((cells[240 * (j - 1) + i + 1] - cells[240 * j + i]) == step) ||
                            ((cells[240 * j + i + 1] - cells[240 * j + i]) == step) ||
                            ((cells[240 * (j + 1) + i + 1] - cells[240 * j + i]) == step) ) { // right part


                        cells_buf[240 * j + i] = cells[240 * j + i] + step;

                    } else if ((cells[240 * j + i] == (seed - 1) * step ) &&

                                      ( (cells[240 * (j - 1) + i - 1] == 0) ||
                                        (cells[240 * j + i - 1] == 0) ||
                                        (cells[240 * (j + 1) + i - 1] == 0) ||  // left part

                                        (cells[240 * (j - 1) + i] == 0) ||
                                        (cells[240 * (j + 1) + i] == 0) ||// up&down part

                                        (cells[240 * (j - 1) + i + 1] == 0) ||
                                        (cells[240 * j + i + 1] == 0) ||
                                        (cells[240 * (j + 1) + i + 1] == 0) ) ) {

                        cells_buf[240 * j + i] = 0;
                    } else {
                        cells_buf[240 * j + i] = cells[240 * j + i];
                    }
                }
            }



            // upper row
            for (int i = 1; i < 239; i++) {

                if (    ((cells[240 * 319 + i - 1] - cells[i]) == step) ||
                        ((cells[i - 1] - cells[i]) == step) ||
                        ((cells[240 + i - 1] - cells[i]) == step) ||
                        // left part

                        ((cells[240 * 319 + i] - cells[i]) == step) ||
                        ((cells[240 + i] - cells[i]) == step) ||
                        // up&down part

                        ((cells[240 * 319 + i + 1] - cells[i]) == step) ||
                        ((cells[i + 1] - cells[i]) == step) ||
                        ((cells[240 + 1] - cells[i]) == step)
                        ) { // right part

                    cells_buf[i] = cells[i] + step;

                } else if ((cells[i] == (seed - 1) * step) &&

                  ( (cells[240 * 319 + i - 1] == 0) ||
                    (cells[i - 1] == 0) ||
                    (cells[240 + i - 1] == 0) ||  // left part

                    (cells[240 * 319 + i] == 0) ||
                    (cells[240 + i] == 0) || // up&down part

                    (cells[240 * 319 + i + 1] == 0) ||
                    (cells[i + 1] == 0) ||
                    (cells[240 + i + 1] == 0) ) ) { // right part

                    cells_buf[i] = 0;
                } else {
                    cells_buf[i] = cells[i];
                }
            }

            // downer row
            for (int i = 1; i < 239; i++) {

                if (    ((cells[240 * 318 + i - 1] - cells[240 * 319 + i]) == step) ||
                        ((cells[240 * 319 + i - 1] - cells[240 * 319 + i]) == step) ||
                        ((cells[i - 1] - cells[240 * 319 + i]) == step) || // left part

                        ((cells[240 * 318 + i] - cells[240 * 319 + i]) == step) ||
                        ((cells[i] - cells[240 * 319 + i]) == step) || // up&down part

                        ((cells[240 * 318 + i + 1] - cells[240 * 319 + i]) == step) ||
                        ((cells[240 * 319 + i + 1] - cells[240 * 319 + i]) == step) ||
                        ((cells[i + 1] - cells[240 * 319 + i]) == step) ) { // right part

                    cells_buf[240 * 319 + i] = cells[240 * 319 + i] + step;

                } else if ((cells[240 * 319 + i] >= (seed - 1) * step) &&

                            ( (cells[240 * 318 + i - 1] == 0) ||
                              (cells[240 * 319 + i - 1] == 0) ||
                              (cells[i - 1] == 0) ||  // left part

                              (cells[240 * 318 + i] == 0) ||
                              (cells[i] == 0) || // up&down part

                              (cells[240 * 318 + i + 1] == 0) ||
                              (cells[240 * 319 + i + 1] == 0) ||
                              (cells[i + 1]) == 0) ) { // right part

                    cells_buf[240 * 319 + i] = 0;
                } else {
                    cells_buf[240 * 319 + i] = cells[240 * 319 + i];
                }
            }

            // left column
            for (int j = 1; j < 319; j++) {

                if (    ((cells[240 * (j - 1) + 239] - cells[240 * j]) == step) ||
                        ((cells[240 * j + 239] - cells[240 * j]) == step) ||
                        ((cells[240 * (j + 1) + 239] - cells[240 * j]) == step) || // left part

                        ((cells[240 * (j - 1)] - cells[240 * j]) == step) ||
                        ((cells[240 * (j + 1)] - cells[240 * j]) == step) || // up&down part

                        ((cells[240 * (j - 1) + 1] - cells[240 * j]) == step) ||
                        ((cells[240 * j + 1] - cells[240 * j]) == step) ||
                        ((cells[240 * (j + 1) + 1] - cells[240 * j]) == step) ) { // right part


                    cells_buf[240 * j] = cells[240 * j] + step;

                } else if ((cells[240 * j] >= (seed - 1) * step) &&

                            ( (cells[240 * (j - 1) + 239] == 0) ||
                              (cells[240 * j + 239] == 0) ||
                              (cells[240 * (j + 1) +  239] == 0) ||  // left part

                              (cells[240 * (j - 1)] == 0) ||
                              (cells[240 * (j + 1)] == 0) || // up&down part

                              (cells[240 * (j - 1) + 1] == 0) ||
                              (cells[240 * j + 1] == 0) ||
                              (cells[240 * (j + 1) + 1] == 0) ) ) { // right part

                    cells_buf[240 * j] = 0;
                } else {
                    cells_buf[240 * j] = cells[240 * j];
                }
            }

            // right column
            for (int j = 1; j < 319; j++) {

                if (    ((cells[240 * (j - 1) + 238] - cells[240 * j + 239]) == step) ||
                        ((cells[240 * j + 238] - cells[240 * j + 239]) == step) ||
                        ((cells[240 * (j + 1) + 238] - cells[240 * j + 239]) == step) || // left part

                        ((cells[240 * (j - 1) + 239] - cells[240 * j + 239]) == step) ||
                        ((cells[240 * (j + 1) + 239] - cells[240 * j + 239]) == step) || // up&down part

                        ((cells[240 * (j - 1)] - cells[240 * j + 239]) == step) ||
                        ((cells[240 * j] - cells[240 * j + 239]) == step) ||
                        ((cells[240 * (j + 1)] - cells[240 * j + 239]) == step) ) { // right part

                    cells_buf[240 * j + 239] = cells[240 * j + 239] + step;

                } else if ((cells[240 * j + 239] >= (seed - 1) * step) &&

                              ( (cells[240 * (j - 1) + 238] == 0) ||
                                (cells[240 * j + 238] == 0) ||
                                (cells[240 * (j + 1) +  238] == 0) ||  // left part

                                (cells[240 * (j - 1) + 239] == 0) ||
                                (cells[240 * (j + 1) + 239] == 0) || // up&down part

                                (cells[240 * (j - 1)] == 0) ||
                                (cells[240 * j] == 0) ||
                                (cells[240 * (j + 1)] == 0) ) ) { // right part

                    cells_buf[240 * j + 239] = 0;
                } else {
                    cells_buf[240 * j + 239] = cells[240 * j + 239];
                }
            }


            // corners:
            // left&up
            if (    ((cells[240 * 320 - 1] - cells[0]) == step) ||
                    ((cells[239] - cells[0]) == step) ||
                    ((cells[240 + 239] - cells[0]) == step) || // left part

                    ((cells[240 * 319] - cells[0]) == step) ||
                    ((cells[240] - cells[0]) == step) || // up&down part

                    ((cells[240 * 319 + 1] - cells[0]) == step) ||
                    ((cells[1] - cells[0]) == step) ||
                    ((cells[240 + 1] - cells[0]) == step) ) { // right part

                cells_buf[0] = cells[0] + step;
            } else if ( (cells[0] >= (seed - 1) * step) &&
                    ( (cells[240 * 320 - 1] == 0) || (cells[239] == 0) || (cells[240 + 239] == 0) ||
                    (cells[240 * 319] == 0) || (cells[240] == 0) || (cells[240 * 319 + 1] == 0) ||
                    (cells[1] == 0) || (cells[240 + 1] == 0) ) ) {

                cells_buf[0] = 0;
            } else {
                cells_buf[0] = cells[0];
            }

            // right&up
            if (    ((cells[240 * 319 + 238] - cells[239]) == step) ||
                    ((cells[238] - cells[239]) == step) ||
                    ((cells[240 + 238] - cells[239]) == step) || // left part

                    ((cells[240 * 319 + 239] - cells[239]) == step) ||
                    ((cells[240 + 239] - cells[239]) == step) || // up&down part

                    ((cells[240 * 319] - cells[239]) == step) ||
                    ((cells[0] - cells[239]) == step) ||
                    ((cells[240] - cells[239]) == step) ) { // right part

                cells_buf[239] = cells[239] + step;
            } else if ( (cells[239] >= (seed - 1) * step) &&
                    ( (cells[240 * 319 + 238] == 0) || (cells[238] == 0) || (cells[240 + 238] == 0) ||
                    (cells[240 * 319 + 239] == 0) || (cells[240 + 239] == 0) || (cells[240 * 319] == 0) ||
                    (cells[0]  == 0) || (cells[240] == 0) ) ) {

                cells_buf[239] = 0;
            } else {
                cells_buf[239] = cells[239];
            }

            // left&down
            if (    ((cells[240 * 319 - 1] - cells[240 * 319]) == step) ||
                    ((cells[240 * 320 - 1] - cells[240 * 319]) == step) ||
                    ((cells[239] - cells[240 * 319]) == step) || // left part

                    ((cells[240 * 318] - cells[240 * 319]) == step) ||
                    ((cells[0] - cells[240 * 319]) == step) || // up&down part

                    ((cells[240 * 318 + 1] - cells[240 * 319]) == step) ||
                    ((cells[240 * 319 + 1] - cells[240 * 319]) == step) ||
                    ((cells[1] - cells[240 * 319]) == step) ) { // right part

                cells_buf[240 * 239] = cells[240 * 239] + step;
            } else if ( (cells[240 * 239] >= (seed - 1) * step) &&
                    ( (cells[240 * 319 - 1] == 0) || (cells[240 * 320 - 1] == 0) || (cells[239] == 0) ||
                    (cells[240 * 318] == 0) || (cells[0] == 0) || (cells[240 * 318 + 1] == 0) ||
                    (cells[240 * 319 + 1] == 0) || (cells[1] == 0) ) ) {

                cells_buf[240 * 239] = 0;
            } else {
                cells_buf[240 * 239] = cells[240 * 239];
            }

            // right&down
            if (    ((cells[240 * 319 - 2] - cells[240 * 320 - 1]) == step) ||
                    ((cells[240 * 320 - 2] - cells[240 * 320 - 1]) == step) ||
                    ((cells[238] - cells[240 * 320 - 1]) == step) || // left part

                    ((cells[240 * 319 - 1] - cells[240 * 320 - 1]) == step) ||
                    ((cells[239] - cells[240 * 320 - 1]) == step) || // up&down part

                    ((cells[240 * 318] - cells[240 * 320 - 1]) == step) ||
                    ((cells[240 * 319] - cells[240 * 320 - 1]) == step) ||
                    ((cells[0] - cells[240 * 320 - 1]) == step) ) { // right part

                cells_buf[240 * 320 - 1] = cells[240 * 320 - 1] + step;
            } else if ( (cells[240 * 320 - 1] >= (seed - 1) * step) &&
                    ( (cells[240 * 319 - 2] == 0) || (cells[240 * 320 - 2] == 0) || (cells[238] == 0) ||
                    (cells[240 * 319 - 1] == 0) || (cells[239] == 0) || (cells[240 * 318] == 0) ||
                    (cells[240 * 319] == 0) || (cells[0]) == 0) ) {

                cells_buf[240 * 320 - 1] = 0;
            } else {
                cells_buf[240 * 320 - 1] = cells[240 * 320 - 1];
            }


            canvas.drawBitmap(cells_buf, 0, 240, 0, 0, 240, 320, false, null);

            tmp = cells;
            cells = cells_buf;
            cells_buf = tmp;

            endTime = (int) System.currentTimeMillis();
            curTime = 1000.0 / (endTime - startTime);
            startTime = (int) System.currentTimeMillis();

            fps = "FPS: " + (int) (curTime);
            canvas.drawText(fps, 10, 80, p);

            invalidate();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        matrix.setScale(3, 3, 0, 0);
        seed = 15;
        step = 4011;
        startTime = (int) System.currentTimeMillis();
        p.setARGB(255, 255, 255, 255);

        Random random = new Random();

        for (int j = 0; j < 320; j++) {
            for (int i = 0; i < 240; i++) {
                cells[240 * j + i] = random.nextInt(seed) * step;
            }
        }

        setContentView(new WhirlView(this));
    }
}