package com.jpyl.suspendedwindowdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by dg on 2017/2/16.
 */

public class MyProgressView extends View {
    private int width = 100;
    private int height = 100;
    private Paint circlePaint;
    private Paint progressPaint;
    private Paint textPaint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Path path;
    private int progress = 50;
    private int max = 100;
    private GestureDetector detector;
    private int currentProgress = 0;
    private MyRunable myRunable = new MyRunable();
    private boolean isDoubleTap = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
        }
    };
    private float h;
    private int count = 50;

    public MyProgressView(Context context) {
        super(context);
        init();
    }


    public MyProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public MyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.argb(0xff, 0x3a, 0x8c, 0x6c));
        circlePaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setColor(Color.argb(0xff, 0x41, 0xc9, 0x63));
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        progressPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(25);

        path = new Path();
        detector = new GestureDetector(new MyGestureListener());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
        setClickable(true);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Toast.makeText(getContext(), "双击", Toast.LENGTH_SHORT).show();
            startDoubleAnimation();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();
            startSingleAnimation();
            return super.onSingleTapConfirmed(e);
        }
    }

    private void startDoubleAnimation() {
        isDoubleTap = true;

        handler.postDelayed(myRunable, 50);
    }

    private void startSingleAnimation() {
        if (!isDoubleTap) {
            currentProgress = progress;
            handler.postDelayed(singleRunable, 50);

        }

    }

    private SingleRunable singleRunable = new SingleRunable();

    class SingleRunable implements Runnable {

        @Override
        public void run() {
            if (count >= 0) {
                count--;
                invalidate();
                handler.postDelayed(singleRunable, 50);
            } else {
                handler.removeCallbacks(singleRunable);
                count = 50;
            }
        }
    }

    class MyRunable implements Runnable {

        @Override
        public void run() {
            if (currentProgress <= progress) {
                handler.postDelayed(myRunable, 50);
                currentProgress++;
                invalidate();
            } else {
                handler.removeCallbacks(myRunable);
                currentProgress = 0;
                isDoubleTap = false;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        bitmapCanvas = new Canvas(bitmap);

        bitmapCanvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);
        path.reset();
        float y = (1 - (float) currentProgress / max) * width;
        path.moveTo(width, y);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.lineTo(0, y);
        if (currentProgress == 0) {
        } else {
            if (isDoubleTap) {
                h = (1 - (float) currentProgress / progress) * 10;
                for (int i = 0; i < 3; i++) {
                    path.rQuadTo(10, -h, 20, 0);
                    path.rQuadTo(10, h, 20, 0);
                }
            } else {
                h = (int) ((float) count / 50 * 10);
                for (int i = 0; i < 3; i++) {
                    if (count % 2 == 0) {

                        path.rQuadTo(10, -h, 20, 0);
                        path.rQuadTo(10, h, 20, 0);
                    } else {
                        path.rQuadTo(10, h, 20, 0);
                        path.rQuadTo(10, -h, 20, 0);
                    }
                }

            }
        }


        path.close();
        bitmapCanvas.drawPath(path, progressPaint);
        String text = (int) (((float) currentProgress / max) * 100) + "%";
        float textWidth = textPaint.measureText(text);
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        float textHeight = metrics.ascent + metrics.descent;
        bitmapCanvas.drawText(text, (width - textWidth) / 2, (height - textHeight) / 2, textPaint);

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }
}
