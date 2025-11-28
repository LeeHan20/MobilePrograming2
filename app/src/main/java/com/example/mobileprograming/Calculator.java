package com.example.mobileprograming;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Calculator extends AppCompatActivity {

    private EditText num1EditText;
    private EditText num2EditText;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);

        num1EditText = findViewById(R.id.num1_edit_text);
        num2EditText = findViewById(R.id.num2_edit_text);
        resultTextView = findViewById(R.id.result_text_view);

        Button addButton = findViewById(R.id.add_button);
        Button subtractButton = findViewById(R.id.subtract_button);
        Button multiplyButton = findViewById(R.id.multiply_button);
        Button divideButton = findViewById(R.id.divide_button);
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            finish();
        });

        View.OnClickListener listener = v -> {
            try {
                String num1Str = num1EditText.getText().toString();
                String num2Str = num2EditText.getText().toString();

                if (num1Str.isEmpty() || num2Str.isEmpty()) {
                    Toast.makeText(this, "Please enter both numbers", Toast.LENGTH_SHORT).show();
                    return;
                }

                double num1 = Double.parseDouble(num1Str);
                double num2 = Double.parseDouble(num2Str);
                double result = 0;

                int id = v.getId();
                if (id == R.id.add_button) {
                    result = num1 + num2;
                } else if (id == R.id.subtract_button) {
                    result = num1 - num2;
                } else if (id == R.id.multiply_button) {
                    result = num1 * num2;
                } else if (id == R.id.divide_button) {
                    if (num2 == 0) {
                        Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    result = num1 / num2;
                }

                resultTextView.setText(String.valueOf(result));

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            }
        };

        addButton.setOnClickListener(listener);
        subtractButton.setOnClickListener(listener);
        multiplyButton.setOnClickListener(listener);
        divideButton.setOnClickListener(listener);
    }
}
