package com.example.mobileprograming;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ImagePlayground extends AppCompatActivity {

    private ImageView imageView;
    private int currentScaleTypeIndex = 0;
    private float currentRotation = 0f;
    private float currentAlpha = 1.0f;

    private final ImageView.ScaleType[] scaleTypes = {
            ImageView.ScaleType.CENTER,
            ImageView.ScaleType.CENTER_CROP,
            ImageView.ScaleType.CENTER_INSIDE,
            ImageView.ScaleType.FIT_CENTER,
            ImageView.ScaleType.FIT_XY,
            ImageView.ScaleType.MATRIX
    };

    // 람다 함수를 적극 이용
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_playground);

        ConstraintLayout rootLayout = findViewById(R.id.root_layout);
        imageView = findViewById(R.id.image_view);
        Button scaleTypeButton = findViewById(R.id.scale_type_button);
        Button rotationButton = findViewById(R.id.rotation_button);
        Button alphaButton = findViewById(R.id.alpha_button);
        Button backToMainButton = findViewById(R.id.back_to_main_button);
        Button backgroundButton = findViewById(R.id.background_button);

        // 이미지를 스케일함
        scaleTypeButton.setOnClickListener(v -> {
            currentScaleTypeIndex = (currentScaleTypeIndex + 1) % scaleTypes.length;
            ImageView.ScaleType newScaleType = scaleTypes[currentScaleTypeIndex];
            imageView.setScaleType(newScaleType);
        });

        // 이미지를 회전함
        rotationButton.setOnClickListener(v -> {
            currentRotation += 45f;
            if (currentRotation >= 360f) {
                currentRotation = 0f;
            }
            imageView.setRotation(currentRotation);
        });

        // 이미지의 투명도로 변경
        alphaButton.setOnClickListener(v -> {
            if (currentAlpha == 1.0f) {
                currentAlpha = 0.5f;
            } else {
                currentAlpha = 1.0f;
            }
            imageView.setAlpha(currentAlpha);
        });


        backToMainButton.setOnClickListener(v -> {
            finish(); // Finish current activity and go back to the previous one (Main)
        });

        // 배경 색상을 바꿈
        backgroundButton.setOnClickListener(v -> {
            Random random = new Random();
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            int randomColor = Color.rgb(red, green, blue);
            rootLayout.setBackgroundColor(randomColor);
        });
    }
}
