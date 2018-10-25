package com.example.it_lab_local.androidpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Ball {

    private float r, x, y, speed;
    private float velX, velY, ratioX, ratioY;

    private Paint paint;

    Random random = new Random();

    side hit = side.None;

    public enum side { None, Left, Right, Top, Bottom }

    public Ball(float r, float x, float y) {
        this.r = r;
        this.x = x;
        this.y = y;
        this.velX = 1f;
        this.velY = 1f;
        this.speed = 0.01f;
        paint = new Paint();
        paint.setColor(0xFF000000); //Start off as black
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(canvas.getWidth()*x,canvas.getHeight()*y,canvas.getWidth()*r,paint);
    }

    public void tick(Canvas canvas) {
        float prevX = x;
        float prevY = y;
        x += velX;
        y += velY;
        float xpos_bound = x + r;
        float xneg_bound = x - r;
        float ypos_bound = y + r;
        float yneg_bound = y - r;

        if (ypos_bound >= 1 || yneg_bound <= 0) {
            y = prevY;
            hitY();
            hit = (ypos_bound >= 1) ? side.Top : side.Bottom;
        }

        if (xpos_bound >= 1 || xneg_bound <= 0) {
            x = prevX;
            hitX();
            hit = (xpos_bound >= 1) ? side.Right : side.Left;
            paint.setColor((xpos_bound >= 1) ? 0xFF0000FF : 0xFFFF0000); //Red ball if hits left side, blue ball if hits right side
        }

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



}
