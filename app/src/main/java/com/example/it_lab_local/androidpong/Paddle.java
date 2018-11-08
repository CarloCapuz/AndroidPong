package com.example.it_lab_local.androidpong;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Paddle {

    private final float width = 0.01f;
    private final float minLength = 0.10f;
    private final float maxLength = 0.14f;

    private float axis,y, length;
    private Paint paint;

    public Paddle(float axis, int color) {
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
    }

}
