package com.example.home1;







import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import java.util.Random;


class WhirlView extends View {
	final int h = 240;
	final int w = 320;
	int[][] table = new int[h][w];
	int[][] new_table = new int[h][w];	
	int[] draw_table = new int[h * w];
	long begin;
	long end;
	final int number_colour = 11;
	Paint paint = new Paint();
    Random random = new Random();
    int[] colors = {Color.GREEN, Color.DKGRAY,  Color.BLUE, Color.GRAY, Color.CYAN, Color.RED,  Color.BLACK, Color.LTGRAY, 
            Color.TRANSPARENT,Color.BLACK,Color.YELLOW};
    public WhirlView(Context context){
        super(context);
        for (int i = 0; i < h; ++i)
        	for (int j = 0; j < w; ++j)
        		table[i][j] = random.nextInt(number_colour);
        begin = System.currentTimeMillis();
        paint.setColor(Color.BLACK);
    }
    private void change(){
    	for (int i = 0; i < h; ++i)
    		for (int j = 0; j < w; ++j){
    			int e = (table[i][j] + 1) % number_colour;
    			if (e == table[(i + 1 + h) % h][j] ){
    				new_table[i][j] = table[(i + 1 + h) % h][j]; 
    			} else
    			if (e == table[(i - 1 + h) % h][j] ){
    				new_table[i][j] = table[(i - 1 + h) % h][j]; 
    			} else
    				if (e == table[i][(j + 1 + w )% w] ){
        				new_table[i][j] = table[i][(j + 1 + w) % w]; 
        			}
    				else 
    					if (e == table[i][(j - 1 + w )% w] ){
            				new_table[i][j] = table[i][(j - 1 + w) % w]; 
            			}
    					else
    			            if (e == table[(i + 1 + h) % h][(j + 1 + w )% w] ){
    				            new_table[i][j] = table[(i + 1 + h) % h][(j + 1 + w) % w]; 
    			            }
    			            else
    			            	if (e == table[(i - 1 + h) % h][(j + 1 + w )% w] ){
    			    				new_table[i][j] = table[(i - 1 + h) % h][(j + 1 + w) % w]; 
    			    			} 
    			            	else
    			            		if (e == table[(i + 1 + h) % h][(j - 1 + w )% w] ){
    			        				new_table[i][j] = table[(i + 1 + h) % h][(j - 1 + w) % w]; 
    			        			} 
    			            		else
    			        				if (e == table[(i - 1 + h) % h][(j - 1 + w )% w] ){
    			            				new_table[i][j] = table[(i - 1 + h) % h][(j - 1 + w) % w]; 
    			            			}
    			        				else
    			        					new_table[i][j] = table[i][j];
    			    				
    			
    		}
    	for (int i = 0; i < h; ++i)
    		for (int j = 0; j < w; ++j)
    			table[i][j] = new_table[i][j];
    }
    @Override
    public void onDraw(Canvas canvas) {
        int t = 0;
        change();
    	for (int i = 0; i < w; ++i)
        	for (int j = 0; j < h; ++j){
        	   	draw_table[t++] = colors[table[j][i]];
        	   	
        	}
    	canvas.scale(3, 4);
        canvas.drawBitmap(draw_table, 0, 240, 0, 0, 240, 320, true, paint);
        end = System.currentTimeMillis();
        canvas.drawText("FPS: " + 1000 / (end - begin), 10, 30, paint);
        begin = end;
        invalidate();
    }
}

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new WhirlView(this));
    }
}
