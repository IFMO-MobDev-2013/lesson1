package ru.ifmo.mobdev.whirl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.*;

import java.util.Random;

public class WhirlActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new WhirlView(this));
    }

}

class WhirlView extends View {

    int[][] field = null;
    int width = 0;
    int height = 0;
    int[] palette = {0xFFFFFFFF, 0xFF000000, 0xFF00FF00,
                     0xFFFF0000, 0xFF0000FF, 0xFFFFFF00,
                     0xFF00FFFF, 0xFF5F00FF, 0xFFFF6700,
                     0xFFCCFF00};

    final int MAX_COLOR = palette.length;
    Paint[] paintArray = null;
    Paint textColor = null;

    long oldTime = System.currentTimeMillis();

    public WhirlView(Context context) {
        super(context);
        width = 240;
        height = 320;
        paintArray = new Paint[MAX_COLOR];
        for (int i = 0; i < MAX_COLOR; i++) {
            paintArray[i] = new Paint();
            paintArray[i].setColor(palette[i]);
        }
        textColor = new Paint();
        textColor.setARGB(255, 255, 255, 255);
        initField();
    }


    void initField() {
        field = new int[width][height];
        Random rand = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                field[x][y] = rand.nextInt(MAX_COLOR);
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);


        updateField();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //canvas.drawRect(x, y, x + 1, y + 1, paintArray[field[x][y]]);
                bitmap.setPixel(x, y, palette[field[x][y]]);
            }
        }

        canvas.drawBitmap(bitmap, 0, 0, null);

        long delta = System.currentTimeMillis() - oldTime;
        oldTime = System.currentTimeMillis();
        float fps = 1000 / delta;
        canvas.drawText("fps " + fps, 900, 120, textColor);

        invalidate();
    }

    void updateField() {
        int[][] field2 = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                field2[x][y] = field[x][y];
                for (int dx = -1;  dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int x2 = x + dx;
                        int y2 = y + dy;
                        if (x2 < 0)
                            x2 += width;
                        if (y2 < 0)
                            y2 += height;
                        if (x2 >= width)
                            x2 -= width;
                        if (y2 >= height)
                            y2 -= height;
                        int f2 = (field[x][y] + 1) % MAX_COLOR;
                        if (f2 == field[x2][y2])
                            field2[x][y] = field[x2][y2];
                    }
                }
            }
        }
        field = field2;
    }
}
