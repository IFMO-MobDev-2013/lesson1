package com.example.First;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class FirstActivity extends Activity {
	private static FirstView myView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		myView = new FirstView(this, width, height);
		setContentView(myView);
	}
}
