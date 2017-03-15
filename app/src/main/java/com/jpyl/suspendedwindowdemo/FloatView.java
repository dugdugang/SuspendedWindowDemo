package com.jpyl.suspendedwindowdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import static android.R.attr.bitmap;

/**
 * Created by dg on 2017/2/15.
 */

public class FloatView extends View {
    private Paint circlePaint;
    private Paint textPaint;
    public int width = 150;
    public int height = 150;
    private String text = "50%";
    private boolean drag;
    private Bitmap bitmap;

    public FloatView(Context context) {
        super(context);
        initPaint();
    }

    private void initPaint() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setTextSize(25);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

    }

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();

    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drag) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        } else {
            canvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);
            float textWidth = textPaint.measureText(text);
            float x = width / 2 - textWidth / 2;
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float dy = -(fontMetrics.ascent + fontMetrics.descent) / 2;
            float y = height / 2 + dy;
            canvas.drawText(text, x, y, textPaint);
        }

    }

    public void setDragState(boolean b) {
        this.drag = b;
        invalidate();
    }
}
