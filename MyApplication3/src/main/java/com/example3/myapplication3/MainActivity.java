package com.example3.myapplication3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.Random;

public class MainActivity extends Activity {

    class WhirlView extends View {
        public WhirlView(Context contex) {
            super(contex);
        }

        Paint p;
        int count = 0;
        boolean drw = true;
        long fpscount = 0;
        long lastTime = System.currentTimeMillis();
        int[] colors;
        int w = 24 * 2; //квадратиков в ширину
        int h = 32 * 2; //квадратиков в высоту
        int a = 5; //сторона квадрата
        int num = 16; //количество цветов
        int[] thisCol;
        int[] thisColCopy;
        int[] nextCol;
        int[] nextColCopy;
        int width, height;
        int[] x = {-1, 0, 1, -1, 0, 1, 1, -1};
        int[] y = {-1, -1, -1, 1 , 1, 1, 0, 0};
        boolean green  = false, blue = false, violet = false, red = false, orange = false, black = false;

         public void fps(Canvas canvas) {
            long currTime = System.currentTimeMillis();
            count++;
            long time = currTime - lastTime;
            if (time > 1000) {
                lastTime = currTime;
                fpscount = count / (time / 1000);
                count = 0;
            }
            p.setARGB(255, 0, 0, 0);
             canvas.drawText("FPS:" + Long.toString(fpscount, 10), w - 70, h - 20, p);
         }

        public void setFieldSize() {
            Display display =((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            width = display.getWidth();
            height = display.getHeight();
            a = Math.max(1, Math.min(width / 320, height / 240));
            w = width / a + 1;
            h = height / a + 1;
        }

        private void setColors() {
            if (blue) {
                colors[0] = 0xFF191970;
                colors[1] = 0x00008E;
                colors[2] = 0x0000CD;
                colors[3] = 0x0000FF;
                colors[4] = 0x4169E1;
                colors[5] = 0x7B68EE;
                colors[6] = 0x6495ED;
                colors[7] = 0x1E90FF;
                colors[8] = 0x00BFFF;
                colors[9] = 0x87CEFA;
                colors[10] = 0x40E0D0;
                colors[11] = 0x00CED1;
                colors[12] = 0x48D1CC;
                colors[13] = 0xAFEEEE;
                colors[14] = 0x4682B4;
                colors[15] = 0x000080;
            } else if (green) {
                colors[0] = 0x008080;
                colors[1] = 0x008B8B;
                colors[2] = 0x20B2AA;
                colors[3] = 0x66CDAA;
                colors[4] = 0x8FBC8F;
                colors[5] = 0x556B2F;
                colors[6] = 0x808000;
                colors[7] = 0x6B8E23;
                colors[8] = 0x006400;
                colors[9] = 0x008000;
                colors[10] = 0x228B22;
                colors[11] = 0x98EE90;
                colors[12] = 0x9ACD32;
                colors[13] = 0x32CD32;
                colors[14] = 0x3CB371;
                colors[15] = 0x2E8B57;
            } else if (violet) {
                //Violet & pink
                colors[0] = 0x483D8B;
                colors[1] = 0x6A5ACD;
                colors[2] = 0x4B0082;
                colors[3] = 0x800080;
                colors[4] = 0x9932CC;
                colors[5] = 0x9400D3;
                colors[6] = 0x8A2BE3;
                colors[7] = 0x9370DB;
                colors[8] = 0xBA55D3;
                colors[9] = 0xFF00FF;
                colors[10] = 0xDA70D6;
                colors[11] = 0xEE82EE;
                colors[12] = 0xFF69B4;
                colors[13] = 0xFF1493;
                colors[14] = 0xC71585;
                colors[15] = 0x8B008B;
            } else if (red) {
                colors[0] = 0xDDADAF;
                colors[1] = 0xCC8899;
                colors[2] = 0xDB7093;
                colors[3] = 0xDE3163;
                colors[4] = 0xE6114F;
                colors[5] = 0xE32636;
                colors[6] = 0xFA0909;
                colors[7] = 0xFA0942;
                colors[8] = 0xFF2400;
                colors[9] = 0xA42C1F;
                colors[10] = 0x9B2D30;
                colors[11] = 0x92000A;
                colors[12] = 0x800000;
                colors[13] = 0x900020;
                colors[14] = 0x560319;
                colors[15] = 0x45161C;
            } else if (orange) {
                colors[0] = 0xA08040;
                colors[1] = 0xD2B48C;
                colors[2] = 0xEEE6A3;
                colors[3] = 0xF2E8C9;
                colors[4] = 0xFaEEDD;
                colors[5] = 0xE3A971;
                colors[6] = 0xFF9966;
                colors[7] = 0xE9967A;
                colors[8] = 0xFF8C69;
                colors[9] = 0xFF7F50;
                colors[10] = 0xE97451;
                colors[11] = 0xF36223;
                colors[12] = 0xD5481C;
                colors[13] = 0xD5713F;
                colors[14] = 0xD77D31;
                colors[15] = 0xCD7F32;
            }
            //green
            colors[16] = 0x008080;
            colors[17] = 0x008B8B;
            colors[18] = 0x20B2AA;
            colors[19] = 0x66CDAA;
            colors[20] = 0x8FBC8F;
            colors[21] = 0x556B2F;
            colors[22] = 0x808000;
            colors[23] = 0x6B8E23;
            colors[24] = 0x006400;
            colors[25] = 0x008000;
            colors[26] = 0x228B22;
            colors[27] = 0x98EE90;
            colors[28] = 0x9ACD32;
            colors[29] = 0x32CD32;
            colors[30] = 0x3CB371;
            colors[31] = 0x2E8B57;

        }

        private void firstFilling() {
            p = new Paint();
            Random generator = new Random();

            colors = new int [32];
            thisCol = new int [h * w]; //цвета квадратиков текущего поля
            thisColCopy = new int [h * w];
            nextCol = new int [h * w]; //следующий цвет каждого квадратика
            nextColCopy = new int [h * w];
            p.setTextSize(20); //размер текста при выводе фпс

            blue = true; //пусть волны будут синими
            setColors(); //заполнение массива цветов - потом  меню с выбором цвета надо сделать

            //начальное заполнение массива
            int x;
            int ind;
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    x = generator.nextInt(num);
                    ind = i * w + j;
                    thisCol[ind] = thisColCopy[ind] = colors[x];
                    x++;
                    nextCol[ind] = nextColCopy[ind] = colors[x % num];
                }
            }
            drw = false;
        }

