package com.gosugdr.whirl;

import java.util.Arrays;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	WhirlView whirlView;

	class WhirlView extends SurfaceView implements Runnable {
		int level = 0;
		int[][] field;
		int[][] field2;
		int fieldForBitmap[];
		int width = 0;
		int halfWidth = 0;
		int height = 0;
		float scaleX;
		float scaleY;
		final int MAX_COLOR = 10;
		int[] palette = { 0xFFE0E4CC, 0xFFA7DBD8, 0xFF598D3F, 0xFFF38630,
				0xFFBC5133, 0xFF95DEB2, 0xFF489B80, 0xFF9E991E, 0xFF7F490D,
				0xFFBC5133 };
		SurfaceHolder holder;
		Thread thread = null;
		volatile boolean running = false;
		Paint paint = new Paint();
		Bitmap picture;

		
		
		public WhirlView(Context context) {
			super(context);
			holder = getHolder();
		}

		public void resume() {
			running = true;
			thread = new Thread(this);
			thread.start();
		}

		public void pause() {
			running = false;
			try {
				thread.join();
			} catch (InterruptedException ignore) {
			}
		}

		@SuppressLint("WrongCall")
		public void run() {
			while (running) {
				if (holder.getSurface().isValid()) {
					long startTime = System.nanoTime();
					Canvas canvas = holder.lockCanvas();
					
					try {
						updateField();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					onDraw(canvas);
					holder.unlockCanvasAndPost(canvas);
					long finishTime = System.nanoTime();
					Log.i("TIME", "Circle: " + ((finishTime - startTime)
							/ 1000000));
				}
			}
		}

		@Override
		public void onSizeChanged(int w, int h, int oldW, int oldH) {
			width = 240;
			halfWidth = 240 / 2;
			height = 320;
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			scaleX = (float) (((float) size.x) / width);
			scaleY = (float) (((float) size.y) / height);
			initField();
		}

		void initField() {
			field = new int[width][height];
			field2 = new int[width][height];
			fieldForBitmap = new int[width * height];
			Random rand = new Random();
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					field[x][y] = rand.nextInt(MAX_COLOR);
				}
			}
			picture = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
		}
		
		class Calc extends Thread {
			int start;
			int finish;
			
			
			public Calc(int start, int finish) {
				super();
				this.start = start;
				this.finish = finish;
			}


			@Override
			public void run() {
				for (int x = start; x < finish; x++) {
					int[] f2x = field2[x];
					int[] fx = field[x];
					for (int y = 0; y < height; y++) {
						f2x[y] = fx[y];
						for (int dx = -1; dx <= 1; dx++) {
							for (int dy = -1; dy <= 1; dy++) {
								int x2 = x + dx;
								int y2 = y + dy;
								if (x2 < 0)
									x2 += width;
								if (y2 < 0)
									y2 += height;
								if (x2 >= width)
									x2 -= width;
								if (y2 >= height)
									y2 -= height;
								final int val = field[x2][y2];
								int val2 = fx[y] + 1;
								if (val2 == MAX_COLOR) {
									val2 = 0;
								}
								if (val2 == val) {
									f2x[y] = val;
								}
							}
						}
					}
				}
				for (int i = start; i < finish; i++) {
					int[] js = field[i];
					for (int j = 0; j < height; j++) {
						fieldForBitmap[i + j * width] = palette[js[j]];
					}
				}
			}
		}

		void updateField() throws InterruptedException {
			
			Thread t1 = new Calc(0, width / 2);
			Thread t2 = new Calc((width / 2), width);
			
			t1.start();
			t2.start();
			t1.join();
			t2.join();
			
			picture.setPixels(fieldForBitmap, 0, width, 0, 0, width, height);
			int[][] t = field;
			field = field2;
			field2 = t;
		}

		@Override
		public void onDraw(Canvas canvas) {
			canvas.scale(scaleX, scaleY);
			canvas.drawBitmap(picture, 0, 0, null);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		whirlView.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		whirlView.pause();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		whirlView = new WhirlView(this);
		setContentView(whirlView);
	}
}
