package com.example.elilo.myapplication;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Paddle {

    private float maxAIVelocity = 0.01f;

    private float aiTarget = 0.5f;

    private float error = 0f;

    private final float width = 0.01f;
    private final float minLength = 0.07f;
    private final float maxLength = 0.10f;

    private float axis,y, length;
    private Paint paint;

    private Random random;

    private boolean moving = false;

    public Paddle(float axis, int color) {
        this.random = new Random();
        this.axis = axis;
        this.y = 0.5f;
        this.length = maxLength;
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setLevel(int level, int maxLevel) {
        this.length = GameBoard.lerp(maxLength, minLength, (float)level/(float)maxLevel);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect((axis-width)*canvas.getWidth(), (y+length)*canvas.getHeight(), (axis+width)*canvas.getWidth(), (y-length)*canvas.getHeight(), paint);
    }

    public boolean doesBallClipPaddle(Ball ball) {
        float clipPointX = (ball.getX() > 0.5) ? ball.getX() + ball.getRadius() : ball.getX() - ball.getRadius();
        float paddleXBound = (axis > 0.5) ? axis - width : axis + width;
        float yTop = y + length;
        float yBottom = y - length;
        return ((axis > 0.5) ? clipPointX >= paddleXBound : clipPointX <= paddleXBound) && (ball.getY() >= yBottom && ball.getY() <= yTop);
    }

    public void reset() {
        this.y = 0.5f;
        this.length = maxLength;
        this.maxAIVelocity = 0.01f;
    }

    public void aiTick() {
        float currDist = Math.abs(aiTarget - y);
        if (currDist <= maxAIVelocity) {
            y = aiTarget;
            moving = false;
        } else {
            y = (y > aiTarget) ? y - maxAIVelocity : y + maxAIVelocity;
            moving = true;
        }
    }

    public void setAITarget(float newTarget) {
        this.aiTarget = newTarget + error;
    }

    public void recalcError() {
        float maxError = length;
        error = (random.nextFloat()*maxError);
    }

    public void setAIMaxSPeed(float maxSpeed) {
        this.maxAIVelocity = maxSpeed;
    }

    public boolean isMoving() {
        return moving;
    }

}
