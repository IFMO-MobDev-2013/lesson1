package ru.ifmo.ctddev.koval.hw1;

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

public class AnimationActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new PointView(this));
    }

    private class PointView extends View {

        private final int WIDTH = 240, HEIGHT = 320;
        private final int DISPLAY_WIDTH, DISPLAY_HEIGHT;
        private final Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.RGB_565);
        private final int[] colors = new int[10];
        private final Paint textPaint = new Paint();
        private int[][] pixels;
        private long lastTime;
        private long fps;
        private int frameCount;
        private final int maxFrameCount = 10;

        public PointView(Context context) {
            super(context);

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            DISPLAY_WIDTH = size.x;
            DISPLAY_HEIGHT = size.y;

            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(50);
            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            frameCount = 0;
            lastTime = SystemClock.currentThreadTimeMillis();

            genColors();
            genPixels();
            setFocusable(true);
        }

        private void genColors() {
//            Random random = new Random();
//            for (int i = 0; i < colors.length; i++) {
//                colors[i] = random.nextInt();
//            }
            colors[0] = 0x00ffff;
            colors[1] = 0x00ff00;
            colors[2] = 0x0000ff;
            colors[3] = 0xff0000;
            colors[4] = 0xff00ff;
            colors[5] = 0xffff00;
            colors[6] = 0x800080;
            colors[7] = 0x000080;
            colors[8] = 0xFF4500;
            colors[9] = 0x8B0000;
        }

        private void genPixels() {
            pixels = new int[HEIGHT][WIDTH];
            Random random = new Random();
            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    pixels[i][j] = random.nextInt(colors.length);
                }
            }
        }

        private boolean haveSameNeighbours(int i, int j, int paintIndex) {
            if (pixels[(i - 1 + HEIGHT) % HEIGHT][(j - 1 + WIDTH) % WIDTH] == paintIndex) return true;
            if (pixels[(i - 1 + HEIGHT) % HEIGHT][j] == paintIndex) return true;
            if (pixels[(i - 1 + HEIGHT) % HEIGHT][(j + 1) % WIDTH] == paintIndex) return true;
            if (pixels[i][(j - 1 + WIDTH) % WIDTH] == paintIndex) return true;
            if (pixels[i][(j + 1) % WIDTH] == paintIndex) return true;
            if (pixels[(i + 1) % HEIGHT][(j - 1 + WIDTH) % WIDTH] == paintIndex) return true;
            if (pixels[(i + 1) % HEIGHT][j] == paintIndex) return true;
            if (pixels[(i + 1) % HEIGHT][(j + 1) % WIDTH] == paintIndex) return true;

            return false;
        }

        private void updatePixels() {
            int[][] newPixels = new int[HEIGHT][WIDTH];
            for (int i = 0; i < newPixels.length; i++) {
                for (int j = 0; j < newPixels[i].length; j++) {
                    int newPaintIndex = (pixels[i][j] + 1) % colors.length;
                    newPixels[i][j] = pixels[i][j];
                    if (haveSameNeighbours(i, j, newPaintIndex)) {
                        newPixels[i][j] = newPaintIndex;
                    }
                }
            }
            pixels = newPixels;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            updatePixels();

            int[] p = new int[WIDTH * HEIGHT];
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    p[i + j * WIDTH] = colors[pixels[j][i]];
                }
            }
            bitmap.setPixels(p, 0, WIDTH, 0, 0, WIDTH, HEIGHT);

            canvas.drawBitmap(createScaledBitmap(bitmap, DISPLAY_WIDTH, DISPLAY_HEIGHT, false), 0, 0, null);

            if (++frameCount == maxFrameCount) {
                long currentTime = SystemClock.currentThreadTimeMillis();
                frameCount = 0;
                fps = 1000 * maxFrameCount / (currentTime - lastTime);
                lastTime = currentTime;
            }
            canvas.drawText(" FPS: " + fps, DISPLAY_WIDTH - 200, DISPLAY_HEIGHT - 40, textPaint);

            invalidate();
        }
    }
}
