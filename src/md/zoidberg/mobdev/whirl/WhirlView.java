package md.zoidberg.mobdev.whirl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

import java.text.DecimalFormat;
import java.util.Random;

class WhirlView extends View {
    public WhirlView(Context ctx) {
        super(ctx);
        initField(240, 320);
    }

    private int fieldWidth;
    private int fieldHeight;

    private float scaleFactorX = 1.0f;
    private float scaleFactorY = 1.0f;

    private int[][] field;
    private int[][] nextGeneration;

    private int[] colors;
    private Matrix identity = new Matrix();
    private Paint textPaint = new Paint();
    {
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(20);
        textPaint.setFakeBoldText(true);
    }

    private long[] lastUpdate = new long[10];
    private int lastFrame = 0;
    private DecimalFormat fpsFormat = new DecimalFormat("fps ##.##");

    private static final int COLOR_NUMBER = 16;
    private int[] palette;


    private void initField(int w, int h) {
        this.fieldWidth = 240;
        this.fieldHeight = 320;
        field = new int[h][w];
        nextGeneration = new int[h][w];

        colors = new int[h*w];

        // Pleasant white-green palette.
        palette = new int[COLOR_NUMBER];
        for (int i = 0; i < COLOR_NUMBER; i++) {
            palette[i] = Color.argb(0, 255 - (255*i/ COLOR_NUMBER), 255, 255 - (255*i/ COLOR_NUMBER));
        }

        Random rand = new Random();

        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                field[y][x] = rand.nextInt(COLOR_NUMBER);
            }
        }
    }

    public void recalculateField() {
        for (int y = 0; y < fieldHeight; y++) {
            for (int x = 0; x < fieldWidth; x++) {
                updatePoint(x, y, field, nextGeneration);
            }
        }

        int[][] temp = field;
        field = nextGeneration;
        nextGeneration = temp;
    }

    private boolean updatePoint(int x, int y, int[][] originalField, int[][] newField) {
        // Copy-paste code. Can't be much faster.
        int L = (x - 1 < 0? fieldWidth - 1 : x - 1);
        int R = (x + 1 >= fieldWidth ? 0 : x + 1);
        int P = (y - 1 < 0? fieldHeight + y - 1 : y - 1);
        int N = (y + 1 >= fieldHeight ? y - fieldHeight + 1 : y + 1);
        int nextColor = (originalField[y][x] + 1) % COLOR_NUMBER;
        if (originalField[P][L] == nextColor ||
                originalField[P][x] == nextColor ||
                originalField[P][R] == nextColor ||
                originalField[y][L] == nextColor ||
                originalField[y][R] == nextColor ||
                originalField[N][L] == nextColor ||
                originalField[N][x] == nextColor ||
                originalField[N][R] == nextColor) {
            newField[y][x] = nextColor;
            return true;
        } else {
            newField[y][x] = field[y][x];
            return false;
        }
    }

    private void redrawScreen(Canvas screen) {
        // Finding out colours
        for (int y = 0; y < fieldHeight; y++) {
            for (int x = 0; x < fieldWidth; x++) {
                colors[y* fieldWidth + x] = palette[field[y][x]];
            }
        }

        // Scaling canvas to interpolate 240x320 to native resolution
        screen.scale(scaleFactorX, scaleFactorY);
        screen.drawBitmap(colors, 0, fieldWidth, 0.0F, 0.0F, fieldWidth, fieldHeight, false, null);

        // Now for FPS counting. I'm averaging over last 10 frames.
        long timeDelta = System.nanoTime() - lastUpdate[lastFrame % 10];
        float fps = 10000000000.0f/timeDelta; // dragons ahoy! 10^10 = 10^9 * 10 (nanoseconds / 10 frames)

        // I would want to draw text in native resolution, so I scale canvas back to 1:1 dimensions
        screen.setMatrix(identity);
        if (lastFrame > 10) screen.drawText(fpsFormat.format(fps), 20.0f, 40.0f, textPaint);
        screen.drawText("frame " + Integer.toString(lastFrame), 20.0f, 60.0f, textPaint);

        lastUpdate[lastFrame % 10] = System.nanoTime();
        lastFrame++;
    }

    @Override
    public void onDraw(Canvas canvas) {
        recalculateField();
        redrawScreen(canvas);
        invalidate();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        initField(w, h);
        scaleFactorX = ((float)w)/fieldWidth;
        scaleFactorY = ((float)h)/fieldHeight;
    }
}