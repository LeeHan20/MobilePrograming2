package com.example.mobileprograming;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.Animation;
import android.graphics.Color;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Main extends AppCompatActivity {
    private TextView dateTextView;
    private EditText diaryEditText;
    private Button saveButton;
    private Button randomButton;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyDiary";
    private static final String DIARY_KEY_PREFIX = "diary_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        dateTextView = findViewById(R.id.dateTextView);
        diaryEditText = findViewById(R.id.diaryEditText);
        saveButton = findViewById(R.id.saveButton);
        randomButton = findViewById(R.id.button3);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        displayCurrentDate();

        loadDiary();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDiary();
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRandomNumber();
            }
        });
    }

    private void displayCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);
        String currentDate = sdf.format(new Date());
        dateTextView.setText(currentDate);
    }

    private void loadDiary() {
        String todayKey = getTodayDateKey();
        String savedDiary = sharedPreferences.getString(todayKey, "");
        diaryEditText.setText(savedDiary);
    }

    private void saveDiary() {
        String diaryContent = diaryEditText.getText().toString();
        String todayKey = getTodayDateKey();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(todayKey, diaryContent);
        editor.apply();

        Toast.makeText(this, "일기가 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private String getTodayDateKey() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        String date = sdf.format(new Date());
        return DIARY_KEY_PREFIX + date;
    }

    private void showRandomNumber() {

        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1;

        // 버튼 배경색을 랜덤하게 변경
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        randomButton.setBackgroundColor(Color.rgb(red, green, blue));

        final ViewGroup overlay = new android.widget.FrameLayout(this);
        overlay.setBackgroundColor(Color.argb(180, 0, 0, 0)); // 반투명 검은색 배경
        overlay.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        overlay.setClickable(true);

        final TextView randomTextView = new TextView(this);
        randomTextView.setText("Random Number! :\n" + randomNumber);
        randomTextView.setTextSize(48);
        randomTextView.setTextColor(Color.WHITE);
        randomTextView.setGravity(Gravity.CENTER);
        randomTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        overlay.addView(randomTextView);

        ViewGroup rootLayout = findViewById(android.R.id.content);
        rootLayout.addView(overlay);

        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.5f, 1.5f,
                0.5f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setStartOffset(500);
        alphaAnimation.setDuration(500);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        overlay.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rootLayout.removeView(overlay);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
