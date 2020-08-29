package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

public class ImageProcessing extends AppCompatActivity {

    FrameLayout frameImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processing);

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");

        frameImage = findViewById(R.id.frameImage);
        MyCanvas canvas = new MyCanvas(this , path);
        frameImage.addView(canvas);

    }
}