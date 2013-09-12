package ru.smelik.whirlview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.graphics.Canvas;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;

import java.util.Random;


public class WhirlActivity extends Activity
        implements SeekBar.OnSeekBarChangeListener {

    public int check = 1;
    TextView mTextValue;
    int colorsCount = 16;
    int localWidth, localHeight;
    int width = 240;
    int height = 320;

    private int[] color = {0x082567, 0x000080, 0x120a8f, 0x120a8f, 0x1a4780, 0x0047ab, 0x0047ab, 0x007ba7,
                           0x3a75c4, 0x0095b6, 0x6495ed, 0xc8a2c8, 0x9966cc, 0x735184, 0x8b00ff, 0x6600ff,
                           0xff47ca, 0xff00ff, 0x911e42, 0xc71585, 0x8a3324, 0x560319, 0x92000a, 0x1faee9,
                           0x4d7198, 0x3b5998, 0x960018, 0xd53e07, 0xff0000, 0xff4f00, 0xe32636, 0xfaeedd, 0x7fffd4};

    class WhirlView extends View {

        private int count = 0;                                  // framerate
        private long fpsCount = 0;
        private long lastTime = System.currentTimeMillis();
        private int[][] mas = new int[height][width];




        private int[] colors = new int[height * width];


        public WhirlView(Context context) {
            super(context);
        }

        private void fps() {
            long currentTime = System.currentTimeMillis();
            count++;
            long time = currentTime - lastTime;
            if (time > 1000) {
                lastTime = currentTime;
                fpsCount = count / (time / 1000);
                count = 0;
            }
        }

        private void randomArrayCreator() {
            Random generator = new Random();
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++) {
                    mas[i][j] = generator.nextInt(colorsCount);
                }

            check = 0;
        }

        private void arrayChanging() {
            int[][] newMas = new int[height][width];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int newState = (mas[i][j] + 1) % colorsCount;
                    if (newState == mas [(i + height - 1) % height][j]
                            || newState == mas [(i + 1) % height][j]
                            || newState == mas [i][(j + width - 1) % width]
                            || newState == mas [i][(j + 1) % width]
                            || newState == mas [(i + height - 1) % height][(j + width - 1) % width]
                            || newState == mas [(i + height - 1) % height][(j + 1) % width]
                            || newState == mas [(i + 1) % height][(j + width - 1) % width]
                            || newState == mas [(i + 1) % height][(j + 1) % width]) {
                        newMas[i][j] = newState;
                        colors[i * width + j] = color[newState];
                    } else {
                        newMas[i][j] = mas[i][j];
                        colors[i * width + j] = color[mas[i][j]];
                    }
                }
            }

            mas = newMas;
        }

        @Override
        public void onDraw(Canvas canvas) {

            if (check == 1) {
                randomArrayCreator();
            }

            arrayChanging();

            canvas.scale((float)localWidth / (float)width, (float)localHeight / (float)height);
            canvas.drawBitmap(colors, 0, width, 0, 0, width, height, false, null);

            canvas.scale(0.5f, 0.5f);
            Paint p = new Paint();
            fps();
            p.setARGB(255, 255, 255, 255);
            p.setTextSize(20);
            canvas.drawText("FPS: " + fpsCount, 20, 20, p);
            invalidate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        localWidth = display.getWidth();
        localHeight = display.getHeight();

        setContentView(R.layout.activity_whirl);

        final SeekBar seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(this);
        seekbar.setProgress(colorsCount);
        mTextValue = (TextView)findViewById(R.id.textView);
        mTextValue.setText("Count of colors: " + String.valueOf(colorsCount));

        final EditText editText1 = (EditText)findViewById(R.id.editText);
        final EditText editText2 = (EditText)findViewById(R.id.editText2);

        final Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                width = Integer.parseInt(editText1.getText().toString());
                height = Integer.parseInt(editText2.getText().toString());
                setContentView(new WhirlView(context));
            }
        });

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        colorsCount = seekBar.getProgress();
        mTextValue.setText("Count of colors: " + String.valueOf(colorsCount));
    }
}

