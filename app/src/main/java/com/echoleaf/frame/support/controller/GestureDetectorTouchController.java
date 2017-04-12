package com.echoleaf.frame.support.controller;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by dydyt on 2017/3/16.
 * 对TouchEvent进行分类派发，
 */
public class GestureDetectorTouchController implements TouchEventController {
    GestureDetector gestureDetector;

    public GestureDetectorTouchController(Context context, GestureDetector.SimpleOnGestureListener listener) {
        gestureDetector = new GestureDetector(context, listener);
    }

    @Override
    final public boolean processEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void recycle() {
        gestureDetector = null;
    }


}
