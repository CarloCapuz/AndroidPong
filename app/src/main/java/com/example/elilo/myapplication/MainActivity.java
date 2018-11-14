package com.example.elilo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GameBoard board = findViewById(R.id.board);
        final Button resetButton = findViewById(R.id.buttonReset);
        final TextView playerScoreView = findViewById(R.id.textViewPlayerScoreBox);
        final TextView enemyScoreView = findViewById(R.id.textViewAiScoreBox);
        final TextView levelView = findViewById(R.id.textViewSpeed);

        board.setOnGameStateChangeListener(new OnGameStateChangeListener() {
            @Override
            public void OnGameStateChanged(int level, int playerScore, int enemyScore, boolean running) {
                levelView.setText(Integer.toString(level));
                playerScoreView.setText(Integer.toString(playerScore));
                enemyScoreView.setText(Integer.toString(enemyScore));
                resetButton.setText((running) ? "RESET" : "START");
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.reset();
            }
        });

    }
}
