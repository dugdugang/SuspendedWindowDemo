package com.jpyl.suspendedwindowdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by dg on 2017/2/16.
 */

public class FloatMenuView extends LinearLayout {

    private RelativeLayout ll;
    private TranslateAnimation animation;

    public FloatMenuView(Context context) {
        super(context);
        init();
    }


    public FloatMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public FloatMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.floatmenu, null);
        ll = (RelativeLayout) view.findViewById(R.id.ll);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        ll.setAnimation(animation);
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatViewManager manager = FloatViewManager.getInstance(getContext());
                manager.hiddenView();
                manager.showFloatView();
                return false;
            }
        });
        addView(view);
    }

    public void startAnimtion() {
        animation.start();
    }

}
