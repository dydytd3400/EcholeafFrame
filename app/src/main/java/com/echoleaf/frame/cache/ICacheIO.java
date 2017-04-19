package com.echoleaf.frame.cache;

import android.content.Context;

/**
 * Created by dydyt on 2017/2/28.
 */

public interface ICacheIO<T, V> {
    /**
     * 读取缓存对象
     *
     * @param context
     * @param key
     * @return
     */
    CacheObejct<V> get(Context context, T key);

    /**
     * 读取缓存对象
     *
     * @param context
     * @param key
     * @param shelfLife 如果缓存的保存时间已经超过指定的生命周期，则返回null
     * @return
     */
    CacheObejct<V> get(Context context, T key, long shelfLife);

    /**
     * 保存缓存对象
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    boolean put(Context context, T key, V value);

    /**
     * 保存缓存对象
     *
     * @param context
     * @param key
     * @param value
     * @param shelfLife 为缓存对象指定生命周期，当读取时如若超过了该周期，则返回null
     * @return
     */
    boolean put(Context context, T key, V value, long shelfLife);

    /**
     * 删除指定缓存对象
     *
     * @param context
     * @param key
     * @return
     */
    boolean remove(Context context, T key);

    /**
     * 清空缓存
     * 删除所有缓存对象
     *
     * @param context
     * @return
     */
    boolean clear(Context context);

    /**
     * 清理缓存
     * 删除所有通过<code> put(Context context, T key, V value, long shelfLife) </code>方法保存且已经过期的缓存
     *
     * @param context
     * @return
     */
    boolean sortOut(Context context);

    /**
     * 清理缓存
     * 删除所有生命周期超过了shelfLife的缓存对象
     *
     * @param context
     * @param shelfLife
     * @return
     */
    boolean sortOut(Context context, long shelfLife);

    int size(Context context);

    long byteSize(Context context);


}
