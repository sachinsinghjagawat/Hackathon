package com.example.hackathon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;

import static android.graphics.Color.WHITE;

public class ImageProcessing extends AppCompatActivity {

    OvershootInterpolator interpolator = new OvershootInterpolator();
    FrameLayout frameImage;
    FloatingActionButton crop , text , doodle , filter , color , blur , rotate, fabMain;
    EditText editText;
    Button textEnter , colorEnter , saveButton;
    LinearLayout textLinearLayout;
    MyCanvas canvas;
    static String textDraw = "";
    boolean isMenuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processing);

        final Intent intent = getIntent();
        final String path = intent.getStringExtra("path");

        frameImage = findViewById(R.id.frameImage);
        canvas = new MyCanvas(this , path);
        frameImage.addView(canvas);

        crop = findViewById(R.id.fabCrop);
        text = findViewById(R.id.fabText);
        doodle = findViewById(R.id.fabDoodle);
        filter = findViewById(R.id.fabFilter);
        color = findViewById(R.id.fabColor);
        blur = findViewById(R.id.fabBlur);
        rotate = findViewById(R.id.fabRotate);
        fabMain = findViewById(R.id.fabMain);
        textLinearLayout = findViewById(R.id.textInput);
        editText = findViewById(R.id.editText);
        textEnter = findViewById(R.id.textEnter);
        colorEnter = findViewById(R.id.colorEnter);
        saveButton = findViewById(R.id.saveButton);
        closeMenu();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View content = frameImage;
                content.setDrawingCacheEnabled(true);
                content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                Bitmap bitmap = content.getDrawingCache();
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = new File("/storage/emulated/0/DCIM/Camera"+"/image.png");
                FileOutputStream ostream;
                try {
                    file.createNewFile();
                    ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.flush();
                    ostream.close();
                    Toast.makeText(getApplicationContext(), "Image saved", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuOpen){
                    closeMenu();
                }else {
                    openMenu();
                }
            }
        });


        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext() , CropActivity.class);
                intent.putExtra( "path" , path);
                startActivity(intent1);
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textLinearLayout.setVisibility(View.VISIBLE);
                canvas.isRotate = false;
                canvas.isText = true;
                canvas.isDoodle = false;
                canvas.isFilter = false;
                canvas.isBlur = false;

                textEnter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( editText.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Please Enter The Texxt", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        textLinearLayout.setVisibility(View.INVISIBLE);
                        textDraw = editText.getText().toString();
                        editText.setText("");

                        canvas.postInvalidate();

                    }
                });
            }
        });

        doodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.isRotate = false;
                canvas.isText = false;
                canvas.isDoodle = true;
                canvas.isFilter = false;
                canvas.isBlur = false;

                canvas.postInvalidate();

            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.isRotate = false;
                canvas.isText = false;
                canvas.isDoodle = false;
                canvas.isFilter = true;
                canvas.isBlur = false;
            }
        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext());
//                final View dialogLayout = ImageProcessing.this.getLayoutInflater().inflate(R.layout.color_piker_accent, null);
//                final LineColorPicker colorPicker = (LineColorPicker) dialogLayout.findViewById(R.id.color_picker_accent);
//                final TextView dialogTitle = (TextView) dialogLayout.findViewById(R.id.cp_accent_title);
//                dialogTitle.setText(R.string.text_color_title);
//                colorPicker.setColors(ColorPalette.getAccentColors(activity.getApplicationContext()));
//                changeTextColor(WHITE);

            }
        });

        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.isRotate = false;
                canvas.isText = false;
                canvas.isDoodle = false;
                canvas.isFilter = false;
                canvas.isBlur = true;
            }
        });

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.isRotate = true;
                canvas.postInvalidate();

            }
        });

    }

    private void closeMenu() {
        crop.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
        doodle.setVisibility(View.GONE);
        filter.setVisibility(View.GONE);
        color.setVisibility(View.GONE);
        blur.setVisibility(View.GONE);
        rotate.setVisibility(View.GONE);

        crop.setAlpha(0f);
        text.setAlpha(0f);
        doodle.setAlpha(0f);
        filter.setAlpha(0f);
        color.setAlpha(0f);
        blur.setAlpha(0f);
        rotate.setAlpha(0f);

        fabMain.animate().rotation(0f);

        isMenuOpen = false;
    }

    private void openMenu() {
        crop.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);
        doodle.setVisibility(View.VISIBLE);
        filter.setVisibility(View.VISIBLE);
        color.setVisibility(View.VISIBLE);
        blur.setVisibility(View.VISIBLE);
        rotate.setVisibility(View.VISIBLE);

        crop.setTranslationY(100);
        crop.animate().alpha(1).translationY(0).setInterpolator(interpolator).setDuration(300);

        text.setTranslationY(100);
        text.animate().alpha(1).translationY(0).setInterpolator(interpolator).setDuration(300);

        doodle.setTranslationY(100);
        doodle.animate().alpha(1).translationY(0).setInterpolator(interpolator).setDuration(300);

        filter.setTranslationY(100);
        filter.animate().alpha(1).translationY(0).setInterpolator(interpolator).setDuration(300);

        color.setTranslationY(100);
        color.animate().alpha(1).translationY(0).setInterpolator(interpolator).setDuration(300);

        blur.setTranslationY(100);
        blur.animate().alpha(1).translationY(0).setInterpolator(interpolator).setDuration(300);

        rotate.setTranslationY(100);
        rotate.animate().alpha(1).translationY(0).setInterpolator(interpolator).setDuration(300);

        fabMain.animate().rotation(45f).setInterpolator(interpolator).setDuration(300);


        isMenuOpen = true;
    }

//    public static int[] getAccentColors(Context context){
//        return new int[]{
//                ContextCompat.getColor(context, R.color.md_red_500),
//                ContextCompat.getColor(context, R.color.md_purple_500),
//                ContextCompat.getColor(context, R.color.md_deep_purple_500),
//                ContextCompat.getColor(context, R.color.md_blue_500),
//                ContextCompat.getColor(context, R.color.md_light_blue_500),
//                ContextCompat.getColor(context, R.color.md_cyan_500),
//                ContextCompat.getColor(context, R.color.md_teal_500),
//                ContextCompat.getColor(context, R.color.md_green_500),
//                ContextCompat.getColor(context, R.color.md_yellow_500),
//                ContextCompat.getColor(context, R.color.md_orange_500),
//                ContextCompat.getColor(context, R.color.md_deep_orange_500),
//                ContextCompat.getColor(context, R.color.md_brown_500),
//                ContextCompat.getColor(context, R.color.md_blue_grey_500),
//        };
//    }


}