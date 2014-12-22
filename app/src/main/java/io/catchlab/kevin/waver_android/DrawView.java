package io.catchlab.kevin.waver_android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by kevin on 14/12/23.
 */
public class DrawView extends View {
    public  int numberOfWaves = 5;
    public float frequency = 1.5f;
    public float density = 15.f;
    public float phaseShift = -0.25f;
    public float phase = 0.f;
    public float maxAmplitude = 0;

    private  ArrayList<Paint> paintsArray= new ArrayList<>();
    private  ArrayList<Path> pathArray= new ArrayList<>();
    private  int ViewWidth = 0;
    private  int ViewHeight = 0;
    private  float ViewMid = 0;
    public float amplitude = 0;

    public DrawView(Context context) {
        super(context);

        Resources res = getResources();

        for (int i = 0; i < numberOfWaves; i++)
        {
          float progress = 1.0f - (float)i / numberOfWaves;
          float multiplier = Math.min(1.0f, (progress / 3.0f * 2.0f) + (1.0f / 3.0f));

          if (i == 0){
              Paint p = new Paint();
              p.setColor(Color.WHITE);
              p.setStrokeWidth(res.getDimension(R.dimen.waver_width));
              p.setStyle(Paint.Style.STROKE);


              paintsArray.add(p);
          }else{
              Paint p = new Paint();
              Log.v("Color", ""+ (int)(( 1.0f * multiplier * 0.4) * 255));
              p.setColor(Color.WHITE);
              p.setAlpha((int)(( 1.0f * multiplier * 0.4) * 255));
              p.setStrokeWidth(res.getDimension(R.dimen.waver_width_min));
              p.setStyle(Paint.Style.STROKE);
              paintsArray.add(p);
          }
        }


    }


    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        ViewWidth = xNew;
        ViewHeight = yNew;
        ViewMid = ViewWidth /2.0f;
        maxAmplitude = ViewHeight/2.f - 4.0f;

        Log.v("Waver", "width=" + ViewWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i  = 0; i< numberOfWaves; i++)
        {
            Path path2 = new Path();

            float progress = 1.f - (i / (float)numberOfWaves);
            float normedAmplitude = (1.5f * progress - 0.5f) * amplitude;

            for(float x = 0.f; x< ViewWidth+density; x += density) {

                //Thanks to https://github.com/stefanceriu/SCSiriWaveformView
                // We use a parable to scale the sinus wave, that has its peak in the middle of the view.
                double scaling = -Math.pow(x / ViewMid  - 1.f, 2.f) + 1.f; // make center bigger

                double y = scaling * maxAmplitude * normedAmplitude * Math.sin(2 * 3.14159 *(x / ViewWidth) * frequency + phase) + ViewHeight/2.0;

                if (x==0.f) {

                    path2.moveTo(x, (float)y);
                }
                else {
                    path2.lineTo(x, (float)y);
                }
            }
            Paint p = paintsArray.get(i);
            canvas.drawPath(path2, p);
        }




    }



}