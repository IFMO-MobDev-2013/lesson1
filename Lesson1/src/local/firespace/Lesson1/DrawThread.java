package local.firespace.Lesson1;

import android.graphics.*;
import android.os.SystemClock;
import android.view.SurfaceHolder;

/**
 * Created by FireSpace on 29.12.13.
 */
public class DrawThread extends Thread {
	private static final int WIDTH = MainActivity.getScreenWidth();
	private static final int HEIGHT = MainActivity.getScreenHeight();
	private static final float RATIO_WIDTH = (float)WIDTH / (float)Field.X;
	private static final float RATIO_HEIGHT = (float)HEIGHT / (float)Field.Y;

	private boolean runFlag = false;

	private SurfaceHolder surfaceHolder;
	private Bitmap picture = Bitmap.createBitmap(Field.X, Field.Y, Bitmap.Config.ARGB_8888);
	private Paint paint = new Paint();
	private Field field = new Field();

	private long lastSecond, currentSecond, tick;

	private void drawFps(final Canvas canvas) {
		tick++;
		currentSecond = SystemClock.uptimeMillis();
		canvas.drawText("FPS " + (tick*1000 / (currentSecond - lastSecond)), 40, 40 ,paint);
		if (lastSecond + 1000 < currentSecond) {
			lastSecond = currentSecond;
			tick = 0;
		}
	}

	public DrawThread(SurfaceHolder surfaceHolder) {
		paint.setTextSize(30);
		paint.setARGB(127, 0, 0, 0);
		lastSecond = SystemClock.uptimeMillis();
		this.surfaceHolder = surfaceHolder;
	}

	public void setRunning(boolean run) {
		runFlag = run;
	}

	@Override
	public void run() {
		while(runFlag) {
			Canvas canvas = null;

			try {
				canvas = surfaceHolder.lockCanvas(null);
				synchronized (surfaceHolder) {
					if (canvas != null) {
						onDraw(canvas);
					}
				}
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	public void onDraw(final Canvas canvas) {
		int[] pixels = field.updateField();
		picture.setPixels(pixels, 0, Field.X, 0, 0, Field.X, Field.Y);
		canvas.scale(RATIO_WIDTH, RATIO_HEIGHT);
		canvas.drawBitmap(picture, 0, 0, null);
		drawFps(canvas);
	}
}
