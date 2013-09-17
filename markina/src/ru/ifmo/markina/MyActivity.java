package ru.ifmo.markina;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

import static android.graphics.Bitmap.createScaledBitmap;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new WhirlView (this));
    }

    private class WhirlView extends View {

        private int width = 240;
        private int height = 320;
        private int cntDraw = 0;
        private long timeBegin = 0;
        private long time = 0;
        private long fps = 0;
        private Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        private int t = 0;
        private double ans;
        private int[] palette =
            {0x00FF33,
            0x00CC00,
            0x009900,
            0x0066CC,
            0x006666,
            0x0033CC,
            0x0000CC,
            0x0000CC,
            0xFFFF00,
            0xFFFFCC,
            0xFF3300,
            0xFF00FF,
            0x990033,
            0x9966FF,
            0x99CC33,
            0x99FFFF};
        private Paint text = new Paint();
        private int NatWidth, NatHeight;
        private int[][] table;



        public WhirlView (Context context) {
            super(context);

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            NatWidth = size.x;
            NatHeight = size.y;

            text.setColor(Color.WHITE);
            text.setTextSize(50);
            text.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            genTable();

        }

        private void genTable() {
            table = new int[height][width];
            Random random = new Random();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    table[i][j] = random.nextInt(palette.length);
                }
            }
        }

        private boolean updateField(int i, int j, int index) {

            int tx, ty;
            tx = i - 1;
            if(tx < 0) tx += height;
            ty = j - 1;
            if(ty < 0) ty += width;
            if(table[tx][ty] == index) return true;

            tx = i - 1;
            if(tx < 0) tx += height;
            ty = j;
            if(table[tx][ty] == index) return true;

            tx = i - 1;
            if(tx < 0) tx += height;
            ty = j + 1;
            if(ty >= width) ty -= width;
            if(table[tx][ty] == index) return true;

            tx = i;
            ty = j - 1;
            if(ty < 0) ty += width;
            if(table[tx][ty] == index) return true;

            tx = i;
            ty = j + 1;
            if(ty >= width) ty -= width;
            if(table[tx][ty] == index) return true;

            tx = i + 1;
            if(tx >= height) tx -= height;
            ty = j - 1;
            if(ty < 0) ty += width;
            if(table[tx][ty] == index) return true;

            tx = i + 1;
            if(tx >= height) tx -= height;
            ty = j;
            if(table[tx][ty] == index) return true;

            tx = i + 1;
            if(tx >= height) tx -= height;
            ty = j + 1;
            if(ty >= width) ty -= width;
            if(table[tx][ty] == index) return true;

            return false;
        }

        private void updateTable() {
            int[][] tableCopy = new int[height][width];
            for (int i = 0; i < tableCopy.length; i++) {
                for (int j = 0; j < tableCopy[i].length; j++) {
                    int ColorIndex = (table[i][j] + 1) % palette.length;
                    tableCopy[i][j] = table[i][j];
                    if (updateField(i, j, ColorIndex)) {
                        tableCopy[i][j] = ColorIndex;
                    }
                }
            }
            table = tableCopy;
        }

        @Override
        protected void onDraw(Canvas canvas) {

            timeBegin = System.currentTimeMillis();
            updateTable();

            int[] p = new int[width * height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    p[i + j * width] = palette[table[j][i]];
                }
            }
            bitmap.setPixels(p, 0, width, 0, 0, width, height);

            canvas.drawBitmap(createScaledBitmap(bitmap, NatWidth, NatHeight, false), 0, 0, null);
            cntDraw++;
            time = System.currentTimeMillis();
            if(t < 5) {
                t++;
                ans += time - timeBegin;
            }
            if(t == 5) {
                fps = (int) (1000 / (ans / 5));
                ans = 0;

                t = 0;
            }
            canvas.drawText("" + fps, NatWidth - 100, NatHeight - 50, text);

            invalidate();
        }
    }
}