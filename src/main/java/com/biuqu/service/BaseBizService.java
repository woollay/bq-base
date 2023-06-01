package com.biuqu.service;

import com.biuqu.cache.CacheFactory;
import com.biuqu.model.BaseSecurity;
import com.biuqu.utils.JsonUtil;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 抽象业务服务实现(不做加密机处理的抽象实现):
 * 1.默认给出所有接口服务的空实现，让最终服务可以按需覆写即可
 * 2.默认添加本地缓存，避免频繁查询数据库，可覆写{@link BaseBizService#hasCached()}覆写，覆写之后只有默认的空实现
 *
 * @author BiuQu
 * @date 2023/1/21 22:32
 */
@Slf4j
public abstract class BaseBizService<T extends BaseSecurity> implements Service<T>
{
    @Override
    public int add(T model)
    {
        return 0;
    }

    @Override
    public T get(T model)
    {
        String key = StringUtils.EMPTY;
        try
        {
            if (hasCached())
            {
                key = model.toKey();
                if (!StringUtils.isEmpty(key))
                {
                    return cache.get(key);
                }
            }
        }
        catch (ExecutionException e)
        {
            log.error("no cache[{}] found:{},with exception:{}", this.getClass().getSimpleName(), key, e);
        }
        return null;
    }

    @Override
    public List<T> getBatch(T model)
    {
        List<T> results = Lists.newArrayList();
        String key = StringUtils.EMPTY;
        try
        {
            if (hasCached())
            {
                key = model.toBatchKey();
                if (!StringUtils.isEmpty(key))
                {
                    results = batchCache.get(key);
                }
            }
        }
        catch (ExecutionException e)
        {
            log.error("no batch cache[{}] found:{},with exception:{}", this.getClass().getSimpleName(), key, e);
        }
        return results;
    }

    @Override
    public List<T> batchGet(List<T> batch)
    {
        List<String> keys = Lists.newArrayList();
        for (T model : batch)
        {
            keys.add(model.toKey());
        }

        List<T> bestResults = Lists.newArrayList();
        try
        {
            Map<String, List<T>> batchResult = batchCache.getAll(keys);
            for (T model : batch)
            {
                String key = model.toKey();
                List<T> subResult = batchResult.get(key);
                subResult = bestChoose(subResult);
                if (!CollectionUtils.isEmpty(subResult))
                {
                    bestResults.addAll(subResult);
                }
            }
        }
        catch (ExecutionException e)
        {
            log.error("no batch caches[{}] found:{},{}", this.getClass().getSimpleName(), JsonUtil.toJson(keys), e);
        }
        return bestResults;
    }

    @Override
    public int update(T model)
    {
        return 0;
    }

    @Override
    public int delete(T model)
    {
        if (hasCached())
        {
            cache.invalidate(model.getId());
        }
        return 0;
    }

    /**
     * 是否有缓存(可覆写)
     *
     * @return 默认有缓存
     */
    protected boolean hasCached()
    {
        return true;
    }

    /**
     * 根据key查询获取业务模型对象(给默认空实现)
     *
     * @param key 唯一key
     * @return 业务模型对象
     */
    protected T queryByKey(String key)
    {
        return null;
    }

    /**
     * 根据key查询获取批量的业务模型对象(给默认空实现)
     *
     * @param key 唯一key
     * @return 批量的业务模型对象
     */
    protected List<T> queryBatchByKey(String key)
    {
        return null;
    }

    /**
     * 批量查询场景(给默认空实现)
     *
     * @param keys 批量key
     * @return 批量key对应的结果对象集合
     */
    protected List<T> queryBatchByKeys(Iterable<? extends String> keys)
    {
        return null;
    }

    /**
     * 批量结果对象中挑选
     *
     * @param batch 批量结果
     * @return 挑选后的对象
     */
    protected List<T> bestChoose(List<T> batch)
    {
        return batch;
    }

    /**
     * 本地缓存对象
     */
    private final LoadingCache<String, T> cache = CacheFactory.create(new CacheLoader<String, T>()
    {
        @Override
        public T load(String key)
        {
            return queryByKey(key);
        }
    });

    /**
     * 批量的本地缓存对象
     */
    private final LoadingCache<String, List<T>> batchCache = CacheFactory.create(new CacheLoader<String, List<T>>()
    {
        @Override
        public List<T> load(String key)
        {
            return queryBatchByKey(key);
        }

        @Override
        public Map<String, List<T>> loadAll(Iterable<? extends String> keys)
        {
            List<T> queryResults = queryBatchByKeys(keys);
            if (CollectionUtils.isEmpty(queryResults))
            {
                return Maps.newHashMap();
            }

            Map<String, List<T>> results = Maps.newHashMap();
            for (T model : queryResults)
            {
                String key = model.toKey();
                List<T> subResults = results.get(key);
                if (null == subResults)
                {
                    subResults = Lists.newArrayList();
                    results.put(key, subResults);
                }
                subResults.add(model);
            }
            return results;
        }
    });
}
