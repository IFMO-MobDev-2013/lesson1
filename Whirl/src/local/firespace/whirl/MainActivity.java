package local.firespace.whirl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

	private TextView numbColors;
	private int colorsCount = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		numbColors = (TextView) findViewById(R.id.numb_colors);
		numbColors.setText(getString(R.string.seek_bar_desc) + Integer.toString(colorsCount));
		final SeekBar seekBar = (SeekBar)findViewById(R.id.seek_bar);
		seekBar.setOnSeekBarChangeListener(this);
	}

	@SuppressWarnings("UnusedParameters")
	public void setColors(View view) {
		Intent intent = new Intent(this, WhirlActivity.class);
		intent.putExtra(getString(R.string.numb_colors_intent), colorsCount);
		startActivity(intent);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		@SuppressWarnings("UnnecessaryBoxing") int x = Integer.valueOf(seekBar.getProgress());
		x = x / 5;
		colorsCount = x;
		numbColors.setText(getString(R.string.seek_bar_desc) + Integer.toString(colorsCount));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		@SuppressWarnings("UnnecessaryBoxing") int x = Integer.valueOf(seekBar.getProgress());
		x = x / 5;
		colorsCount = x;
		numbColors.setText(getString(R.string.seek_bar_desc) + Integer.toString(colorsCount));
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		@SuppressWarnings("UnnecessaryBoxing") int x = Integer.valueOf(seekBar.getProgress());
		x = x / 5;
		colorsCount = x;
		numbColors.setText(getString(R.string.seek_bar_desc) + Integer.toString(colorsCount));
	}
}