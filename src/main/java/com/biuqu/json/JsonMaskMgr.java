package com.biuqu.json;

import com.biuqu.model.JsonMask;
import com.biuqu.model.JsonRule;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Json打码管理器
 *
 * @author BiuQu
 * @date 2023/6/8 16:03
 */
public class JsonMaskMgr
{
    /**
     * 构造方法，初始化所有规则
     *
     * @param masks 打码规则
     */
    public JsonMaskMgr(List<JsonMask> masks)
    {
        for (JsonMask mask : masks)
        {
            String className = mask.getClazz();
            List<JsonRule> rules = mask.getRules();
            Map<String, JsonRule> ruleCache = Maps.newConcurrentMap();
            for (JsonRule rule : rules)
            {
                ruleCache.put(rule.getName(), rule);
            }
            MASK_CACHE.put(className, ruleCache);
        }
    }

    /**
     * 应用打码规则，并返回打码后的字符
     *
     * @param className 打码的全路径类名
     * @param key       打码的key
     * @param value     待打码的value
     * @return 打码后的value
     */
    public String applyRule(String className, String key, String value)
    {
        if (MASK_CACHE.containsKey(className))
        {
            Map<String, JsonRule> ruleCache = MASK_CACHE.get(className);
            if (ruleCache.containsKey(key))
            {
                JsonRule rule = ruleCache.get(key);
                return rule.apply(value);
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * 打码的缓存规则
     */
    private static final Map<String, Map<String, JsonRule>> MASK_CACHE = Maps.newConcurrentMap();
}
