package com.biuqu.cache;

import com.google.common.cache.*;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 基于Guava的缓存工厂
 *
 * @author BiuQu
 * @date 2023/1/4 15:48
 */
public final class CacheFactory
{
    /**
     * 创建缓存对象
     *
     * @param loader 缓存加载器
     * @param <T>    缓存对象类型
     * @return 缓存对象
     */
    public static <T> LoadingCache<String, T> create(CacheLoader<String, T> loader)
    {
        return create(loader, CACHE_DAY, TimeUnit.DAYS);
    }

    /**
     * 创建缓存对象
     *
     * @param loader   缓存加载器
     * @param duration 缓存有效期
     * @param unit     缓存有效期的时间单位
     * @param <T>      缓存对象类型
     * @return 缓存对象
     */
    public static <T> LoadingCache<String, T> create(CacheLoader<String, T> loader, Long duration, TimeUnit unit)
    {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        builder.maximumSize(MAX_NUM);
        if (null == unit)
        {
            unit = TimeUnit.DAYS;
        }
        if (null == duration)
        {
            duration = CACHE_DAY;
        }
        builder.refreshAfterWrite(duration, unit);
        builder.expireAfterAccess(duration, unit);
        builder.expireAfterWrite(duration, unit);

        builder.removalListener(new RemovalListener<String, T>()
        {
            @Override
            public void onRemoval(RemovalNotification<String, T> rn)
            {
                RemovalCause cause = rn.getCause();
                String key = rn.getKey();
                T value = rn.getValue();
                LOGGER.info("cache[{}] expired with value:{},cause:{}", key, value, cause.name());
            }
        });

        LoadingCache<String, T> cache = builder.build(loader);
        CACHES.add(cache);

        return cache;
    }

    /**
     * 清空所有缓存
     */
    public static void invalidateAll()
    {
        for (LoadingCache<String, ?> cache : CACHES)
        {
            cache.invalidateAll();
        }
    }

    private CacheFactory()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheFactory.class);

    /**
     * 缓存对象集合
     */
    private static final List<LoadingCache<String, ?>> CACHES = Lists.newArrayList();

    /**
     * 默认缓存天数
     */
    private static final long CACHE_DAY = 1;

    /**
     * 默认缓存刷新的时间(分钟)
     */
    private static final int CACHE_REFRESH = 60;

    /**
     * 最多缓存100w个对象
     */
    private static final int MAX_NUM = 1000000;
}
