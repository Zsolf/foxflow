package com.example.foxflow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.random;
import static java.lang.Math.sin;

public class MyCanvas extends RelativeLayout {

    Paint paint;
    Canvas canvas;
    ArrayList<Path> paths;


    public MyCanvas(Context context) {
        super(context);
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paths = new ArrayList<>();
        setWillNotDraw(false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
            this.canvas = canvas;
            super.onDraw(canvas);

            if(this.paths.size() != 0){
                for(int i =0 ; i < this.paths.size() ;i++){
                    this.canvas.drawPath(paths.get(i), this.paint);
                }
            }
    }



    public void drawArrow( float from_x, float from_y, float to_x, float to_y)
    {

        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);

        float angle,anglerad, radius, lineangle;

        //values to change for other appearance *CHANGE THESE FOR OTHER SIZE ARROWHEADS*
        radius=30f;
        angle=35f;

        //some angle calculations
        anglerad= (float) (PI*angle/180.0f);
        lineangle= (float) (atan2(to_y-from_y,to_x-from_x));

        //tha triangle
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(from_x,from_y);
        path.lineTo(to_x,to_y);
        path.moveTo(to_x, to_y);
        path.lineTo((float)(to_x-radius*cos(lineangle - (anglerad / 2.0))),
                (float)(to_y-radius*sin(lineangle - (anglerad / 2.0))));
        path.lineTo((float)(to_x-radius*cos(lineangle + (anglerad / 2.0))),
                (float)(to_y-radius*sin(lineangle + (anglerad / 2.0))));
        path.close();

        this.paths.add(path);
        this.invalidate();
    }

}
