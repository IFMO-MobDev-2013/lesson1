package local.firespace.Lesson1;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by FireSpace on 26.12.13.
 */

public class MainActivity extends Activity {

	private static int screenWidth;
	private static int screenHeight;

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenHeight = size.y;
		screenWidth = size.x;


		super.onCreate(savedInstanceState);
		setContentView(new WhirlView(this));
	}
}