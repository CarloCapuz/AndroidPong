package com.example.elilo.myapplication;
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

    private final int winningPoint = 12;

    private int playerScore, enemyScore, level;
    private int tick = 0;
    private Paddle player, enemy;
    private Ball ball;
    private Paint paint;

    private float ballRadius = 0.02f;
    private float paddleStartLength = 30f;

    private final GameBoard instance;

    private boolean running = false;
    private boolean resetDebounce = false;

    private Timer timer = new Timer(false);

    private OnGameStateChangeListener listener;

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
        tick++;
        super.onDraw(canvas);
        ball.draw(canvas);
        player.draw(canvas);
        enemy.draw(canvas);
        if (!running) return;
        int result = ball.tick(canvas, player, enemy,  (tick >= 300));
        if (result == 1) {
            enemyScore++;
            increaseLevel();
            fireListener();
        } else if (result == 2) {
            playerScore++;
            increaseLevel();
            fireListener();
        }
        if (playerScore >= winningPoint) {
            running = false;
            Toast toast = Toast.makeText(getContext(), "Blue wins!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
            fireListener();
        } else if (enemyScore >= winningPoint) {
            running = false;
            Toast toast = Toast.makeText(getContext(), "Red wins!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
            fireListener();
        }
    }

    public void reset() {
        if (resetDebounce) return;
        resetDebounce = true;
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
                        tick = 0;
                        ball.reset();
                        player.reset();
                        enemy.reset();
                        fireListener();
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
                                        resetDebounce = false;
                                        fireListener();
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
        fireListener();
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

    private void fireListener() {
        if (listener != null) listener.OnGameStateChanged(level, playerScore, enemyScore, running);
    }

    public void setOnGameStateChangeListener(OnGameStateChangeListener listener) {
        this.listener = listener;
    }


}

