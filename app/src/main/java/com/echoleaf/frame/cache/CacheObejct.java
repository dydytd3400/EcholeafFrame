package com.echoleaf.frame.cache;


import com.echoleaf.frame.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dydyt on 2017/2/28.
 */

public class CacheObejct<T> implements Serializable {

    public CacheObejct(T value) {
        this(value, -1);
    }

    public CacheObejct(T value, Date deadline) {
        this(value, DateUtils.millisBetween(new Date(), deadline));
    }

    public CacheObejct(T value, long shelfLife) {
        this.value = value;
        this.shelfLife = shelfLife;
        this.cacheTime = new Date();
    }

    private T value;
    private long shelfLife;
    private Date cacheTime;

    public T getValue() {
        return value;
    }

    public Date getCacheTime() {
        return cacheTime;
    }

    public long getShelfLife() {
        return shelfLife;
    }

    public boolean isExpired() {
        long millis = DateUtils.millisBetween(cacheTime, new Date());
        return shelfLife != -1 && shelfLife - millis <= 0;
    }
}
