package com.example.pole;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.View;

import java.util.Random;

public class MyActivity extends Activity {
    public int[][] a=new int[11][3];
    public int[][] x=new int[8][2];
    public int[][] b=new int[720][560];
    public int[][] c=new int[720][560];
    public int[] pi=new int[720*560+10];
    public float k1,k2,hi,wi;
    public int w,h;
    public long time;
    public int ans,fps;
    public Paint p = new Paint();
    Random random=new Random();

    class WhirlView extends View {
        public WhirlView(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {

            for(int i=0;i<w;i++)
                for(int j=0;j<h;j++)
                    if (i==0 || j==0 || i==w-1 || j==h-1)
                {
                    int z=(b[i][j]+1)%10,f=0;
                    for(int k=0;k<8;k++)
                        if ( b[(i+x[k][0]+h)%h][(j+x[k][1]+w)%w] == z){f=1;break;}
                    if (f==1)
                        c[i][j]=z;
                    else c[i][j]=b[i][j];
                } else
                    {
                        int z=(b[i][j]+1)%10,f=0;
                        for(int k=0;k<8;k++)
                            if ( b[i+x[k][0]][j+x[k][1]] == z){f=1;break;}
                        if (f==1)
                            c[i][j]=z;
                        else c[i][j]=b[i][j];
                    }
            int k=0;

                for(int j=0;j<w;j++)
                    for(int i=0;i<h;i++)
                {
                    b[j][i]=c[j][i];
                    pi[k++]=Color.argb(255,a[b[j][i]][0],a[b[j][i]][1],a[b[j][i]][2]);
                }
            canvas.scale(k1,k2);
            canvas.drawBitmap(pi, 0, h,0,0,h,w,true,null);

            ans++;
            if (SystemClock.uptimeMillis()-time>=1000)
            {
                time=SystemClock.uptimeMillis();
                fps=ans;
                ans=0;
            }

            canvas.drawText("fps=" + fps,5,30,p);
            invalidate();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        fps=1;
        h=240;
        w=320;
        Display display=getWindowManager().getDefaultDisplay();
        wi=display.getWidth();
        hi=display.getHeight();
        k1=wi/240;
        k2=hi/320;
        a[0][0]=255;a[0][1]=0;a[0][2]=0;
        a[1][0]=0;a[1][1]=255;a[1][2]=0;
        a[2][0]=0;a[2][1]=0;a[2][2]=255;
        a[3][0]=255;a[3][1]=255;a[3][2]=0;
        a[4][0]=255;a[4][1]=0;a[4][2]=255;
        a[5][0]=0;a[5][1]=255;a[5][2]=255;
        a[6][0]=255;a[6][1]=255;a[6][2]=255;
        a[7][0]=0;a[7][1]=0;a[7][2]=0;
        a[8][0]=232;a[8][1]=21;a[8][2]=221;
        a[9][0]=48;a[9][1]=205;a[9][2]=198;
        for(int i=0;i<w;i++)
            for(int j=0;j<h;j++)
            {
                int q;
                q=(int)random.nextInt(10);
                b[i][j]=q;
            }
        x[0][0]=0;x[0][1]=1;
        x[1][0]=0;x[1][1]=-1;
        x[2][0]=1;x[2][1]=0;
        x[3][0]=-1;x[3][1]=0;
        x[4][0]=1;x[4][1]=1;
        x[5][0]=-1;x[5][1]=1;
        x[6][0]=1;x[6][1]=-1;
        x[7][0]=-1;x[7][1]=-1;
        p.setARGB(255, 0,0,0);
        p.setTextSize(20);
        time=SystemClock.uptimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(new WhirlView(this));
    }
}

