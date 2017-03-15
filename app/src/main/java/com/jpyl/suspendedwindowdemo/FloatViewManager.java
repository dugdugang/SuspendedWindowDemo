package com.jpyl.suspendedwindowdemo;

import android.content.Context;
import android.graphics.PixelFormat;
import android.icu.text.NumberFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by dg on 2017/2/15.
 */

public class FloatViewManager implements View.OnTouchListener {
    private static FloatViewManager instance;
    private final FloatMenuView floatMenuView;
    private Context context;
    private static WindowManager wm;//控制浮动窗体的显示deng
    private static FloatView floatView;
    private float moveY;
    private float moveX;
    private float curY;
    private float curX;
    private float preY;
    private float preX;
    private static WindowManager.LayoutParams lp;
    private float upX;
    private float downY;
    private float downX;
    private float upY;

    public FloatViewManager(Context context) {
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        floatView = new FloatView(context);
        floatView.setOnTouchListener(this);
        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("M-TAG", "click");
                showFloatMenuView();
                wm.removeView(floatView);
                floatMenuView.startAnimtion();
            }


        });
        floatMenuView = new FloatMenuView(context);

    }

    private void showFloatMenuView() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = (int) getScreenWidth();
        layoutParams.height = (int) (getScreenHeight() - getStatebarHeight());
        layoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.format = PixelFormat.RGBA_8888;
        wm.addView(floatMenuView, layoutParams);
    }

    private float getScreenHeight() {
        return wm.getDefaultDisplay().getHeight();
    }

    private float getStatebarHeight() {
        float h = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            h = context.getResources().getDimensionPixelSize(resourceId);
        }
        return h;
    }

    public static FloatViewManager getInstance(Context context) {
        if (instance == null) {
            synchronized (FloatViewManager.class) {
                if (instance == null) {
                    instance = new FloatViewManager(context);
                }
            }
        }
        return instance;
    }

    public static void showFloatView() {
        if (lp == null) {
            lp = new WindowManager.LayoutParams();
            lp.width = floatView.width;
            lp.height = floatView.height;
            lp.gravity = Gravity.TOP | Gravity.LEFT;
            lp.type = WindowManager.LayoutParams.TYPE_TOAST;
            lp.x = 0;
            lp.y = 0;
            lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            lp.format = PixelFormat.RGBA_8888;
        }

        wm.addView(floatView, lp);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                floatView.setDragState(true);
                downX = event.getRawX();
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                Log.i("M-TAG", "up");
                floatView.setDragState(false);

                upX = event.getRawX();
                upY = event.getRawY();
                if (upX < getScreenWidth() / 2) {
                    lp.x = 0;
                } else {
                    lp.x = (int) (getScreenWidth() - floatView.width);
                }
                wm.updateViewLayout(floatView, lp);
                if (upX - downX > 6)
                    return true;
                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getRawX();
                curY = event.getRawY();
                //  moveX = curX - preX;
                // moveY = curY - preY;
                lp.x = (int) curX - floatView.width / 2;
                lp.y = (int) curY - floatView.width / 2;
                wm.updateViewLayout(floatView, lp);
                //  preX = curX;
                // preY = curY;
                break;
        }
        return false;
    }

    private float getScreenWidth() {
        return wm.getDefaultDisplay().getWidth();
    }

    public void hiddenView() {
        wm.removeView(floatMenuView);
    }
}
