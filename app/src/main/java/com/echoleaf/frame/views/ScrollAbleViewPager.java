package com.echoleaf.frame.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dydyt on 2016/11/10.
 */
public class ScrollAbleViewPager extends ViewPager {
    private boolean scrollAble = true;

    public ScrollAbleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollAbleViewPager(Context context) {
        super(context);
    }

    public void setScrollAble(boolean scrollAble) {
        this.scrollAble = scrollAble;
    }

    public boolean isScrollAble() {
        return scrollAble;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        try {
            return scrollAble ? super.onTouchEvent(arg0) : false;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        try {
            return scrollAble ? super.onInterceptTouchEvent(arg0) : false;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;

    }


}
