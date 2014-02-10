package local.firespace.whirl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.View;

public class WhirlView extends View {

	private final int COLORS_COUNT;
	private static final int WIDTH = 240;
	private static final int HEIGHT = 320;
	private static final int PIX_COUNT = WIDTH*HEIGHT;
	private int[] colors;
	private int[] toDraw = new int[PIX_COUNT];
	private int[][] field = new int[HEIGHT][WIDTH];
	private int[][] updateField = new int [HEIGHT][WIDTH];

	private float scaleX;
	private float scaleY;

	private long last = 0;
	private int time = 0;
	private float fps;
	private Paint paint = new Paint();

	private void initField() {
		for (int i = 0; i < COLORS_COUNT; i++) {
			colors[i] = (int) (Math.random() * 0xFFFFFF + 1);
		}

		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				field[i][j] = (int) (Math.random() * COLORS_COUNT);
				updateField[i][j] = field[i][j];
			}
		}
	}

	public WhirlView(Context context, int countColors, int width, int height) {
		super(context);
		COLORS_COUNT = countColors;
		colors = new int[COLORS_COUNT];
		scaleX = ((float) width) / WIDTH;
		scaleY = ((float) height) / HEIGHT;

		initField();
	}

	private boolean willChangeColor(int i, int j) {
		int ni, nj;
		int cur = field[i][j];
		for (int di = -1; di < 2; di++) {
			for (int dj = -1; dj < 2; dj++) {
				if (di == dj && di == 0) {
					continue;
				}
				ni = i + di;
				nj = j + dj;
				if (ni < 0) ni = HEIGHT - 1;
				if (nj < 0) nj = WIDTH - 1;
				if (ni == HEIGHT) ni = 0;
				if (nj == WIDTH) nj = 0;

				if (cur + 1 == field[ni][nj] || (cur == COLORS_COUNT - 1 && field[ni][nj] == 0)) {
					return true;
				}
			}
		}
		return false;
	}

	private void recount() {
		for (int p = 0, i = 0, j = 0; p < PIX_COUNT; p++, j++) {
			if (j == WIDTH) {
				i++;
				j = 0;
			}

			toDraw[p] = colors[field[i][j]];

			if (willChangeColor(i, j)) {
				updateField[i][j]++;
				if (updateField[i][j] == COLORS_COUNT)
					updateField[i][j] = 0;
			}

		}


		for (int i = 0; i < HEIGHT; ++i) {
			System.arraycopy(updateField[i], 0, field[i], 0, WIDTH);
		}
	}

	private void fps(Canvas canvas) {
		++time;
		long now = SystemClock.uptimeMillis();
		if (now - last > 1000) {
			fps = (float) time * 1000 / (now - last);
			time = 0;
			last = now;
		}

		paint.setARGB(127, 0, 0, 0);
		canvas.drawRect(0, 0, 150, 50, paint);
		paint.setARGB(255, 255, 255, 255);
		paint.setTextSize(20);
		canvas.drawText("FPS " + fps, 10, 30, paint);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		recount();

		canvas.scale(scaleX, scaleY);
		canvas.drawBitmap(toDraw, 0, WIDTH, 0, 0, WIDTH, HEIGHT, false, null);

		fps(canvas);

		postInvalidate();
	}
}
