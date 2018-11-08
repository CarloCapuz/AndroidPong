package com.example.it_lab_local.androidpong;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GameBoard gameBoard = findViewById(R.id.gameboard);
        gameBoard.reset();


    }



}
