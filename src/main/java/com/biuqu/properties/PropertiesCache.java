package com.biuqu.properties;

import com.biuqu.utils.PropertiesUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 属性缓存
 *
 * @author BiuQu
 * @date 2023/1/17 16:04
 */
public final class PropertiesCache
{
    /**
     * 获取properties缓存对象
     * <p>
     * 策略：先加载basePath路径下的配置，然后再用newPath配置下的配置去覆盖
     *
     * @param basePath 基础路径
     * @param newPath  新路径
     * @return properties缓存对象
     */
    public static PropertiesCache build(String basePath, String newPath)
    {
        PropertiesCache cache = build(basePath);
        return cache.append(newPath);
    }

    /**
     * 获取properties缓存对象
     *
     * @param path properties文件路径
     * @return properties缓存对象
     */
    public static PropertiesCache build(String path)
    {
        Map<String, Object> map = PropertiesUtil.getConfigs(path);
        return new PropertiesCache(map);
    }

    /**
     * 追加配置文件(builder模式)
     *
     * @param path 追加的配置路径
     * @return properties缓存对象
     */
    public PropertiesCache append(String path)
    {
        Map<String, Object> map = PropertiesUtil.getConfigs(path);
        this.cache.putAll(map);
        return this;
    }

    /**
     * 获取到指定属性的属性值(就算有多个值，也会自动转换成只取第1个)
     *
     * @param key 属性名
     * @return 第1个属性值
     */
    public String getProperty(String key)
    {
        List<String> values = getProperties(key);
        if (!CollectionUtils.isEmpty(values))
        {
            return values.get(0);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取到指定属性的属性值集合(就算只有1个值，类型也会自动转换成集合)
     *
     * @param key 属性名
     * @return 属性值集合
     */
    public List<String> getProperties(String key)
    {
        List<String> values = Lists.newArrayList();
        Object value = this.cache.get(key);
        if (value instanceof String)
        {
            values.add(value.toString());
        }
        else if (value instanceof List)
        {
            List<String> lists = (List<String>)value;
            values.addAll(lists);
        }
        return values;
    }

    private PropertiesCache(Map<String, Object> cache)
    {
        this.cache = cache;
    }

    /**
     * 缓存数据集合
     */
    private final Map<String, Object> cache;
}
