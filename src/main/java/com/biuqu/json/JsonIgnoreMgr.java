package com.biuqu.json;

import com.biuqu.model.JsonIgnores;
import com.biuqu.model.ResultCode;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Json模型的属性忽略管理器
 *
 * @author BiuQu
 * @date 2023/6/8 19:59
 */
public class JsonIgnoreMgr
{
    public JsonIgnoreMgr(List<JsonIgnores> batchIgnores)
    {
        for (JsonIgnores ignore : batchIgnores)
        {
            CACHE.put(ignore.getClazz(), ignore.getFields());
        }
    }

    /**
     * 获取模型的忽略列表
     *
     * @param model 业务模型
     * @return 模型的忽略列表
     */
    public Set<String> getIgnores(Object model)
    {
        String className = model.getClass().getName();
        if (model instanceof ResultCode)
        {
            ResultCode resultCode = (ResultCode)model;
            Object data = resultCode.getData();
            if (null != data)
            {
                className = data.getClass().getName();
            }
        }

        if (CACHE.containsKey(className))
        {
            return CACHE.get(className);
        }
        return Sets.newHashSet();
    }

    /**
     * 忽略的缓存规则
     */
    private static final Map<String, Set<String>> CACHE = Maps.newConcurrentMap();
}
