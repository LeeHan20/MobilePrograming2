package com.example.mobileprograming;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast; // Toast를 사용하기 위해 import

import androidx.appcompat.app.AppCompatActivity;

public class Login_main extends AppCompatActivity {

    ImageView imageView;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private static final String VALID_EMAIL = "test";
    private static final String VALID_PASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.button2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        handleImage();
    }

    private void performLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        if (email.equals(VALID_EMAIL) && password.equals(VALID_PASSWORD)) {
            Toast.makeText(Login_main.this, "로그인 성공!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Login_main.this, Main.class);
            startActivity(intent);
            finish();

        } else {
            // 로그인 실패
            Toast.makeText(Login_main.this, "이메일 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void handleImage() { //
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.hannyang);
    }
}
