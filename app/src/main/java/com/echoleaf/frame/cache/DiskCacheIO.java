package com.echoleaf.frame.cache;

import android.content.Context;

import com.echoleaf.frame.utils.DateUtils;
import com.echoleaf.frame.utils.FileUtils;
import com.echoleaf.frame.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by echoleaf on 2017/4/6.
 */

public class DiskCacheIO<K extends String, V extends Serializable> implements ICacheIO<K, V> {

    private DiskCacheIO() {
    }

    private static DiskCacheIO instence;

    public static DiskCacheIO getInstence() {
        if (instence == null)
            instence = new DiskCacheIO();
        return instence;
    }


    /**
     * 判断缓存是否存在
     *
     * @param cachefile
     * @return
     */
    public boolean exists(Context context, String cachefile) {
        if (StringUtils.isEmpty(cachefile))
            return false;
        File file = new File(context.getCacheDir(), cachefile);
        return file.exists();
    }


    @Override
    public CacheObejct<V> get(Context context, K key) {
        CacheObejct<V> result = readCache(context, key);
        if (result == null)
            return null;
        if (result.isExpired()) {
            remove(context, key);
            return null;
        }
        return result;
    }

    @Override
    public CacheObejct<V> get(Context context, K key, long deadline) {
        CacheObejct<V> cacheObejct = readCache(context, key);
        if (cacheObejct == null)
            return null;
        long millis = DateUtils.millisBetween(cacheObejct.getCacheTime(), new Date());
        if (deadline - millis <= 0) {
            return null;
        }
        return cacheObejct;
    }

    @Override
    public boolean put(Context context, K key, V value) {
        return saveCache(context, key, new CacheObejct<>(value));
    }

    @Override
    public boolean put(Context context, K key, V value, long shelfLife) {
        return saveCache(context, key, new CacheObejct<>(value, shelfLife));
    }

    public boolean remove(Context context, K key) {
        File cacheFile = new File(context.getCacheDir(), key);
        if (cacheFile.exists())
            return cacheFile.delete();
        return false;
    }

    @Override
    public boolean clear(Context context) {
        File[] files = context.getCacheDir().listFiles();
        if (files != null) {
            int bf = files.length;
            int af = 0;
            for (File file : files) {
                if (file != null && file.exists() && file.delete()) {
                    af++;
                }
            }
            return bf == af;
        }
        return false;
    }

    @Override
    public boolean sortOut(Context context) {
        long be = byteSize(context);
        File[] files = context.getCacheDir().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file != null && file.exists()) {
                    CacheObejct<V> cacheObejct = readCache(file);
                    if (cacheObejct == null || (cacheObejct.isExpired() || cacheObejct.getShelfLife() < 0)) {
                        file.delete();
                    }
                }
            }
        }
        long af = byteSize(context);
        return be > af;
    }

    @Override
    public boolean sortOut(Context context, long shelfLife) {
        long be = byteSize(context);
        File[] files = context.getCacheDir().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file != null && file.exists()) {
                    CacheObejct<V> cacheObejct = readCache(file);
                    if (cacheObejct == null || (shelfLife - DateUtils.millisBetween(cacheObejct.getCacheTime(), new Date()) <= 0)) {
                        file.delete();
                    }
                }
            }
        }
        long af = byteSize(context);
        return be > af;
    }

    @Override
    public int size(Context context) {
        return context.getCacheDir().list().length;
    }

    @Override
    public long byteSize(Context context) {
        return FileUtils.getDirSize(context.getCacheDir());
    }

    private CacheObejct<V> readCache(Context context, K key) {
        File cacheFile = new File(context.getCacheDir(), key);
        return readCache(cacheFile);
    }

    private CacheObejct<V> readCache(File cacheFile) {
        if (!cacheFile.exists())
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(cacheFile);
            ois = new ObjectInputStream(fis);
            return (CacheObejct<V>) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                cacheFile.delete();
            }
        } finally {
            try {
                ois.close();
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    private boolean saveCache(Context context, K key, CacheObejct<V> cache) {
        File cacheFile = new File(context.getCacheDir(), key);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(cacheFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(cache);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
                fos.close();
            } catch (Exception e) {
            }
        }
        return false;
    }

    public static <K extends String, V extends Serializable> V getCache(Context context, K key) {
        CacheObejct cacheObejct = DiskCacheIO.getInstence().get(context, key);
        return cacheObejct == null ? null : (V) cacheObejct.getValue();
    }

    public static <K extends String, V extends Serializable> V getCache(Context context, K key, long deadline) {
        CacheObejct cacheObejct = DiskCacheIO.getInstence().get(context, key, deadline);
        return cacheObejct == null ? null : (V) cacheObejct.getValue();
    }

    public static <K extends String, V extends Serializable> boolean putCache(Context context, K key, V value) {
        return DiskCacheIO.getInstence().put(context, key, value);
    }

    public static <K extends String, V extends Serializable> boolean putCache(Context context, K key, V value, long shelfLife) {
        return DiskCacheIO.getInstence().put(context, key, value, shelfLife);
    }

    public static <K extends String> boolean removeCache(Context context, K key) {
        return DiskCacheIO.getInstence().remove(context, key);
    }

}
