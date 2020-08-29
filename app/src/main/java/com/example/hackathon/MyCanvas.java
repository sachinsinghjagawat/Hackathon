package com.example.hackathon;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MyCanvas extends View {


    private static final float BLUR_RADIUS = 25f;

    Paint paint , imagePaint , textPaint , doodlePaint , filterPaint , selectedPaint;
    float pxConversion = getResources().getDisplayMetrics().density;
    float x, y , xText , yText , xPos , yPos;
    Path pathDraw = new Path();
    String path;
    Bitmap myBitmap , bmp_Copy , resized;
    static boolean isText = false , isDoodle = false , isFilter = false , isBlur = false,  isRotate = false;
    Rect rect = new Rect();
    float minText ;
    int xTemp , yTemp;
    Context context;


    public MyCanvas(Context context , String path) {
        super(context);

        this.context = context;

        paint = new Paint();
        textPaint= new Paint();
        doodlePaint = new Paint();
        filterPaint = new Paint(Paint.DITHER_FLAG);
        filterPaint.setColor(getResources().getColor(R.color.transparent));
        selectedPaint = new Paint();
        imagePaint = new Paint(Paint.DITHER_FLAG);
        this.path = path;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        x = displayMetrics.widthPixels ;
        y = displayMetrics.heightPixels;
        xText = x/2;
        yText = y/2;
        minText = 70;
        myBitmap = BitmapFactory.decodeFile(path);
//        bmp_Copy = myBitmap.copy(Bitmap.Config.ARGB_8888,true);
        resized = Bitmap.createScaledBitmap(myBitmap, (int) x, (int) y/2, true);
//        bmp_Copy.setHeight(500);
//        bmp_Copy.setWidth(500);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        blur(resized);
        rotate(resized);
        canvas.drawBitmap(resized, 0, y/4 , imagePaint);

        drawText(canvas);
        drawDoodle(canvas);
        drawFilter(canvas);
        drawBlur(canvas);

        try {
            canvas.drawPath(pathDraw, doodlePaint);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            canvas.drawText(ImageProcessing.textDraw , xTemp , yTemp , textPaint);
        }catch (Exception e) {
            e.printStackTrace();
        }
        try {
            canvas.drawRect(0 , y/4 , x , (3*y)/4  , filterPaint );
        }catch (Exception e) {
            e.printStackTrace();
        }

        invalidate();
    }

    private void rotate(Bitmap resized) {
        if (isRotate){
            Matrix matrix = new Matrix();

            matrix.postRotate(90);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(resized, (int) x, (int) x, true);

            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            this.resized = rotatedBitmap;
            isRotate = false;
        }
    }

    private void drawBlur(Canvas canvas) {
        if (isBlur){

        }
    }

    private void drawFilter(Canvas canvas) {
        if (isFilter){
            LinearGradient linearGradient = new LinearGradient(0 , y/4 , x , (3*y)/4 , getResources().getColor(R.color.transparent)
                    , getResources().getColor(R.color.colorPrimaryDark) , Shader.TileMode.MIRROR);

            filterPaint.setStyle(Paint.Style.FILL);
            filterPaint.setColor(getResources().getColor(R.color.blue));

        }
    }

    private void drawDoodle(Canvas canvas) {
        if (isDoodle){
            doodlePaint.setAntiAlias(true);
            doodlePaint.setColor(Color.RED);
            doodlePaint.setStyle(Paint.Style.STROKE);
            doodlePaint.setStrokeWidth(5f);

        }
    }

    private void drawText(Canvas canvas) {
        if (isText){
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setStrokeWidth(5f);
            textPaint.setColor(Color.BLACK);
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(minText);

            textPaint.getTextBounds(ImageProcessing.textDraw , 0 , ImageProcessing.textDraw.length() , rect);
            xTemp = (int) (xText -(rect.width()/2));
            yTemp = (int) (yText +(rect.height()/2) );

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        xPos = event.getX();
        yPos = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN : {
                touchDown(xPos, yPos);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                touchMove(xPos , yPos);
                break;
            }
            case MotionEvent.ACTION_UP:{
                touchUp(xPos, yPos);
                return false;
            }
        }
        invalidate();
        return true;
    }

    private void touchUp(float xPos, float yPos) {
        if (isText){
            xText = xPos;
            yText = yPos;
            minText = 70;
        }
        if (isDoodle){
        }
        if (isBlur){

        }

    }

    private void touchMove(float xPos, float yPos) {
        if (isText){
            xText = xPos;
            yText = yPos;
        }
        if (isDoodle){
            pathDraw.lineTo(xPos , yPos);
        }
        if (isBlur){

        }
    }

    private void touchDown(float xPos, float yPos) {
        if (isText){
            xText = xPos;
            yText = yPos;
            minText = 85;
        }
        if (isDoodle){
            pathDraw.moveTo(xPos, yPos);
        }
        if (isBlur){

        }
    }


    public  Bitmap getBitmap()
    {
        //this.measure(100, 100);
        //this.layout(0, 0, 100, 100);
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);

//        Bitmap bitmap = content.getDrawingCache();


        return bmp;
    }

    public void blur(Bitmap image) {
        if (!isBlur) return ;
        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(context);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        resized = outputBitmap;
        isBlur = false;
    }


    public static Bitmap convertToMutable(Bitmap imgIn) {
        try {
            //this is the file going to use temporally to save the bytes.
            // This file will not be a image, it will store the raw image data.
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

            //Open an RandomAccessFile
            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            //into AndroidManifest.xml file
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            // get the width and height of the source bitmap.
            int width = imgIn.getWidth();
            int height = imgIn.getHeight();
            Bitmap.Config type = imgIn.getConfig();

            //Copy the byte to the file
            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
            imgIn.copyPixelsToBuffer(map);
            //recycle the source bitmap, this will be no longer used.
            imgIn.recycle();
            System.gc();// try to force the bytes from the imgIn to be released

            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
            imgIn = Bitmap.createBitmap(width, height, type);
            map.position(0);
            //load it back from temporary
            imgIn.copyPixelsFromBuffer(map);
            //close the temporary file and channel , then delete that also
            channel.close();
            randomAccessFile.close();

            // delete the temp file
            file.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgIn;
    }
}