        private class DrawThread implements Runnable {
            private int hb, he, wb, we;

            public DrawThread(int hb, int he, int wb, int we) {
                this.hb = hb;
                this.he = he;
                this.wb = wb;
                this.we = we;
            }

            @Override
            public void run() {
                int ind;
                int newI;
                int newJ;
                int cell;
                int nextInd;

                for (int i = hb; i < he; i++) {
                    for (int j = wb; j < we; j++) {
                        ind = i * w + j;
                        cell = nextColCopy[ind];
                        for (int k = 0; k < 8; k++) {
                            newI = i + y[k];
                            newJ = j + x[k];
                            nextInd = newI * w + newJ;
                            if (thisColCopy[nextInd] == cell) {
                                thisCol[ind] = thisColCopy[nextInd];
                                nextCol[ind] = nextColCopy[nextInd];
                                break;
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onDraw(Canvas canvas) {
            if (drw) {
                setFieldSize();
                firstFilling();
            }

            canvas.scale(a, a);
            canvas.drawBitmap(thisColCopy, 0, w, 0, 0, w, h, false, null);

            //разделим матрицу на 4 части и каждую часть пересчитаем отдельным потоком
            Thread th1 = new Thread(new DrawThread(1, h / 2, 1, w / 2));
            Thread th2 = new Thread(new DrawThread(1, h / 2, w / 2, w - 1));
            Thread th3 = new Thread(new DrawThread(h / 2, h - 1, 1, w / 2));
            Thread th4 = new Thread(new DrawThread(h / 2, h - 1, w / 2, w - 1));

            th1.start();
            th2.start();
            th3.start();
            th4.start();

            try {
                th1.join();
                th2.join();
                th3.join();
                th4.join();
            } catch (InterruptedException e) {}

            int ind;
            int newI;
            int newJ;
            int cell;
            int nextInd;

            for (int i = 1; i < h - 1; i++) {
                for (int j = 0; j < w; j += w - 1) {
                    ind = i * w + j;
                    cell = nextColCopy[ind];
                    for (int k = 0; k < 8; k++) {
                        newI = i + y[k];
                        if (newI == h) newI = 0;
                        else if (newI < 0) newI = h - 1;
                        newJ = j + x[k];
                        if (newJ == w) newJ = 0;
                        else if (newJ < 0) newJ = w - 1;

                        nextInd = newI * w + newJ;

                        if (thisColCopy[nextInd] == cell) {
                            thisCol[ind] = thisColCopy[nextInd];
                            nextCol[ind] = nextColCopy[nextInd];
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < h; i += h - 1) {
                for (int j = 0; j < w; j++) {
                    ind = i * w + j;
                    cell = nextColCopy[ind];
                    for (int k = 0; k < 8; k++) {
                        newI = i + y[k];
                        if (newI == h) newI = 0;
                        else if (newI < 0) newI = h - 1;
                        newJ = j + x[k];
                        if (newJ == w) newJ = 0;
                        else if (newJ < 0) newJ = w - 1;

                        nextInd = newI * w + newJ;

                        if (thisColCopy[nextInd] == cell) {
                            thisCol[ind] = thisColCopy[nextInd];
                            nextCol[ind] = nextColCopy[nextInd];
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < h * w; i++) {
                thisColCopy[i] = thisCol[i];
                nextColCopy[i] = nextCol[i];
            }

            fps(canvas);
            invalidate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new WhirlView(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}