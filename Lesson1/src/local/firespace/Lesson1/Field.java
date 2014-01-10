package local.firespace.Lesson1;

import android.graphics.Color;

import java.util.Random;


public class Field {
	public static final int X = 240;
	public static final int Y = 320;
	private static final int T_X = X-1;
	private static final int T_Y = Y-1;
	private static final int COLOUR_COUNT = 10;

	private int currentField[][];
	private int updateField[][];
	private int colours[];
	private int pixels[];

	private void initializeColoursAndField() {
		colours[0] = Color.argb(255, 255, 0, 0);
		colours[1] = Color.argb(255, 0, 255, 0);
		colours[2] = Color.argb(255, 0, 0, 255);
		colours[3] = Color.argb(255, 0, 255, 255);
		colours[4] = Color.argb(255, 255, 0, 255);
		colours[5] = Color.argb(255, 255, 255, 0);
		colours[6] = Color.argb(255, 255, 255, 255);
		colours[7] = Color.argb(255, 153, 204, 153);
		colours[8] = Color.argb(255, 102, 204, 0);
		colours[9] = Color.argb(255, 102, 153, 102);

		Random rand = new Random();

		for (int i = 0; i < X; i++) {
			for (int j = 0; j < Y; j++) {
				currentField[i][j] = rand.nextInt(COLOUR_COUNT);
				updateField[i][j] = currentField[i][j];
			}
		}
	}

	private boolean check(final int i, final int j) {
		int temp = (currentField[i][j] + 1)%COLOUR_COUNT;
		return  temp == currentField[i-1][j] ||
				temp == currentField[i+1][j] ||
				temp == currentField[i-1][j-1] ||
				temp == currentField[i][j-1] ||
				temp == currentField[i+1][j-1] ||
				temp == currentField[i-1][j+1] ||
				temp == currentField[i][j+1] ||
				temp == currentField[i+1][j+1];
	}

	public Field() {
		currentField = new int[X][Y];
		updateField = new int[X][Y];
		colours = new int[COLOUR_COUNT];
		pixels = new int[X*Y];
		initializeColoursAndField();
	}

	public int[] updateField() {

		int temp = (currentField[0][0]+1)%COLOUR_COUNT;
		if (temp == currentField[0][1] ||
			temp == currentField[1][0] ||
			temp == currentField[1][1] ||
			temp == currentField[0][T_Y] ||
			temp == currentField[T_X][0] ||
			temp == currentField[T_X][T_Y] ||
			temp == currentField[T_X][1] ||
			temp == currentField[1][T_Y]) {

			updateField[0][0] = temp;
		} else {
			updateField[0][0] = currentField[0][0];
		}

		temp = (currentField[T_X][0]+1)%COLOUR_COUNT;
		if (temp == currentField[T_X-1][0] ||
			temp == currentField[T_X][1] ||
			temp == currentField[T_X-1][1] ||
			temp == currentField[T_X][T_Y] ||
			temp == currentField[0][1] ||
			temp == currentField[T_X-1][T_Y] ||
			temp == currentField[0][0] ||
			temp == currentField[0][T_Y]) {

			updateField[T_X][0] = temp;
		} else {
			updateField[T_X][0] = currentField[T_X][0];
		}

		temp = (currentField[0][T_Y]+1)%COLOUR_COUNT;
		if (temp == currentField[0][T_Y-1] ||
			temp == currentField[1][T_Y] ||
			temp == currentField[1][T_Y-1] ||
			temp == currentField[T_X][T_Y-1] ||
			temp == currentField[T_X][T_Y] ||
			temp == currentField[T_X][0] ||
			temp == currentField[0][0] ||
			temp == currentField[1][0]) {

			updateField[0][T_Y] = temp;
		} else {
			updateField[0][T_X] = currentField[0][T_X];
		}

		temp = (currentField[T_X][T_Y]+1)%COLOUR_COUNT;
		if (temp == currentField[T_X-1][T_Y] ||
			temp == currentField[T_X][T_Y-1] ||
			temp == currentField[T_X-1][T_Y-1] ||
			temp == currentField[0][T_Y-1] ||
			temp == currentField[0][T_Y] ||
			temp == currentField[0][0] ||
			temp == currentField[T_X][0] ||
			temp == currentField[T_X-1][0]) {

			updateField[T_X][T_Y] = temp;
		} else {
			updateField[T_X][T_Y] = currentField[T_X][T_Y];
		}

		for (int i = 1; i < T_X; i++) {
			temp = (currentField[i][0]+1)%COLOUR_COUNT;
			if (temp == currentField[i-1][0] ||
				temp == currentField[i+1][0] ||
				temp == currentField[i-1][1] ||
				temp == currentField[i][1] ||
				temp == currentField[i+1][1] ||
				temp == currentField[i-1][T_Y] ||
				temp == currentField[i][T_Y] ||
				temp == currentField[i+1][T_Y]) {

				updateField[i][0] = temp;
			} else {
				updateField[i][0] = currentField[i][0];
			}

			temp = (currentField[i][T_Y]+1)%COLOUR_COUNT;
			if (temp == currentField[i-1][T_Y] ||
				temp == currentField[i+1][T_Y] ||
				temp == currentField[i-1][T_Y-1] ||
				temp == currentField[i][T_Y-1] ||
				temp == currentField[i+1][T_Y-1] ||
				temp == currentField[i-1][0] ||
				temp == currentField[i][0] ||
				temp == currentField[i+1][0]) {

				updateField[i][T_Y] = temp;
			} else {
				updateField[i][T_Y] = currentField[i][T_Y];
			}
		}
		for (int i = 1; i < T_Y; i++) {
			temp = (currentField[0][i]+1)%COLOUR_COUNT;
			if (temp == currentField[0][i-1] ||
				temp == currentField[0][i+1] ||
				temp == currentField[1][i-1] ||
				temp == currentField[1][i] ||
				temp == currentField[1][i+1] ||
				temp == currentField[T_X][i-1] ||
				temp == currentField[T_X][i] ||
				temp == currentField[T_X][i+1]) {

				updateField[0][i] = temp;
			} else {
				updateField[0][i] = currentField[0][i];
			}

			temp = (currentField[T_X][i]+1)%COLOUR_COUNT;
			if (temp == currentField[T_X][i-1] ||
				temp == currentField[T_X][i+1] ||
				temp == currentField[T_X-1][i-1] ||
				temp == currentField[T_X-1][i] ||
				temp == currentField[T_X-1][i+1] ||
				temp == currentField[0][i-1] ||
				temp == currentField[0][i] ||
				temp == currentField[0][i+1]) {

				updateField[T_X][i] = temp;
			} else {
				updateField[T_X][i] = currentField[T_X][i];
			}
		}

		for (int i = 1; i < T_X; i++) {
			for (int j = 1; j < T_Y; j++) {
				if (check(i, j)) {
					updateField[i][j] = (currentField[i][j] + 1)%COLOUR_COUNT;
				} else {
					updateField[i][j] = currentField[i][j];
				}
			}
		}

		int iteratorPixels = 0;
		for (int i = 0; i < X; i++) {
			for (int j = 0; j < Y; j++) {
				currentField[i][j] = updateField[i][j];
				pixels[iteratorPixels++] = colours[currentField[i][j]];
			}
		}

		return pixels;
	}
}
