package com.echoleaf.frame.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.echoleaf.frame.cache.CacheObejct;
import com.echoleaf.frame.cache.ICacheIO;
import com.echoleaf.frame.recyle.Trash;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class RequestBehavior<T, R> implements Trash {
    public RequestBehavior() {
        requestHandles = new HashMap<>();
    }

    private ResponseBehavior<T> responseBehavior;
    private ResponseFormatter<T> responseFormatter;
    protected Map<R, Boolean> requestHandles;
    private CacheConfig<T> cacheConfig;

    public R request(Context context, RequestParams config) {
        responseBehavior.onRequest(context);
        R requestHandle = onRequest(context, config);
        if (requestHandle != null)
            requestHandles.put(requestHandle, true);
        return requestHandle;
    }

    public String cacheRquest(final Context context, final RequestParams requestParams) {
        final String requestKey = requestParams.generateUniqueCode();
        CacheObejct<String> cache = getCacheIO().get(context, requestKey);
        final String response = cache == null ? null : cache.getValue();
        Observable.create(new Observable.OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onNext(responseFormatter.formate(response));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<T>() {
            @Override
            public void onCompleted() {
                responseBehavior.onFinish(context);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                onFailure(context, -1, response, e);
            }

            @Override
            public void onNext(T result) {
                responseBehavior.onResponse(context, result);
            }

        });
        return requestKey;
    }

    public T cache(Context context, RequestParams params) {
        CacheObejct<String> cacheObejct = getCacheIO().get(context, params.generateUniqueCode());
        return responseFormatter.formate(cacheObejct == null ? null : cacheObejct.getValue());
    }

    protected abstract R onRequest(Context context, RequestParams params);

    protected void onCancle(R requestHandle) {
        if (requestHandle != null)
            requestHandles.put(requestHandle, false);
    }

    protected void onTimeOut(Context context) {
        responseBehavior.onTimeOut(context);
    }

    protected void onResponse(final Context context, final RequestParams requestParams, int statusCode, final String response) {
        if (statusCode == 200) {
            Observable.create(new Observable.OnSubscribe<T>() {

                @Override
                public void call(Subscriber<? super T> subscriber) {
                    T result = responseFormatter.formate(response);
                    String requestKey = requestParams == null ? "" : requestParams.generateUniqueCode();
                    if (getCacheConfig().getCacheFilter().cache(context, requestParams, response, result, requestKey)) {
                        if (getCacheIO().byteSize(context) > getCacheConfig().maxByteSiez) {
                            getCacheIO().sortOut(context);
                        }
                        getCacheIO().put(context, requestKey, response);
                    }
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<T>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    onFailure(context, -1, response, e);
                }

                @Override
                public void onNext(T result) {
                    responseBehavior.onResponse(context, result);
                }

            });
        } else {
            responseBehavior.onResponse(context, statusCode, response);
        }
    }

    protected void onFailure(Context context, int statusCode, String response, Throwable throwable) {
        responseBehavior.onFailure(context, statusCode, response, throwable);
    }

    protected void onFinish(Context context, R requestHandle) {
        if (requestHandle != null)
            requestHandles.put(requestHandle, false);
        responseBehavior.onFinish(context);
    }

    public void setResponseBehavior(ResponseBehavior<T> responseBehavior) {
        this.responseBehavior = responseBehavior;
    }

    public ResponseBehavior<T> getResponseBehavior() {
        return responseBehavior;
    }


    public void setResponseFormatter(ResponseFormatter<T> responseFormatter) {
        this.responseFormatter = responseFormatter;
    }

    public ResponseFormatter<T> getResponseFormatter() {
        return responseFormatter;
    }

    protected ICacheIO<String, String> getCacheIO() {
        return getCacheConfig().getCacheIO();
    }

    protected CacheConfig<T> getCacheConfig() {
        if (cacheConfig == null)
            cacheConfig = new CacheConfig<>();
        return cacheConfig;
    }

    public void setCacheConfig(CacheConfig<T> cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    @Override
    public void recycle() {
        if (requestHandles != null) {
            for (R requestHandle : requestHandles.keySet()) {
                if (requestHandles.get(requestHandle)) {
                    timerHander.sendEmptyMessageDelayed(DELAYED_RECYCLE, 200);
                    return;
                }
            }
            requestHandles.clear();
        }
        requestHandles = null;

        if (responseBehavior != null)
            responseBehavior.recycle();
        responseBehavior = null;

        if (cacheConfig != null)
            cacheConfig.recycle();
        cacheConfig = null;
        responseFormatter = null;
    }

    private static final int DELAYED_RECYCLE = 1;
    private Handler timerHander = new TimerHander(this);

    static class TimerHander extends Handler {
        WeakReference<Trash> weakReference;

        TimerHander(Trash trash) {
            weakReference = new WeakReference<>(trash);
        }

        public void handleMessage(Message msg) {
            if (msg.what == DELAYED_RECYCLE) {
                Trash trash = weakReference.get();
                if (trash != null)
                    trash.recycle();
            }
            super.handleMessage(msg);
        }
    }

}
