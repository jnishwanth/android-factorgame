package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public String SHARED_PREFS = "sharedPrefs";
    public  String STREAK_PREF = "streakPref";
    private String stext = "0";

    Integer count;
    TextView timer;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if(count<=10)  timer.setText(String.valueOf(10-count));
            ++ count;
            if(count == 11) {
                ans1.setVisibility(View.INVISIBLE);
                ans2.setVisibility(View.INVISIBLE);
                ans3.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.VISIBLE);
                showToast("2Slow!");
                constlay.setBackgroundColor(Color.parseColor("#ff0000"));
                streak.setText("0");
            }
            timerHandler.postDelayed(this, 1000);
        }
    };
    Integer number, select;
    Integer i,j;
    Integer[] f = new Integer[20];
    private Button ans1;
    private Button ans2;
    private Button ans3;
    private Button submit;
    private TextView streak;
    private ConstraintLayout constlay;
    private EditText numinput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final Vibrator viber = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        setContentView(R.layout.activity_main);
        numinput =  (EditText) findViewById(R.id.text_box);
        ans1 = (Button) findViewById(R.id.button1);
        ans2 = (Button) findViewById(R.id.button2);
        ans3 = (Button) findViewById(R.id.button3);
        submit = (Button) findViewById(R.id.button);
        streak = (TextView) findViewById(R.id.streak_num);
        constlay = (ConstraintLayout) findViewById(R.id.layout);
        timer = (TextView) findViewById(R.id.timer_txt);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        stext = sharedPreferences.getString(STREAK_PREF, "0");

        streak.setText(sharedPreferences.getString(STREAK_PREF, "0"));

        count = 0;
        i = 0;



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count = 0;
                j = 0;
                number = 0;
                number = Integer.valueOf(numinput.getText().toString());

                if (number>=0 && number<=3)
                    {
                        showToast("Come on! Challenge yourself more!");
                        return;
                    }

                for( i=1; i<=number; ++i )
                    if (number % i == 0) {
                        f[j] = i;
                        j++;
                        if (j == 20) break;
                    }
                //Toast.makeText(MainActivity.this, String.valueOf(j), Toast.LENGTH_LONG).show();

                select = new Random().nextInt(3)+1;

                Integer k;

                switch(select)  {
                    case 1:
                        ans1.setText(String.valueOf(f[new Random().nextInt(j)]));
                        while(true) {
                            k = new Random().nextInt(number) + 1;
                            if (number % k != 0) {
                                ans2.setText(String.valueOf(k));
                                break;
                            }
                        }
                        while(true)     {
                            k = new Random().nextInt(number) + 1;
                            if (((number % k) != 0) && (!k.equals(Integer.valueOf(ans2.getText().toString())))) {
                                ans3.setText(String.valueOf(k));
                                break;
                            }
                        }
                        break;
                    case 2:
                        ans2.setText(String.valueOf(f[new Random().nextInt(j)]));
                        while(true)    {
                            k = new Random().nextInt(number) + 1;
                            if ((number % k) != 0) {
                                ans1.setText(String.valueOf(k));
                                break;
                            }
                        }
                            while(true)     {
                                k = new Random().nextInt(number) + 1;
                                if (((number % k) != 0) && (!k.equals(Integer.valueOf(ans1.getText().toString())))) {
                                    ans3.setText(String.valueOf(k));
                                    break;
                                }
                            }
                            break;
                    case 3:
                        ans3.setText(String.valueOf(f[new Random().nextInt(j)]));
                        while(true)    {
                            k = new Random().nextInt(number) + 1;
                            if (number % k != 0) {
                                ans1.setText(String.valueOf(k));
                                break;
                            }
                        }
                        while(true)     {
                            k = new Random().nextInt(number) + 1;
                            if ((number % k != 0)&&(!k.equals(Integer.valueOf(ans1.getText().toString())))) {
                                ans2.setText(String.valueOf(k));
                                break;
                            }
                        }
                        break;
                }

                ans1.setVisibility(View.VISIBLE);
                ans2.setVisibility(View.VISIBLE);
                ans3.setVisibility(View.VISIBLE);
                submit.setVisibility(View.INVISIBLE);
                constlay.setBackgroundColor(Color.parseColor("#ffffff"));


                timerHandler.postDelayed(timerRunnable, 0);
            }
        });

        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timerHandler.removeCallbacks(timerRunnable);

                if( number % Integer.parseInt(ans1.getText().toString()) == 0 )  {
                    showToast("Correcto!");
                    constlay.setBackgroundColor(Color.parseColor("#198C19"));
                    streak.setText(String.valueOf(Integer.parseInt(streak.getText().toString())+1));
                }
                else    {
                    if (Build.VERSION.SDK_INT >= 26) {
                        assert viber != null;
                        viber.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        assert viber != null;
                        viber.vibrate(250);
                    }
                    showToast("Wrongo!");
                    constlay.setBackgroundColor(Color.parseColor("#ff0000"));
                    streak.setText("0");
                }

                ans1.setVisibility(View.INVISIBLE);
                ans2.setVisibility(View.INVISIBLE);
                ans3.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.VISIBLE);

            }
        });

        ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timerHandler.removeCallbacks(timerRunnable);

                if( number % Integer.parseInt(ans2.getText().toString()) == 0 )  {
                    showToast("Correcto!");
                    constlay.setBackgroundColor(Color.parseColor("#198C19"));
                    streak.setText(String.valueOf(Integer.parseInt(streak.getText().toString())+1));
                }
                else    {
                    if (Build.VERSION.SDK_INT >= 26) {
                        viber.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        viber.vibrate(250);
                    }
                    showToast("Wrongo!");
                    constlay.setBackgroundColor(Color.parseColor("#ff0000"));
                    streak.setText("0");
                }

                ans1.setVisibility(View.INVISIBLE);
                ans2.setVisibility(View.INVISIBLE);
                ans3.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.VISIBLE);

            }
        });

        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timerHandler.removeCallbacks(timerRunnable);

                if( number % Integer.parseInt(ans3.getText().toString()) == 0 )  {
                    showToast("Correcto!");
                    constlay.setBackgroundColor(Color.parseColor("#198C19"));
                    streak.setText(String.valueOf(Integer.parseInt(streak.getText().toString())+1));
                }
                else    {
                    if (Build.VERSION.SDK_INT >= 26) {
                        viber.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        viber.vibrate(250);
                    }
                    showToast("Wrongo!");
                    streak.setText("0");
                    constlay.setBackgroundColor(Color.parseColor("#ff0000"));
                }

                ans1.setVisibility(View.INVISIBLE);
                ans2.setVisibility(View.INVISIBLE);
                ans3.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.VISIBLE);

            }
        });

    }

    private void showToast (String text)    {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(STREAK_PREF, streak.getText().toString());

        editor.apply();
    }
}
