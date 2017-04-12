package com.echoleaf.frame.net.async;

import android.content.Context;

import com.echoleaf.frame.net.RequestBehavior;
import com.echoleaf.frame.net.RequestParams;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;


/**
 * Created by dydyt on 2017/2/21.
 */

public class AsyncRequestBehavior<R> extends RequestBehavior<R, RequestHandle> {
    private Context mContext;
    private RequestHandle requestHandle;
    private IAsyncRequester requester;
    public static final int DEFAULT_TIME_OUT_DELAY = 1000 * 10;

    public AsyncRequestBehavior(IAsyncRequester requester) {
        this.requester = requester;
    }

    @Override
    protected RequestHandle onRequest(Context context, RequestParams config) {
        if (requestHandle != null) {
            requestHandle.cancel(true);
            onCancle(requestHandle);
        }
        mContext = context;
        int timeout = config.getTimeout();
        if (timeout <= 0)
            timeout = DEFAULT_TIME_OUT_DELAY;
        Map<String, Object> params = config.getParams();
        Map<String, File[]> fileParams = config.getFileParams();
        if (params != null || fileParams != null) {
            AsyncRequestParams asyncRequestParams = new AsyncRequestParams();
            if (params != null)
                for (String key : params.keySet()) {
                    Object value = params.get(key);
                    if (value != null && value instanceof List) {
                        List list = (List) value;
                        for (Object o : list) {
                            asyncRequestParams.add(key, o == null ? "" : o.toString());
                        }
                    } else
                        asyncRequestParams.put(key, value);
                }
            if (fileParams != null)
                try {
                    for (String key : fileParams.keySet()) {
                        File[] files = fileParams.get(key);
                        if (files != null && files.length > 0)
                            asyncRequestParams.put(key, files);
                    }
                } catch (FileNotFoundException e) {
                    onFailure(context, -1, "文件不存在或已损坏", e);
                    return null;
                }
            requestHandle = requester.request(config.getMethod(), mContext, config.getAction(), asyncRequestParams, timeout, new ResponseHandler(config));
        } else
            requestHandle = requester.request(config.getMethod(), mContext, config.getAction(), timeout, new ResponseHandler(config));
        return requestHandle;
    }

    @Override
    protected void onCancle(RequestHandle requestHandle) {
        if (requestHandle != null) {
            requestHandle.cancel(true);
        }
    }

    @Override
    public void recycle() {
        mContext = null;
        if (requester != null)
            requester.recycle();
        requester = null;
        super.recycle();
    }

    class ResponseHandler extends TextHttpResponseHandler {

        private RequestParams requestParams;

        private ResponseHandler(RequestParams requestParams) {
            this.requestParams = requestParams;
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String response) {
            onResponse(mContext, requestParams, statusCode, response);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            if (requestHandle != null)
                requestHandle.cancel(true);
            if (throwable instanceof java.net.SocketTimeoutException || throwable instanceof org.apache.http.conn.ConnectTimeoutException || throwable instanceof ConnectTimeoutException) {
                onTimeOut(mContext);
            } else {
                AsyncRequestBehavior.this.onFailure(mContext, statusCode, responseString, throwable);
            }
        }

        @Override
        public void onFinish() {
            AsyncRequestBehavior.this.onFinish(mContext, requestHandle);
            requestParams = null;
        }
    }
}
