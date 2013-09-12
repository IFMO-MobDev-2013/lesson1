package ifmo.androiddev.nechaev;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

public class WhirlAct extends Activity {
    int localWidth;
    int localHeight;
    class WhirlView extends View{
        long startTime = System.currentTimeMillis();
        int count = 0;
        int fpsCount = 0;
        private void FPS(){
            long thisTime = System.currentTimeMillis();
            count++;
            long time = thisTime - startTime;
            if (time > 1000){
                startTime = thisTime;
                fpsCount = (int) (count  / (time / 1000));
                count = 0;
            }

        }
        int[] col = new int[]{
                0xFFFFFFFF, 0xFFEEFFFF, 0xFFDDFFFF, 0xFFCCFFFF,
                0xFFBBFFFF, 0xFFAAFFFF, 0xFF99FFFF, 0xFF88FFFF,
                0xFF77FFFF, 0xFF66FFFF, 0xFF55FFFF, 0xFF44FFFF,
                0xFF33FFFF, 0xFF22FFFF, 0xFF11FFFF, 0xFF00FFFF
        };
        int h = 320, w = 240;
        int colorsCount = col.length;
        int[][] matInt = new int[h][w];
        int[] matCol = new int[w * h];

        public WhirlView(Context context) {
            super(context);
        }
        private void rand(){
            Random gen = new Random();
            for (int i = 0; i < h; i++){
                for (int j = 0; j < w; j++){
                    matInt[i][j] = gen.nextInt(colorsCount);
                }
            }
        }
        private void Generate(){
            int[][] newMatInt = new int[h][w];
            for (int y = 0; y < h; y ++){
                for (int x = 0; x < w; x ++){
                    int Junk = (matInt[y][x] + 1) % colorsCount;
                    if  (
                            (matInt[(y + h - 1) % h][x] == Junk) ||
                                    (matInt[(y + 1) % h][x] == Junk) ||
                                    (matInt[y][(x + w - 1) % w] == Junk) ||
                                    (matInt[y][(x + 1) % w] == Junk) ||
                                    (matInt[(y + h - 1) % h][(x + w - 1) % w] == Junk) ||
                                    (matInt[(y + 1) % h][(x + 1) % w] == Junk) ||
                                    (matInt[(y + 1) % h][(x + w - 1) % w] == Junk) ||
                                    (matInt[(y + h - 1) % h][(x + 1) % w] == Junk)){
                        newMatInt[y][x] = Junk;
                        matCol[y * w + x] = col[Junk];
                    } else {
                        newMatInt[y][x] = matInt[y][x];
                        matCol[y * w + x] = col[Junk];
                    }
                }

            }
            matInt = newMatInt;
        }
        int kol = 0;
        @Override
        public void onDraw(Canvas canvas) {
            if (kol == 0){
                rand();
                kol = 1;
            }
            Generate();
            canvas.scale((float)localWidth / (float)w, (float)localHeight / (float)h);
            canvas.drawBitmap(matCol, 0, w, 0, 0, w, h, true, null);
            Paint p = new Paint();
            p.setARGB(255, 0, 0, 0);
            FPS();
            canvas.drawText(String.valueOf(fpsCount), 25, 25, p);
            invalidate();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        localWidth = display.getWidth();
        localHeight = display.getHeight();
        setContentView(new WhirlView(this));
    }

}


