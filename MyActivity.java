package com.example.test1;

import java.util.Random;

import android.os.Bundle;
import android.graphics.Bitmap;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MyActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    TextView text;
    private int COLORSCOUNT;
    private int width = 240;
    private int height = 320;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        text = (TextView)findViewById(R.id.textView);
        text.setText("0");

        final SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        final Context cont = this;
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (COLORSCOUNT == 0) {
                    text.setText("0 is unsupported count of colors");
                } else setContentView(new PointView(cont));
            }
        });

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int x = Integer.valueOf(seekBar.getProgress());
        x = x / 5;
        COLORSCOUNT = x;
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int x = Integer.valueOf(seekBar.getProgress());
        x = x / 5;
        text.setText(String.valueOf(x));
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    class PointView extends View{
        public PointView(Context context) {
            super(context);
            Random x = new Random(System.nanoTime());
            for (int i = 1; i <= height; i++) {
                for (int j = 1; j <= width; j++) {
                    a[i][j] = x.nextInt(COLORSCOUNT);
                }
            }
            for (int i = 1; i <=width+1; i++) {
                a[0][i] = a[height][i];
            }
            for (int i = 1; i <=width+1; i++) {
                a[height+1][i] = a[1][i];
            }
            for (int i = 1; i <=height+1; i++) {
                a[i][0] = a[i][width];
            }
            for (int i = 1; i <=height+1; i++) {
                a[i][width+1] = a[i][1];
            }
            a[0][0] = a[height][width];
            a[height+1][width+1] = a[1][1];
            a[0][width+1] = a[height][1];
            a[height+1][0] = a[1][width];
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        }


        //final public int COLORSCOUNT = 12;
        final int[] COLORS = {0xffffff, 0x000000, 0x808080, 0x000080, 0xc0c0c0, 0x00ffff, 0x0000CD, 0x808000, 0x008080, 0x0000ff, 0x00ff00, 0x800080, 0xff00ff, 0x800000, 0xff0000, 0xffff00, 0x00FF7F, 0x8B0000, 0xFFA500, 0xBDB76B};
        Paint p = new Paint();
        int[][] a = new int[height+2][width+2];
        int[][] b = new int[height+2][width+2];
        int[] comp = new int[height * width + 1];
        Bitmap bitmap;

        protected long last = 0;
        protected int time = 0;
        protected float fps;

        public void onDraw(Canvas canvas) {
          //FPS
            ++time;
            long now = SystemClock.uptimeMillis();
            if (now - last > 1000) {
                fps = (float) time * 1000 / (now - last);
                time = 0;
                last = now;
            }

          //update
            int temp;
            int meter = 1;

           for (int i = 1; i <= height; i++) {
                for (int j = 1; j <= width; j++) {
                    temp = a[i][j] + 1; if (temp > COLORSCOUNT - 1) temp = 0;
                    if (a[i+1][j] == temp || a[i+1][j-1] == temp || a[i][j-1] == temp || a[i-1][j-1] == temp || a[i-1][j] == temp || a[i-1][j+1] == temp || a[i][j+1] == temp || a[i+1][j+1] == temp) {
                        b[i][j] = temp;
                    }
                    comp[meter] = COLORS[b[i][j]];
                    meter++;
                    if (i == 1) { b[height+1][j] = b[i][j];}
                    if (j == 1) { b[i][width+1] = b[i][j];}
                    if (i == height) {b[0][j] = b[i][j];}
                    if (j == width) {b[i][0] = b[i][j];}
                    if (i == 1 && j == 1) {b[height+1][width+1] = b[i][j];}
                    if (i == 1 && j == width) {b[height+1][0] = b[i][j];}
                    if (i == height && j == 1) {b[0][width+1] = b[i][j];}
                    if (i == height && j == width) {b[0][0] = b[i][j];}
                }
            }
            for (int i = 0; i <= height + 1; i++) {
                for (int j = 0; j <= width + 1; j++) {
                    a[i][j] = b[i][j];
                }
            }
            bitmap.setPixels(comp, 1, width, 0, 0, width, height);

           //draw
            float XX = (float) canvas.getWidth() / width;
            float YY = (float) canvas.getHeight() / height;
            canvas.scale(XX, YY);
            canvas.drawBitmap(bitmap, 0, 0, p);
            canvas.scale((float) 1 / XX, (float) 1 / YY);
            p.setARGB(127, 0, 0, 0);
            canvas.drawRect(0, 0, 150, 50, p);
            p.setARGB(255, 255, 255, 255);
            p.setTextSize(20);
            canvas.drawText("FPS " + fps, 10, 30, p);
            postInvalidate();
        }
    }
}


