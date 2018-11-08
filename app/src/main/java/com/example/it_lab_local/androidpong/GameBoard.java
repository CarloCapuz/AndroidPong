package com.example.it_lab_local.androidpong;

import android.annotation.SuppressLint;
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
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameBoard extends View {

    private final Handler handler = new Handler();

    private final int winningPoint = 11;

    private int playerScore, enemyScore;

    private Paddle player, enemy;
    private Ball ball;
    private Paint paint;

    private float ballRadius = 0.02f;
    private float paddleStartLength = 30f;

    private final GameBoard instance;

    private boolean running = false;
    private int level;

    private Timer timer = new Timer(false);


    @SuppressLint("ClickableViewAccessibility")
    public GameBoard(Context context, AttributeSet attribs) {
        super(context, attribs);
        instance = this;
        ball = new Ball(0.02f,0.5f,0.5f);
        player = new Paddle(0.005f, 0xFF0000FF);
        enemy = new Paddle(0.995f, 0xFFFF0000);
        playerScore = 0;
        enemyScore = 0;


        TimerTask refreshTask = new TimerTask() {
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
        timer.schedule(refreshTask, 1,1);

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!running) return false;
                float y = motionEvent.getY() / instance.getHeight();
                player.setY(y);
                return true;
            }
        });



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ball.draw(canvas);
        player.draw(canvas);
        enemy.draw(canvas);
        if (!running) return;
        int result = ball.tick(canvas, player, enemy);
        if (result == 1) {
            enemyScore++;
            increaseLevel();
        } else if (result == 2) {
            playerScore++;
            increaseLevel();
        }
        if (playerScore >= winningPoint) {
            Toast toast = Toast.makeText(getContext(), "Blue wins!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
            reset();
        } else if (enemyScore >= winningPoint) {
            Toast toast = Toast.makeText(getContext(), "Red wins!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
            reset();
        }
    }

    public void reset() {
        running = false;
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        playerScore = 0;
                        enemyScore = 0;
                        level = 0;
                        ball.reset();
                        player.reset();
                        enemy.reset();
                        Toast toast = Toast.makeText(getContext(), "Game starting....",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,0);
                        toast.show();
                        TimerTask task2 = new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        running = true;
                                    }
                                });
                            }
                        };
                        timer.schedule(task2, 5000);
                    }
                });
            }
        };
        timer.schedule(task1, 3000);
    }

    private void increaseLevel() {
        level++;
        ball.setLevel(level, winningPoint);
        player.setLevel(level, winningPoint);
        enemy.setLevel(level, winningPoint);
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getEnemyScore() {
        return enemyScore;
    }

    public int getLevel() {
        return level;
    }

    public static float lerp(float a, float b, float f)
    {
        return (a * (1.0f - f)) + (b * f);
    }
}

