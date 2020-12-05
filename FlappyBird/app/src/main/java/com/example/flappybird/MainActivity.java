package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textViewPlay;
    TextView textViewScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlay = findViewById(R.id.textViewPlay);
        textViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        textViewScore = findViewById(R.id.textViewScore);

        SharedPreferences prefs = getSharedPreferences("game",MODE_PRIVATE);
        int score = prefs.getInt("score", 0);
        textViewScore.setText("HighScore : " + score);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("game",MODE_PRIVATE);
        int score = prefs.getInt("score", 0);
        textViewScore.setText("HighScore : " + score);
    }
}