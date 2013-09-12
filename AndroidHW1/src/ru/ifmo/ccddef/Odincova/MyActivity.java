package ru.ifmo.ccddef.Odincova;

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
import static android.graphics.Bitmap.createScaledBitmap;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // без заголовка
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // на полный экран
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WhirlView a = new WhirlView(this);
        setContentView(a);
    }

    class WhirlView extends View {
        private final int HEIGHT = 320;
        private final int WIDTH = 240;
        private final int COLOR = 10;
        private final int D_WIDTH;
        private final int D_HEIGHT;
        private int table[][];
        int newTable[][] = new int[HEIGHT][WIDTH];
        private int colors[];
        private int mas[];
        private final Paint paint = new Paint();
        private Random generator = new Random();
        private Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        private int tmp;
        private int stills;
        private long fps;
        private int nextI;
        private int nextJ;
        private int prevI;
        private int prevJ;

        public WhirlView(Context context) {
            super(context);

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            D_WIDTH = size.x;
            D_HEIGHT = size.y;

            colors = new int[COLOR];
            for (int i = 0; i < COLOR; i++) {
                paint.setARGB(255, generator.nextInt(255), generator.nextInt(255), generator.nextInt(255));
                colors[i] = paint.getColor();
            }

            paint.setTextSize(50);
            paint.setColor(Color.BLACK);

            table = new int [HEIGHT][WIDTH];
            mas = new int[HEIGHT * WIDTH];
            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    table[i][j] = generator.nextInt(COLOR);
                    mas[i * WIDTH + j] = colors[table[i][j]];
                }
            }
        }

        @Override
        public void onDraw(Canvas canvas) {

            bitmap.setPixels(mas, 0, WIDTH, 0, 0, WIDTH, HEIGHT);
            canvas.drawBitmap(createScaledBitmap(bitmap, D_WIDTH, D_HEIGHT, false), 0, 0, null);

            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    tmp = (table[i][j] + 1) % COLOR;
                    nextI = (i + 1) % HEIGHT;
                    nextJ = (j + 1) % WIDTH;
                    prevI = (i - 1 + HEIGHT) % HEIGHT;
                    prevJ = (j - 1 + WIDTH) % WIDTH;
                    if (tmp == table[nextI][j] ||
                            tmp == table[nextI][nextJ] ||
                            tmp == table[i][nextJ] ||
                            tmp == table[prevI][nextJ] ||
                            tmp == table[prevI][j] ||
                            tmp == table[prevI][prevJ] ||
                            tmp == table[nextI][prevJ] ||
                            tmp == table[i][prevJ]){
                        newTable[i][j] = tmp;
                        mas[i * WIDTH + j] = colors[tmp];
                    } else {
                        newTable[i][j] = table[i][j];
                        //   mas[i * WIDTH + j] = colors[tmp];
                    }
                }
            }

            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    table[i][j] = newTable[i][j];
                }
            }

            stills++;
            fps = 1000 * stills / SystemClock.currentThreadTimeMillis();
            canvas.drawText(" FPS = " + fps, D_WIDTH - 250, D_HEIGHT - 30, paint);

            invalidate();
        }
    }
}