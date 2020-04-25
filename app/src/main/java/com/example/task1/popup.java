package com.example.task1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class popup extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);
        Intent i = getIntent();
        TextView scoreDisplay = (TextView) findViewById(R.id.score);
        TextView streakDisplay = (TextView) findViewById(R.id.streak);
        String score = "Score " + i.getStringExtra("score");
        String streak = "Streak " + i.getStringExtra("streak");
        scoreDisplay.setText(score);
        streakDisplay.setText(streak);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        getWindow().setLayout((int)(metrics.widthPixels*.6), (int)(metrics.heightPixels*.2));
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.x = 0;
        layoutParams.y = -10;
        getWindow().setAttributes(layoutParams);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2400);
    }
}
