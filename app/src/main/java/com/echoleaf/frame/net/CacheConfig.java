package com.echoleaf.frame.net;

import android.content.Context;

import com.echoleaf.frame.cache.ICacheIO;
import com.echoleaf.frame.cache.RAMCacheIO;
import com.echoleaf.frame.recyle.Trash;
import com.echoleaf.frame.utils.StringUtils;

/**
 * Created by echoleaf on 2017/4/11.
 */

public class CacheConfig<R> implements Trash {
    private ICacheIO<String, String> cacheIO;

    private CacheFilter<R> cacheFilter;

    public CacheConfig() {
        this(null, null);
    }

    public CacheConfig(ICacheIO<String, String> cacheIO) {
        this(cacheIO, null);
    }

    public CacheConfig(CacheFilter<R> cacheFilter) {
        this(null, cacheFilter);
    }

    public CacheConfig(ICacheIO<String, String> cacheIO, CacheFilter<R> cacheFilter) {
        this.cacheIO = cacheIO;
        this.cacheFilter = cacheFilter;
    }


    public CacheFilter<R> getCacheFilter() {
        if (cacheFilter == null) {
            cacheFilter = new CacheFilter<R>() {

                @Override
                public boolean cache(Context context, RequestParams requestParams, String cacheKey, R result, String response) {
                    return requestParams.getMethod() == HttpMethod.GET && StringUtils.notEmpty(cacheKey) && requestParams != null && StringUtils.notEmpty(response) && result != null;
                }
            };
        }
        return cacheFilter;
    }

    public ICacheIO<String, String> getCacheIO() {
        if (cacheIO == null)
            cacheIO = RAMCacheIO.getInstence();
        return cacheIO;
    }

    public void setCacheIO(ICacheIO<String, String> cacheIO) {
        this.cacheIO = cacheIO;
    }

    public void setCacheFilter(CacheFilter<R> cacheFilter) {
        this.cacheFilter = cacheFilter;
    }

    @Override
    public void recycle() {
        cacheIO = null;
        cacheFilter = null;
    }

    public interface CacheFilter<R> {
        boolean cache(Context context, RequestParams requestParams, String cacheKey, R result, String response);
    }
}
