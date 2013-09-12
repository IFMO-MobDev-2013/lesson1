package ru.panin.cellautomat;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class CellField extends View{
    private int[][] fieldValues;
    private final char N = 320, M = 240;
    private int cache = 0;
    private int lastUsed = -1, firstUpdating = 0, firstUpdated = 0;
    private long lastT;
    private boolean cycled = false;
    public TextView fps;

    public CellField(Context context, AttributeSet attrs) {
        super(context, attrs);
        lastT = System.currentTimeMillis();
        fieldValues = new int[16][N * M];
        Random r = new Random();
        for (int i = 0; i < N; i++){
            for (int j = 0; j < M; j++){
                fieldValues[0][i * M + j] = (char)(r.nextInt(16) * 16);
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        lastUsed = (lastUsed + 1) % 16;
        canvas.scale(1.0f * getWidth() / M, 1.0f * getHeight() / N);
        canvas.drawBitmap(fieldValues[lastUsed], 0, M, 0, 0, M, N, false, null);

        if (lastUsed == 1){
            long curT = System.currentTimeMillis();
            fps.setText(16000 / (curT - lastT) + " fps");
            lastT = curT;
        }
        if(!cycled)
            update();
        invalidate();
    }

    public void update() {
        int prevUpdating = firstUpdating % 16;
        int curUpdating = (++firstUpdating) % 16;
        System.out.println(firstUpdating + " " + firstUpdated);
        int dx, dy, x, y, idCur, idOther, newColor;
        boolean changedPX;

        for (int i = 0; i < N; i++){
            for (int j = 0; j < M; j++){
                idCur = i * M + j;
                fieldValues[curUpdating][idCur] = fieldValues[prevUpdating][idCur];
                changedPX = false;
                for (dx =  -1; dx < 2 && !changedPX; dx++){
                    for (dy = -1; dy < 2 && !changedPX; dy++){
                        x = i + dx;
                        x = x >= N ? (x - N) : (x < 0 ? (x + N) : x);
                        y = j + dy;
                        y = y >= M ? (y - M) : (y < 0 ? (y + M) : y);
                        idOther = x * M + y;
                        newColor = (fieldValues[prevUpdating][idCur] + 16);
                        newColor = newColor >= 256 ? (newColor - 256) : newColor;
                        if (fieldValues[prevUpdating][idOther] == newColor){
                            fieldValues[curUpdating][idCur] = newColor;
                            changedPX = true;
                        }
                    }
                }
            }
        }

        if(curUpdating == 0){
            int newCache = 0;
            for(int i = 0; i < N * M; i += N / 15){
                newCache *= 239;
                newCache += fieldValues[0][i];
            }
            cycled = newCache == cache;
            cache = newCache;
        }

        firstUpdated++;

    }

}
