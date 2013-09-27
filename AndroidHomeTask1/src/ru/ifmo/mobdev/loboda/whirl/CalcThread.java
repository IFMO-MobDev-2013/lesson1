package ru.ifmo.mobdev.loboda.whirl;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Hashtable;
import java.util.Random;

class CalcThread extends Thread{
    private boolean runFlag = false;
    private boolean ready;
    private boolean needRecalc;
    private int[][] automaton;
    private int width;
    private int height;
    private Bitmap bitmap;
    private Hashtable<Integer, Integer> codeToColour;
    public CalcThread(int width, int height){
        Random random = new Random();
        runFlag = true;
        ready = false;
        this.width = width;
        this.height = height;
        automaton = new int [2][width * height];
        codeToColour = new Hashtable<Integer, Integer>();
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                automaton[0][i * width + j] = random.nextInt(16) * 16 + 0xff000000;
            }
        }
        bitmap = Bitmap.createBitmap(automaton[0], width, height, Bitmap.Config.ARGB_8888);
        ready = true;
    }

    public void recalc(){
        ready = false;
    }

    public void finish(){
        runFlag = false;
    }

    public boolean getReady(){
        return ready;
    }

    public Bitmap getBitMap(){
        return bitmap;
    }

    @Override
    public void run() {
        int stable = 0;
        int[] dy = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] dx = {-1, 0, 1, 1, 1, 0, -1, -1};
        while(runFlag){
            while(ready && runFlag){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            int other = (stable + 1) % 2;
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    int tmp = i * width + j;
                    int needCode = automaton[stable][tmp];
                    needCode += 16;
                    if(needCode > 0xff0000fe){
                        needCode = 0xff000000;
                    }
                    boolean good = false;
                    for(int k = 0; k < 8; ++k){
                        int ys = i + dx[k];
                        int xs = j + dy[k];
                        if(xs >= width){
                            xs = 0;
                        }
                        if(xs < 0){
                            xs = width - 1;
                        }
                        if(ys >= height){
                            ys = 0;
                        }
                        if(ys < 0){
                            ys = height - 1;
                        }
                        if(automaton[stable][ys * width + xs] == needCode){
                            automaton[other][tmp] = needCode;
                            good = true;
                            break;
                        }
                    }
                    if(!good){
                        automaton[other][tmp] =  automaton[stable][tmp];
                    }
                }
            }
            stable = other;
            bitmap = Bitmap.createBitmap(automaton[stable], width, height, Bitmap.Config.ARGB_8888);
            ready = true;
        }
    }
}
