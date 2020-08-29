package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ImageProcessing extends AppCompatActivity {

    FrameLayout frameImage;
    FloatingActionButton crop , text , doodle , filter , color , blur , rotate;
    EditText editText;
    Button textEnter , colorEnter;
    LinearLayout textLinearLayout;
    MyCanvas canvas;
    static String textDraw = "";

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
        textLinearLayout = findViewById(R.id.textInput);
        editText = findViewById(R.id.editText);
        textEnter = findViewById(R.id.textEnter);
        colorEnter = findViewById(R.id.colorEnter);


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

            }
        });

        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.isRotate = false;
                canvas.isText = false;
                canvas.isDoodle = true;
                canvas.isFilter = false;
                canvas.isBlur = false;
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


}