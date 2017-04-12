package com.echoleaf.frame.net;

import android.content.Context;

import com.echoleaf.frame.recyle.Trash;
import com.echoleaf.frame.utils.StringUtils;

/**
 * Created by dydyt on 2017/2/21.
 */

public class HttpClientBehavior<T, R> implements Trash {

    private HttpClientBehavior() {
    }

    private RequestBehavior<T, R> requestBehavior;

    public R request(Context context, RequestParams requestParams) {
        if (requestParams == null)
            throw new NullPointerException("RequestParams is Null");
        if (StringUtils.isEmpty(requestParams.getAction()))
            throw new NullPointerException("Service Action is Null");
        return requestBehavior.request(context, requestParams);
    }

    public String cacheRquest(Context context, RequestParams requestParams) {
        if (requestParams == null)
            throw new NullPointerException("RequestParams is Null");
        if (StringUtils.isEmpty(requestParams.getAction()))
            throw new NullPointerException("Service Action is Null");
        return requestBehavior.cacheRquest(context, requestParams);
    }

    public T cache(Context context, RequestParams requestParams) {
        return requestBehavior.cache(context, requestParams);
    }

    public CacheConfig<T> getCacheConfig() {
        return requestBehavior.getCacheConfig();
    }

    public void setCacheConfig(CacheConfig<T> cacheConfig) {
        requestBehavior.setCacheConfig(cacheConfig);
    }


    @Override
    public void recycle() {
        if (requestBehavior != null)
            requestBehavior.recycle();
        requestBehavior = null;
    }

    public static class Builder<T, R> {
        HttpClientBehavior<T, R> httpClientBehavior;

        public static <T, R> Builder<T, R> newBuilder(Class<T> resultClass, Class<R> handleClass) {
            return new Builder<>();
        }

        private Builder() {
            httpClientBehavior = new HttpClientBehavior<>();
        }

        public Builder<T, R> requestBehavior(RequestBehavior<T, R> requestBehavior) {
            httpClientBehavior.requestBehavior = requestBehavior;
            return this;
        }

        public Builder<T, R> cacheConfig(CacheConfig<T> cacheConfig) {
            httpClientBehavior.requestBehavior.setCacheConfig(cacheConfig);
            return this;
        }

        public Builder<T, R> responseFormatter(ResponseFormatter<T> responseFormatter) {
            httpClientBehavior.requestBehavior.setResponseFormatter(responseFormatter);
            return this;
        }

        public Builder<T, R> responseBehavior(ResponseBehavior<T> responseBehavior) {
            httpClientBehavior.requestBehavior.setResponseBehavior(responseBehavior);
            return this;
        }

        public Builder<T, R> renderBehavior(RenderBehavior<T> renderBehavior) {
            httpClientBehavior.requestBehavior.getResponseBehavior().setRenderBehavior(renderBehavior);
            return this;
        }

        public HttpClientBehavior build() {
            return httpClientBehavior;
        }

    }
}
