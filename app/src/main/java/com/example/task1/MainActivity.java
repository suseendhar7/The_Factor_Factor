package com.example.task1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String MSG = "Longest Winning STREAK ";
    private static final String INPUT_MSG = "Enter a Number";
    private static final String CM = "Correct Answer";
    private static final String WM = "Wrong Answer";
    int[] options = new int[4];
    boolean cVal = false;
    String streakCount;
    String HighScore;
    int currScore = 0;
    int currCnt = 0;

    final int defaultColour = Color.rgb(255, 255, 255);
    final int btnGreen = Color.rgb(0, 179, 90);
    final int btnRed = Color.rgb(255, 0, 0);
    final int bgGreen = Color.rgb(60, 208, 112);
    final int bgRed = Color.rgb(255, 77, 77);

    CountDownTimer count;
    static long START = 11000;

    private int[] rOption(int number) {
        Random random = new Random();
        int[] a = new int[4];
        int i = 0;
        int x;
        int count = 0;
        a[0] = random.nextInt(3);
        while ((count < 2 || i != 1)) {
            x = 1 + (random.nextInt(number));
            if (number % x == 0 && i == 0) {
                a[1] = x;
                i = 1;
            } else if (number % x != 0 && count < 2 && a[2] != x) {
                a[count + 2] = x;
                count++;
            }
        }
        return a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: app started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        final ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.current_layout);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final TextView textView = (TextView) findViewById(R.id.input);
        final TextView streakDisplay = (TextView) findViewById(R.id.streak);
        final Button button = (Button) findViewById(R.id.check_answer);
        final RadioButton rb1 = (RadioButton) findViewById(R.id.option1);
        final RadioButton rb2 = (RadioButton) findViewById(R.id.option2);
        final RadioButton rb3 = (RadioButton) findViewById(R.id.option3);
        final TextView score1 = (TextView) findViewById(R.id.hScore);
        final TextView score2 = (TextView) findViewById(R.id.cScore);
        final TextView timer = (TextView) findViewById(R.id.timer);

        ImageView imageView = (ImageView) findViewById(R.id.sImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, MSG + preferences.getString("streakCount", "0"), Toast.LENGTH_SHORT).show();
            }
        });

        streakCount = preferences.getString("streakCount", "0");
        streakDisplay.setText(streakCount);

        HighScore = preferences.getString("score", "0");
        score1.setText(HighScore);
        score2.setText(String.valueOf(currScore));

        score1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "HighScore " + HighScore, Toast.LENGTH_SHORT).show();
            }
        });

        score2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Current Score " + currScore +
                        "\nCurrent Streak " + currCnt, Toast.LENGTH_SHORT).show();
            }
        });

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Time Remaining", Toast.LENGTH_SHORT).show();
            }
        });

        //editor.putString("streakCount", "0");
        //editor.apply();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String string = textView.getText().toString();
                if (string.isEmpty())
                    Toast.makeText(MainActivity.this, INPUT_MSG, Toast.LENGTH_SHORT).show();
                else if (Integer.parseInt(string) == 0 || Integer.parseInt(string) == 1 ||
                        Integer.parseInt(string) == 2 || Integer.parseInt(string) == 3 || Integer.parseInt(string) == 4) {
                    Toast.makeText(MainActivity.this, "Enter Number Excluding(0,1,2,3,4)", Toast.LENGTH_SHORT).show();
                    textView.setText("");
                } else {
                    button.setVisibility(View.VISIBLE);
                    rb1.setVisibility(View.VISIBLE);
                    rb2.setVisibility(View.VISIBLE);
                    rb3.setVisibility(View.VISIBLE);
                    textView.setEnabled(false);
                    int number = Integer.parseInt(string);
                    //Log.d(TAG, "onClick: " + number);
                    options = rOption(number);
                    if (options[0] == 0) {
                        rb1.setText(String.valueOf(options[1]));
                        rb2.setText(String.valueOf((options[3])));
                        rb3.setText(String.valueOf((options[2])));
                    } else if (options[0] == 1) {
                        rb2.setText(String.valueOf(options[1]));
                        rb1.setText(String.valueOf((options[2])));
                        rb3.setText(String.valueOf((options[3])));
                    } else if (options[0] == 2) {
                        rb3.setText(String.valueOf(options[1]));
                        rb2.setText(String.valueOf((options[2])));
                        rb1.setText(String.valueOf((options[3])));
                    }
                    timer.setVisibility(View.VISIBLE);
                    count = new CountDownTimer(START, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            START = millisUntilFinished;
                            String tRem = String.format(Locale.getDefault(), "%02d:%02d",
                                    START / 1000 / 60, START / 1000 % 60);
                            timer.setText(tRem);
                        }

                        @Override
                        public void onFinish() {
                            Intent i = new Intent(MainActivity.this, popup.class);
                            i.putExtra("score", String.valueOf(currScore));
                            i.putExtra("streak", String.valueOf(currCnt));
                            startActivity(i);
                            START = 11000;
                            Toast.makeText(MainActivity.this, "Time up. Closing App....",Toast.LENGTH_SHORT).show();
                            del();
                        }
                    }.start();

                    button.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onClick(View v) {
                            if (!rb1.isChecked() && !rb2.isChecked() && !rb3.isChecked())
                                Toast.makeText(MainActivity.this, "Choose an Option", Toast.LENGTH_SHORT).show();
                            else {
                                if (rb1.isChecked()) {
                                    if (options[0] == 0) {
                                        rb1.setBackgroundColor(btnGreen);
                                        constraintLayout.setBackgroundColor(bgGreen);
                                        cVal = true;
                                    } else {
                                        rb1.setBackgroundColor(btnRed);
                                        constraintLayout.setBackgroundColor(bgRed);
                                    }
                                    rb2.setBackgroundColor(defaultColour);
                                    rb3.setBackgroundColor(defaultColour);
                                } else if (rb2.isChecked()) {
                                    if (options[0] == 1) {
                                        rb2.setBackgroundColor(btnGreen);
                                        constraintLayout.setBackgroundColor(bgGreen);
                                        cVal = true;
                                    } else {
                                        rb2.setBackgroundColor(btnRed);
                                        constraintLayout.setBackgroundColor(bgRed);
                                    }
                                    rb1.setBackgroundColor(defaultColour);
                                    rb3.setBackgroundColor(defaultColour);
                                } else if (rb3.isChecked()) {
                                    if (options[0] == 2) {
                                        rb3.setBackgroundColor(btnGreen);
                                        constraintLayout.setBackgroundColor(bgGreen);
                                        cVal = true;
                                    } else {
                                        rb3.setBackgroundColor(btnRed);
                                        constraintLayout.setBackgroundColor(bgRed);
                                    }
                                    rb2.setBackgroundColor(defaultColour);
                                    rb1.setBackgroundColor(defaultColour);
                                }
                                if (options[0] == 0)
                                    rb1.setBackgroundColor(btnGreen);
                                else if (options[0] == 1)
                                    rb2.setBackgroundColor(btnGreen);
                                else
                                    rb3.setBackgroundColor(btnGreen);
                                if (cVal) {
                                    Toast.makeText(MainActivity.this, CM, Toast.LENGTH_SHORT).show();
                                    currCnt++;
                                    if ((int) START / 1000 > 5)
                                        currScore += 3;
                                    else if ((int) START / 1000 > 3)
                                        currScore += 2;
                                    else
                                        currScore += 1;
                                    count.cancel();
                                } else {
                                    Toast.makeText(MainActivity.this, WM, Toast.LENGTH_SHORT).show();
                                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                    if (vibrator != null)
                                        vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                                    else
                                        Toast.makeText(MainActivity.this, "Cannot Vibrate Device", Toast.LENGTH_SHORT).show();
                                    currCnt = 0;
                                    count.cancel();
                                }
                                if (currCnt > Integer.parseInt(streakCount)) {
                                    editor.putString("streakCount", String.valueOf(currCnt));
                                    editor.apply();
                                    streakDisplay.setText(String.valueOf(currCnt));
                                } else {
                                    streakCount = preferences.getString("streakCount", "0");
                                    streakDisplay.setText(streakCount);
                                }
                                if ((currScore > Integer.parseInt(HighScore))) {
                                    editor.putString("score", String.valueOf(currScore));
                                    editor.apply();
                                    score1.setText(String.valueOf(currScore));
                                    score2.setText(String.valueOf(currScore));
                                } else {
                                    HighScore = preferences.getString("score", "0");
                                    score1.setText(HighScore);
                                    score2.setText(String.valueOf(currScore));
                                }
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        radioGroup.clearCheck();
                                        textView.setEnabled(true);
                                        rb1.setBackgroundColor(defaultColour);
                                        rb2.setBackgroundColor(defaultColour);
                                        rb3.setBackgroundColor(defaultColour);
                                        textView.setText("");
                                        timer.setVisibility(View.INVISIBLE);
                                        button.setVisibility(View.INVISIBLE);
                                        rb1.setVisibility(View.INVISIBLE);
                                        rb2.setVisibility(View.INVISIBLE);
                                        rb3.setVisibility(View.INVISIBLE);
                                        cVal = false;
                                        START = 11000;
                                        constraintLayout.setBackgroundColor(defaultColour);
                                    }
                                }, 2300);
                            }
                        }
                    });
                }
            }

        });
    }
    private void del() {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2600);
    }
}