package com.biuqu.json;

import com.biuqu.model.JsonRule;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Json规则管理器
 *
 * @author BiuQu
 * @date 2023/1/5 08:44
 */
public final class JsonRuleMgr
{
    /**
     * 添加规则
     *
     * @param rules 规则集合
     */
    public static void addRule(List<JsonRule> rules)
    {
        for (JsonRule rule : rules)
        {
            CACHE.put(rule.getName(), rule);
        }
    }

    /**
     * 应用打码规则，并返回打码后的字符
     *
     * @param key   打码的key
     * @param value 待打码的value
     * @return 打码后的value
     */
    public static String applyRule(String key, String value)
    {
        if (CACHE.containsKey(key))
        {
            JsonRule rule = CACHE.get(key);
            return rule.apply(value);
        }
        return StringUtils.EMPTY;
    }

    private JsonRuleMgr()
    {
    }

    /**
     * 打码的缓存规则
     */
    private static final Map<String, JsonRule> CACHE = Maps.newConcurrentMap();
}
