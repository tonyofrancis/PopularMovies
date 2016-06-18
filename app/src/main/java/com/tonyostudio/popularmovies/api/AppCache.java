package com.tonyostudio.popularmovies.api;

import android.support.v4.util.LruCache;

/**
 * Created by tonyofrancis on 6/5/16.
 */

public final class AppCache extends LruCache<String,Object> {

    private static final int DEFAULT_SIZE = 6;
    private static AppCache sAppCache;

    private AppCache(int maxSize) {
        super(maxSize);
    }

    public static void initialize() {
        initialize(DEFAULT_SIZE);
    }
    public static void initialize(int maxSize) {

        if(sAppCache == null) {
            sAppCache = new AppCache(maxSize);
        }
    }

    public static void store(String key, Object value) {
        sAppCache.put(key,value);
    }

    public static Object retrieve(String key) {

        final Object object = sAppCache.get(key);
        sAppCache.remove(key);

        return object;
    }

    public static void reduceCache() {
        sAppCache.resize(sAppCache.maxSize() / 2);
    }
}
