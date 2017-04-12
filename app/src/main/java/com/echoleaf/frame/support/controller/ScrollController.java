package com.echoleaf.frame.support.controller;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;


/**
 * 滑动变更拟态StatusBar颜色的控制类
 * Created by 何常平 on 2016/12/29.
 */
public abstract class ScrollController implements TouchEventController {
    View mContentView;


    public ScrollController(View contentView) {
        this.mContentView = contentView;
    }

    private void processChange() {
        if (recycling)
            return;
        onInertiaScrolling();
    }

    /**
     * 惯性滑动时调用该方法
     */
    protected abstract void onInertiaScrolling();

    private int lastY = 0;
    private static final int SCROLL_TIME = 50;
    private static final int SCROLL_WHAT = 111;
    private boolean recycling = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCROLL_WHAT:
                    if (recycling)
                        return;
                    try {
                        int scrollY = getContentViewY();
                        if (lastY != scrollY) {
                            lastY = scrollY;
                            processChange();
                            handler.sendEmptyMessageDelayed(SCROLL_WHAT, SCROLL_TIME);
                        }
                    } catch (Exception e) {
                        //可能会由于线程不同步而导致在对垃圾内存回收时，出现空指针异常，故在此强行捕获
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    protected int getContentViewY() {
        int[] bgLocation = new int[2];
        mContentView.getLocationInWindow(bgLocation);
        return bgLocation[1];
    }


    @Override
    public boolean processEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                lastY = getContentViewY();
                handler.sendEmptyMessageDelayed(SCROLL_WHAT, SCROLL_TIME);
                break;
        }
        return true;
    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    @Override
    public void recycle() {
        recycling = true;
        if (handler != null) {
            handler.removeMessages(SCROLL_WHAT);
            handler = null;
        }
        mContentView = null;
    }
}
