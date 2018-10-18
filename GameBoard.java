package com.example.it_lab_local.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;

public class GameBoard extends View {

    private ShapeDrawable ball, player, enemy;

    private int ballRatio = 30;
    private float paddleWidthRatio = 30f;
    private float paddleLengthRatio = 30f;

    public GameBoard(Context context) {
        super(context);
        ball = new ShapeDrawable(new OvalShape());
        player = new ShapeDrawable(new RectShape());
        enemy = new ShapeDrawable(new RectShape());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int ballRadius = canvas.getHeight()/ballRatio;
        ball.setBounds(ballRadius, ballRadius, ballRadius, ballRadius);

    }
}
