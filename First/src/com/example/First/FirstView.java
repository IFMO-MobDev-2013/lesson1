package com.example.First;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class FirstView extends View {
	private final int COLORS_COUNT = 13;
	private final int WIDTH = 240;
	private final int HEIGHT = 320;
	private final int NUM = WIDTH * HEIGHT;

	private int[] colors = new int[COLORS_COUNT];
	private int[] bitmap = new int[NUM];
	private int[][] field = new int[HEIGHT][WIDTH];
	private int[][] fieldCopy = new int[HEIGHT][WIDTH];
	private long lastFpsTime;
	private double lastFpsValue;
	private int frames;
	private float scale_x;
	private float scale_y;

	public FirstView(Context context, int width, int height) {
		super(context);
		frames = 0;
		lastFpsTime = System.currentTimeMillis();
		lastFpsValue = 0.0;
		scale_x = ((float) width) / WIDTH;
		scale_y = ((float) height) / HEIGHT;

		for (int i = 0; i < COLORS_COUNT; ++i)
			colors[i] = (int) (Math.random() * 0xFFFFFF);

		for (int i = 0; i < HEIGHT; ++i)
			for (int j = 0; j < WIDTH; ++j)
				fieldCopy[i][j] = field[i][j] = (int) (Math.random() * COLORS_COUNT);
	}

	private boolean willNewColor(int i, int j) {
		int ni, nj;
		int cur = field[i][j];
		for (int di = -1; di <= 1; ++di) {
			for (int dj = -1; dj <= 1; ++dj) {
				if ((di == dj) && (di == 0)) continue;
				ni = i + di;
				nj = j + dj;
				if (ni < 0) ni = HEIGHT - 1;
				if (nj < 0) nj = WIDTH - 1;
				if (ni == HEIGHT) ni = 0;
				if (nj == WIDTH) nj = 0;
				if (cur + 1 == field[ni][nj] |
						(cur == COLORS_COUNT - 1 && field[ni][nj] == 0)) return true;
			}
		}
		return false;
	}

	private void redraw() {
		for (int p = 0, i = 0, j = 0; p < NUM; ++p, ++j) {
			if (j == WIDTH) {
				++i;
				j = 0;
			}
			if (willNewColor(i, j)) {
				fieldCopy[i][j]++;
				if (fieldCopy[i][j] == COLORS_COUNT)
					fieldCopy[i][j] = 0;
			}

			bitmap[p] = colors[field[i][j]];
		}

		for (int i = 0; i < HEIGHT; ++i)
			System.arraycopy(fieldCopy[i], 0, field[i], 0, WIDTH);
	}

	private void fps(Canvas canvas) {
		frames++;
		long time;
		Paint p = new Paint();
		p.setARGB(255, 0, 0, 200);
		if (frames >= 10) {
			time = System.currentTimeMillis();
			lastFpsValue = (frames * 1000) / ((double) time - lastFpsTime);
			lastFpsTime = time;
			frames = 0;
		}
		canvas.drawText("FPS: " + lastFpsValue, 20, 10, p);
	}

	@Override
	public void onDraw(Canvas canvas) {
		redraw();
		canvas.scale(scale_x, scale_y);
		canvas.drawBitmap(bitmap, 0, WIDTH, 0, 0, WIDTH, HEIGHT, false, null);
		fps(canvas);
		postInvalidate();
	}
}
