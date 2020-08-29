package com.example.hackathon;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

public class MyCanvas extends View {

    Paint paint , imagePaint;
    float pxConversion = getResources().getDisplayMetrics().density;
    float x, y;
    String path;


    public MyCanvas(Context context , String path) {
        super(context);

        paint = new Paint();
        imagePaint = new Paint(Paint.DITHER_FLAG);
        this.path = path;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        x = displayMetrics.widthPixels / 2 ;
        y = displayMetrics.heightPixels / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(BitmapFactory.decodeFile(path), 0, 0 , imagePaint);
    }
}
