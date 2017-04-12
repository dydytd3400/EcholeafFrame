package com.echoleaf.frame.support.controller;

import android.view.MotionEvent;

import com.echoleaf.frame.recyle.Trash;

/**
 * Created by dydyt on 2016/12/29.
 */

public interface TouchEventController  extends Trash{
    boolean processEvent(MotionEvent event);
}
