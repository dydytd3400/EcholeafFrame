package com.echoleaf.frame.support.controller;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import com.echoleaf.frame.R;


/**
 * 滑动变更拟态StatusBar颜色的控制类
 * Created by 何常平 on 2016/12/29.
 */
public class ScrollStatusController extends ScrollController {

    public static final float COLOR_AREA_SCALE = 0.2f;
    View mContentView, mBackgroundView;

    private ArgbEvaluator evaluator;
    private int beforeColor;
    private int afterColor;
    private float colorAreaScale;

    private ScrollStatusController(Context context, View contentView, View backgroundView) {
        this(context, contentView, backgroundView, R.color.full_transparent, R.color.black, COLOR_AREA_SCALE);
    }

    private ScrollStatusController(Context context, View contentView, View backgroundView, int beforeColor, int afterColor, float colorAreaScale) {
        super(backgroundView);
        this.beforeColor = ContextCompat.getColor(context, beforeColor);
        this.afterColor = ContextCompat.getColor(context, afterColor);
        this.mContentView = contentView;
        this.mBackgroundView = backgroundView;
        this.colorAreaScale = colorAreaScale;
        evaluator = new ArgbEvaluator();
    }

    @Override
    protected void onInertiaScrolling() {
        // 获取背景VIEW高度，获取内容VIEW高度，计算高度差进行判断
        int bgHeight = mBackgroundView.getHeight();
        int ctHeight = mContentView.getHeight();
        if (bgHeight > 0 && ctHeight > 0) {//当View高度已经被测量后才进行计算
            int[] bgLocation = new int[2];
            mBackgroundView.getLocationInWindow(bgLocation);
            float scrollAbleHeight = bgHeight - ctHeight;//可滑动区域
            float fraction;//背景色偏移量
            float remainingArea = scrollAbleHeight + bgLocation[1];//剩余可滑动区域
            float colorArea = bgHeight * colorAreaScale;
            if (remainingArea >= colorArea) {//如果剩余可滑动区域大于不变色区域
                fraction = 0;//则不改变背景色
            } else if (remainingArea >= 0) {//当执行到该if判断时，剩余可滑动区域已经完全是可变色区域
                fraction = (colorArea - remainingArea) / colorArea;
            } else {//当执行到该else时，则表示已经超出了可滑动区域，此时背景色完全填充，不再变更
                fraction = 1;
            }
            handleBackgroundColor(fraction);
        }
    }


    /**
     * 控制背景颜色和透明度
     *
     * @param fraction
     */
    private void handleBackgroundColor(float fraction) {
        int colorValue = (int) evaluator.evaluate(fraction, beforeColor, afterColor);
        mContentView.setBackgroundColor(colorValue);
    }


    @Override
    public boolean processEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                onInertiaScrolling();
                break;
            default:
                super.processEvent(event);
                break;
        }
        return true;
    }


    @Override
    public void recycle() {
        evaluator = null;
        mContentView = null;
        mBackgroundView = null;
        super.recycle();
    }


    /**
     * 创建一个Controller
     *
     * @param context
     * @return
     */
    public static Builder Builder(Context context) {
        return new Builder(context);
    }

    /**
     * 配置构建类
     *
     * @return
     */
    public static class Builder {
        private ScrollStatusController controller;
        private Context context;

        private Builder(Context context) {
            this.context = context;
            controller = new ScrollStatusController(context, null, null);
        }

        /**
         * 滑动前颜色
         *
         * @param beforeColor colorId
         * @return
         */
        public Builder beforeColor(int beforeColor) {
            controller.beforeColor = ContextCompat.getColor(context, beforeColor);
            return this;
        }

        /**
         * 滑动后颜色
         *
         * @param afterColor colorId
         * @return
         */
        public Builder afterColor(int afterColor) {
            controller.afterColor = ContextCompat.getColor(context, afterColor);
            return this;
        }

        /**
         * 变色区域的背景View
         *
         * @param backgroundView
         * @return
         */
        public Builder backgroundView(View backgroundView) {
            controller.mBackgroundView = backgroundView;
            controller.setContentView(backgroundView);
            return this;
        }

        /**
         * 实际变色的View
         *
         * @param contentView
         * @return
         */
        public Builder contentView(View contentView) {
            controller.mContentView = contentView;
            return this;
        }

        /**
         * 变色区域的占比
         *
         * @param colorAreaScale
         * @return
         */
        public Builder colorAreaScale(float colorAreaScale) {
            controller.colorAreaScale = colorAreaScale;
            return this;
        }

        /**
         * 完成构建
         *
         * @return
         */
        public ScrollStatusController build() {
            if (controller.mContentView == null)
                throw new RuntimeException("contentView is null");
            if (controller.mBackgroundView == null)
                throw new RuntimeException("backgroundView is null");
            return controller;
        }

    }

}
