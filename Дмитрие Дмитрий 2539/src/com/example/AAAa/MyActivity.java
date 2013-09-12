package com.example.AAAa;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.View;
import static android.graphics.Bitmap.createScaledBitmap;

import java.util.Random;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    class WhirlView extends View {
        private final int COLLOR_SIZE = 10;
        private final int col[];
        private int length = 0;
        private int map[][];
        private int sup[];
        private int tmp[];
        private long fps;
        private long this_time = 0;
        private Point size = new Point();
        private Paint text = new Paint();
        private final int HEIGHT = 320;
        private final int REAL_HEIGHT;
        private final int REAL_WEIGHT;
        private final int WEIGHT = 240;
        private int big_size = HEIGHT * WEIGHT;
        private Bitmap bitmap = Bitmap.createBitmap(WEIGHT, HEIGHT, Bitmap.Config.ARGB_8888);
        private Random rnd = new Random();
        public WhirlView(Context context){
            super(context);
            text.setTextSize(40);
            text.setColor(Color.BLACK);
            Display display = getWindowManager().getDefaultDisplay();
            size = new Point();
            display.getSize(size);
            REAL_HEIGHT = size.y;
            REAL_WEIGHT = size.x;
            col = new int[COLLOR_SIZE];
            tmp = new int[big_size];
            for(int i = 0; i < COLLOR_SIZE; i++){
                Paint sup = new Paint();
                sup.setARGB(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
                col[i] = sup.getColor();//255 * 255 * rnd.nextInt(255) + rnd.nextInt(255) * 255 + rnd.nextInt(255);
            }

            map = new int[WEIGHT][HEIGHT];
            sup = new int[big_size];
            for(int i = 0; i < WEIGHT; i++){
                for(int j = 0; j < HEIGHT; j++) {
                    map[i][j] = rnd.nextInt(COLLOR_SIZE);
                    tmp[j * WEIGHT + i] = col[map[i][j]];
                    //sup[i] = 0;
                }
            }
        }


        @Override
        public void onDraw(Canvas canvas) {
            //sup = new int[big_size];
            fps++;
            length = 0;
            bitmap.setPixels(tmp, 0, WEIGHT, 0, 0, WEIGHT, HEIGHT);
            canvas.drawBitmap(createScaledBitmap(bitmap, REAL_WEIGHT, REAL_HEIGHT, false), 0, 0, null);
            for(int i = 0; i < WEIGHT; i++){
                for(int j = 0; j < HEIGHT; j++){
                    int temp = (map[i][j] + 1) % COLLOR_SIZE;
                    int h = (i - 1 + WEIGHT) % WEIGHT;
                    int h1 = (i + 1) % WEIGHT;
                    int w = (j - 1 + HEIGHT) % HEIGHT;
                    int w1 = (j + 1) % HEIGHT;
                    if(map[i][w] == temp  ||
                        map[i][w1] == temp  ||
                        map[h][j] == temp ||
                        map[h1][j] == temp  ||
                        map[h1][w] == temp  ||
                        map[h1][w1] == temp  ||
                        map[h][w] == temp  ||
                        map[h][w1] == temp
                        ){
                            sup[length] = i * HEIGHT + j;
                            length++;
                            tmp[j * WEIGHT + i] = col[temp];
                    }
                }
            }
            for(int i = 0; i < length; i++){
                int s = sup[i] / HEIGHT;
                int s1 = sup[i] % HEIGHT;
                //tmp[s] = (tmp[s] + 1) % COLLOR_SIZE;
                map[s][s1] = (map[s][s1] + 1) % COLLOR_SIZE;//(map[s / WEIGHT][s % WEIGHT] + 1) & COLLOR_SIZE;
            }

            //System.arraycopy(sup, 0, map, 0, big_size);
            //fps = 1000 / (SystemClock.currentThreadTimeMillis() - this_time);
            //this_time = SystemClock.currentThreadTimeMillis();
            canvas.drawText("FPS = " + fps * 1000 / SystemClock.currentThreadTimeMillis(), 100, 100, text);

            invalidate();
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new WhirlView(this));
    }
}
