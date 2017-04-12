package com.echoleaf.frame.net;

import android.content.Context;

import com.echoleaf.frame.recyle.Trash;

/**
 * View视图渲染层接口，负责在视图渲染层中，对网络请求所派发的事件进行处理
 * Created by 何常平 on 2017/2/21.
 */
public interface RenderBehavior<T> extends Trash {
    /**
     * 当请求发起时调用
     *
     * @param context
     */
    void onRequest(Context context);

    /**
     * 当请求发生超时异常时调用
     *
     * @param context
     */
    void onTimeOut(Context context);

    /**
     * 当接收到服务端返回结果时调用
     *
     * @param context
     * @param result
     */
    void onResponse(Context context, T result);

    /**
     * 当捕获到请求过程中出现的除超时以外的所有异常时调用
     *
     * @param context
     */
    void onFailure(Context context);

    /**
     * 当请求结束后调用，无论请求成功与否，都会调用
     *
     * @param context
     */
    void onFinish(Context context);

}
