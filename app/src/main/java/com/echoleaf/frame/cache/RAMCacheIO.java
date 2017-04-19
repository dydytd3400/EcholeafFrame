package com.echoleaf.frame.cache;

import android.content.Context;

import com.echoleaf.frame.utils.ByteUtils;
import com.echoleaf.frame.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dydyt on 2017/2/28.
 */

public class RAMCacheIO<T, V> implements ICacheIO<T, V> {

    private RAMCacheIO() {
        map = new HashMap<>();
    }

    private static RAMCacheIO instence;
    private Map<T, CacheObejct<V>> map;

    public static <T, V> RAMCacheIO<T, V> getInstence() {
        if (instence == null)
            instence = new RAMCacheIO<>();
        return instence;
    }


    @Override
    public CacheObejct<V> get(Context context, T key) {
        CacheObejct<V> result = map.get(key);
        if (result == null)
            return null;
        if (result.isExpired()) {
            map.remove(key);
            return null;
        }
        return result;
    }

    @Override
    public CacheObejct<V> get(Context context, T key, long deadline) {
        CacheObejct<V> result = map.get(key);
        if (result == null)
            return null;
        long millis = DateUtils.millisBetween(result.getCacheTime(), new Date());
        if (deadline - millis <= 0) {
            return null;
        }
        return result;
    }

    @Override
    public boolean put(Context context, T key, V value) {
        map.put(key, new CacheObejct<>(value));
        return true;
    }

    @Override
    public boolean put(Context context, T key, V value, long shelfLife) {
        map.put(key, new CacheObejct<>(value, shelfLife));
        return true;
    }

    @Override
    public boolean remove(Context context, T key) {
        map.remove(key);
        return true;
    }

    @Override
    public boolean clear(Context context) {
        map.clear();
        return true;
    }

    @Override
    public boolean sortOut(Context context) {
        long be = byteSize(context);
        ArrayList<CacheObejct<V>> objects = new ArrayList<>();
        objects.addAll(map.values());
        for (CacheObejct<V> cacheObejct : objects) {
            if (cacheObejct != null && (cacheObejct.isExpired() || cacheObejct.getShelfLife() < 0)) {
                map.remove(cacheObejct);
            }
        }
        objects.clear();
        long af = byteSize(context);
        return be > af;
    }

    @Override
    public boolean sortOut(Context context, long shelfLife) {
        long be = byteSize(context);
        ArrayList<CacheObejct<V>> objects = new ArrayList<>();
        objects.addAll(map.values());
        for (CacheObejct<V> cacheObejct : objects) {
            if (cacheObejct != null) {
                long millis = DateUtils.millisBetween(cacheObejct.getCacheTime(), new Date());
                if (shelfLife - millis <= 0) {
                    map.remove(cacheObejct);
                }
            }
        }
        objects.clear();
        long af = byteSize(context);
        return be > af;
    }

    @Override
    public int size(Context context) {
        return map.size();
    }

    @Override
    public long byteSize(Context context) {
        byte[] bytes = ByteUtils.toByteArray(map);
        return bytes == null ? 0L : bytes.length;
    }


}
