package com.example.elilo.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Ball {

    private final float minSpeed = 0.01f;
    private final float maxSpeed = 0.035f;

    private float r, x, y, speed;
    private float velX, velY, ratioX, ratioY;

    private Paint paint;

    private Random random = new Random();

    public Ball(float r, float x, float y) {
        this.r = r;
        this.x = x;
        this.y = y;
        this.velX = 1f;
        this.velY = 1f;
        this.speed = minSpeed;
        paint = new Paint();
        paint.setColor(0xFF000000); //Start off as black
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(canvas.getWidth()*x,canvas.getHeight()*y,canvas.getWidth()*r,paint);
    }

    public int tick(Canvas canvas, Paddle player, Paddle enemy, boolean canHit) {
        int status = 0;
        float prevX = x;
        float prevY = y;
        x += velX;
        y += velY;
        float xpos_bound = x + r;
        float xneg_bound = x - r;
        float ypos_bound = y + r;
        float yneg_bound = y - r;
        if (player.doesBallClipPaddle(this) || enemy.doesBallClipPaddle(this)) {
            y = prevY;
            x = prevX;
            hitX();
        } else {
            if (ypos_bound >= 1 || yneg_bound <= 0) {
                y = prevY;
                x = prevX;
                hitY();
            }
            if (xpos_bound >= 1 || xneg_bound <= 0) {
                x = prevX;
                y = prevY;
                hitX();
                paint.setColor((canHit) ? ((xpos_bound >= 1) ? 0xFF0000FF : 0xFFFF0000) : paint.getColor()); //Red ball if hits left side, blue ball if hits right side
                status = (canHit) ? ((xpos_bound >= 1) ? 2 : 1) : 0; // 1 = red point, 2 = blue point
            }
        }
        return status;
    }

    private void hitX() {
        ratioX = (((random.nextFloat()+1)/2)*0.6f); //get ratio with bias to X
        ratioY = 1 - ratioX;
        velX = (velX > 0) ? -(speed*ratioX) : (speed*ratioX);
        velY = (velY < 0) ? -(speed*ratioY) : (speed*ratioY);
    }

    private void hitY() {
        velY = (velY > 0) ? -(speed*ratioY) : (speed*ratioY);
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getRadius() {
        return r;
    }

    public float getSpeed() { return speed; }

    public void reset() {
        this.x = 0.5f;
        this.y = 0.5f;
        this.velX = 1f;
        this.velY = 1f;
        this.speed = minSpeed;
        paint.setColor(0xFF000000);
    }

    public void setLevel(int level, int maxLevel) {
        this.speed = GameBoard.lerp(minSpeed, maxSpeed, (float)level/(float)maxLevel);
    }

}
