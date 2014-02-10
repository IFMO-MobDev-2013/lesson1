package local.firespace.whirl;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class WhirlActivity extends Activity {

	private WhirlView view;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int countColors = getIntent().getIntExtra(getString(R.string.numb_colors_intent), 0);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		view = new WhirlView(this, countColors, width, height);
		setContentView(view);
	}
}