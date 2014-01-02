package local.firespace.Lesson1;

import android.content.Context;
import android.view.*;

/**
 * Created by FireSpace on 29.12.13.
 */
public class WhirlView extends SurfaceView implements SurfaceHolder.Callback {

	private DrawThread drawThread;

	public WhirlView(Context context) {
		super(context);
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		drawThread = new DrawThread(getHolder());
		drawThread.setRunning(true);
		drawThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		drawThread.setRunning(false);
		boolean retry = true;
		while(retry) {
			try {
				drawThread.join();
				retry = false;
			} catch (InterruptedException ignore) { /*while not close*/ }
		}
	}
}
