package com.example.mobileprograming;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Main extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button diaryButton = findViewById(R.id.diary_button);
        Button calculatorButton = findViewById(R.id.calculator_button);
        Button imagePlaygroundButton = findViewById(R.id.image_playground_button);

        diaryButton.setOnClickListener(v -> {
            Intent intent = new Intent(Main.this, Diary.class);
            startActivity(intent);
        });

        calculatorButton.setOnClickListener(v -> {
            Intent intent = new Intent(Main.this, Calculator.class);
            startActivity(intent);
        });

        imagePlaygroundButton.setOnClickListener(v -> {
            Intent intent = new Intent(Main.this, ImagePlayground.class);
            startActivity(intent);
        });

        handleImage();
    }

    protected void handleImage() { //
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.hannyang);
    }
}
