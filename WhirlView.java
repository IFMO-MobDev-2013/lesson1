package com.mobdev.whirl;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.View;

class WhirlView extends View {
	Bitmap bitmap;
	Paint paint = new Paint();
	public int[][] colourMap = new int[320][240];
	int[][] colourMapCopy = new int[320][240];
	int[] compiledColors = new int[320 * 240];
	int colors[] = new int[] { 0xff0b00, 0x1c1717, 0x082567, 0x075367, 0x884535, 0xf984e5, 0x8ff984, 0xffcc00, 0xfaeedd, 0xeee6a3 };
	protected long last = 0;
	protected long now;
	protected int time = 0;
	protected float fps;
	
	public WhirlView(Context context) {
		super(context);
		Random rand = new Random(System.nanoTime());
		for (int i = 0; i < 320; ++i)
			for (int j = 0; j < 240; ++j)
				colourMap[i][j] = rand.nextInt(10);
		bitmap = Bitmap.createBitmap(240, 320, Bitmap.Config.RGB_565);
	}

	public void nextcol() {
		int i = 0, j = -1;
		for (int k = 0; k < 320 * 240; ++k) {
			++j;
			if (j == 240) {
				++i;
				j = 0;
			}
			int a = i == 0 ? 319 : i - 1, b = j == 0 ? 239 : j - 1, c = i == 319 ? 0
					: i + 1, d = j == 239 ? 0 : j + 1, e = colourMap[i][j] == 9 ? 0
					: colourMap[i][j] + 1;
			if (colourMap[a][b] == e)
				colourMapCopy[i][j] = colourMap[a][b];
			else if (colourMap[a][j] == e)
				colourMapCopy[i][j] = colourMap[a][j];
			else if (colourMap[a][d] == e)
				colourMapCopy[i][j] = colourMap[a][d];
			else if (colourMap[i][b] == e)
				colourMapCopy[i][j] = colourMap[i][b];
			else if (colourMap[i][d] == e)
				colourMapCopy[i][j] = colourMap[i][d];
			else if (colourMap[c][b] == e)
				colourMapCopy[i][j] = colourMap[c][b];
			else if (colourMap[c][j] == e)
				colourMapCopy[i][j] = colourMap[c][j];
			else if (colourMap[c][d] == e)
				colourMapCopy[i][j] = colourMap[c][d];
			else
				colourMapCopy[i][j] = colourMap[i][j];
			compiledColors[k] = colors[colourMapCopy[i][j]];

		}
		int[][] x = colourMap;
		colourMap = colourMapCopy;
		colourMapCopy = x;
		bitmap.setPixels(compiledColors, 0, 240, 0, 0, 240, 320);
	}

	public void measureFPS() {
		++time;
		now = SystemClock.uptimeMillis();
		if (now - last > 1000) {
			fps = (float) time * 1000 / (now - last);
			time = 0;
			last = now;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		measureFPS();
		nextcol();
		float sX = (float) canvas.getWidth() / 240;
		float sY = (float) canvas.getHeight() / 320;
		canvas.scale(sX, sY);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		canvas.scale((float) 1 / sX, (float) 1 / sY);
		paint.setARGB(127, 0, 0, 0);
		canvas.drawRect(0, 0, 150, 50, paint);
		paint.setARGB(255, 255, 255, 255);
		paint.setTextSize(20);
		canvas.drawText("FPS " + fps, 10, 30, paint);
		postInvalidate();
	}
}