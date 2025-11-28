package com.example.mobileprograming;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_playground);

        imageView = findViewById(R.id.image_view);
        Button scaleTypeButton = findViewById(R.id.scale_type_button);
        Button rotationButton = findViewById(R.id.rotation_button);
        Button alphaButton = findViewById(R.id.alpha_button);
        Button backToMainButton = findViewById(R.id.back_to_main_button);

        scaleTypeButton.setOnClickListener(v -> {
            currentScaleTypeIndex = (currentScaleTypeIndex + 1) % scaleTypes.length;
            ImageView.ScaleType newScaleType = scaleTypes[currentScaleTypeIndex];
            imageView.setScaleType(newScaleType);
        });

        rotationButton.setOnClickListener(v -> {
            currentRotation += 45f;
            if (currentRotation >= 360f) {
                currentRotation = 0f;
            }
            imageView.setRotation(currentRotation);
        });

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
    }
}
