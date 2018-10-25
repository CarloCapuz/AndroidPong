package com.example.it_lab_local.androidpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameBoard extends View {

    private final Handler handler = new Handler();

    private Ball ball;
    private Paint paint;

    private int ballRadius = 30;
    private float paddleWidth = 30f;
    private float paddleLength = 30f;

    private final GameBoard instance;

    public GameBoard(Context context, AttributeSet attribs) {
        super(context);
        instance = this;
        ball = new Ball(0.02f,0.2f,0.1f);

        Timer timer = new Timer(false);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        instance.postInvalidate();
                    }
                });
            }
        };
        timer.schedule(timerTask, 10,10);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ball.draw(canvas);
        ball.tick(canvas);
    }





}

