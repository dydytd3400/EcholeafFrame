package com.echoleaf.frame.net;

import android.content.Context;

import com.echoleaf.frame.recyle.Trash;

/**
 * Created by dydyt on 2017/2/21.
 */

public abstract class ResponseBehavior<T> implements Trash {

    private RenderBehavior<T> renderBehavior;

    public void onRequest(Context context) {
//        context.getMainLooper()
        renderBehavior.onRequest(context);
    }

    public void onTimeOut(Context context) {
        renderBehavior.onTimeOut(context);
    }

    public void onResponse(Context context, T result) {
        renderBehavior.onResponse(context, result);
    }

    /**
     * 响应代码不等于200时调用
     *
     * @param statusCode
     * @param response
     */
    public abstract void onResponse(Context context, int statusCode, String response);

    public void onFailure(Context context, int statusCode, String response, Throwable throwable) {
        renderBehavior.onFailure(context);
    }

    public void onFinish(Context context) {
        renderBehavior.onFinish(context);
    }

    public void setRenderBehavior(RenderBehavior<T> renderBehavior) {
        this.renderBehavior = renderBehavior;
    }

    @Override
    public void recycle() {
        if (renderBehavior != null)
            renderBehavior.recycle();
        renderBehavior = null;
    }
}
