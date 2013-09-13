package ru.mermakov.projects.Whirl;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.util.Random;

public class MyActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new WhirlView(this));
    }

    class WhirlView extends View {
        private final int WIDTH = 240;
        private final int HEIGHT = 320;
        private final int MAX_COLORS = 17;
        private int[] colors = new int[MAX_COLORS + 1];//17 colors + fps color
        private Display display;
        private Point point;
        private int displayWidth;
        private int displayHeight;
        private int[][] field, field2;
        private int[] col = new int[WIDTH * HEIGHT];
        private long time1;
        private int frame;
        private double fps;
        private Paint paint;
        private Matrix matrix;

        public WhirlView(Context context) {
            super(context);
            initRes();
        }

        private void initColors() {
            colors[0] = Color.argb(255, 189, 255, 7);
            colors[1] = (Color.argb(255, 232, 150, 12));
            colors[2] = (Color.argb(255, 255, 0, 0));
            colors[3] = (Color.argb(255, 85, 12, 232));
            colors[4] = (Color.argb(255, 13, 218, 255));
            colors[5] = (Color.argb(255, 255, 245, 11));
            colors[6] = (Color.argb(255, 232, 97, 9));
            colors[7] = (Color.argb(255, 236, 3, 255));
            colors[8] = (Color.argb(255, 23, 112, 232));
            colors[9] = (Color.argb(255, 9, 255, 58));
            colors[10] = (Color.argb(255, 255, 158, 15));
            colors[11] = (Color.argb(255, 232, 4, 105));
            colors[12] = (Color.argb(255, 8, 22, 255));
            colors[13] = (Color.argb(255, 27, 232, 149));
            colors[14] = (Color.argb(255, 255, 254, 5));
            colors[15] = (Color.argb(255, 18, 255, 171));
            colors[16] = (Color.argb(255, 232, 30, 206));
            colors[17] = (Color.BLACK);
        }

        private void initRes() {
            display = getWindowManager().getDefaultDisplay();
            point = new Point();
            display.getSize(point);
            displayHeight = point.y;
            displayWidth = point.x;
            field = new int[HEIGHT][WIDTH];
            field2 = new int[HEIGHT][WIDTH];
            initColors();
            initField();
            frame = 0;
            paint = new Paint();
            paint.setColor(Color.BLACK);
            matrix = new Matrix();
            matrix.setScale((float) displayWidth / WIDTH, (float) displayHeight / HEIGHT);
        }

        private void initField() {
            Random myrnd = new Random();
            for (int i = 0; i < HEIGHT; ++i)
                for (int j = 0; j < WIDTH; ++j)
                    field[i][j] = myrnd.nextInt(MAX_COLORS);
        }

        private void updateFiled() {
            int tmp = 0;
            for (int i = 0; i < HEIGHT; ++i) {
                int x, X;
                if (i - 1 == -1)
                    x = HEIGHT - 1;
                else
                    x = i - 1;
                if (i + 1 == HEIGHT)
                    X = 0;
                else
                    X = i + 1;
                for (int j = 0; j < WIDTH; ++j) {
                    int cur = field[i][j] + 1;
                    if (cur == MAX_COLORS)
                        cur = 0;
                    int y, Y;
                    if (j - 1 == -1)
                        y = WIDTH - 1;
                    else
                        y = j - 1;
                    if (j + 1 == WIDTH)
                        Y = 0;
                    else
                        Y = j + 1;
                    if (cur == field[X][Y] || cur == field[X][j]
                            || cur == field[X][y] || cur == field[x][Y]
                            || cur == field[x][j] || cur == field[x][y]
                            || cur == field[i][Y] || cur == field[i][y]) {
                        col[tmp++] = colors[field2[i][j] = cur];
                    } else {
                        col[tmp++] = colors[field2[i][j] = field[i][j]];
                    }
                }
            }
            int[][] temp = field;
            field = field2;
            field2 = temp;
        }

        @Override
        public void onDraw(Canvas canvas) {
            updateFiled();
            canvas.setMatrix(matrix);
            canvas.drawBitmap(col, 0, WIDTH, 0, 0, WIDTH, HEIGHT, false, null);
            frame++;
            long tmp1 = SystemClock.uptimeMillis();
            long tmp2 = tmp1 - time1;
            if(tmp2 > 1000)
            {
                fps = frame * 1000 / (double)tmp2;
                frame = 0;
                time1 = tmp1;
            }
            canvas.drawText(Math.ceil(fps * 10) / 10 + " FPS", 5, 10, paint);
            invalidate();
        }
    }
}
