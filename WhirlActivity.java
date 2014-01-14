package com.mobdev.whirl;

import android.app.Activity;
import android.os.Bundle;

public class WhirlActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WhirlView myWhirlView = new WhirlView(this);
		setContentView(myWhirlView);
	}
}

