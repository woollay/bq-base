package com.biuqu.properties;

import java.util.List;

/**
 * 属性管理器
 *
 * @author BiuQu
 * @date 2023/1/17 16:54
 */
public final class PropertiesMgr
{
    /**
     * 追加配置文件
     *
     * @param path 追加的配置路径
     */
    public void append(String path)
    {
        CACHE.append(path);
    }

    /**
     * 获取到指定属性的属性值(就算有多个值，也会自动转换成只取第1个)
     *
     * @param key 属性名
     * @return 第1个属性值
     */
    public String getProperty(String key)
    {
        return CACHE.getProperty(key);
    }

    /**
     * 获取到指定属性的属性值集合(就算只有1个值，类型也会自动转换成集合)
     *
     * @param key 属性名
     * @return 属性值集合
     */
    public List<String> getProperties(String key)
    {
        return CACHE.getProperties(key);
    }

    private PropertiesMgr()
    {
    }

    /**
     * 全局配置文件
     */
    private static final String GLOBAL_PATH = "config/global.properties";

    /**
     * 业务中自定义的配置文件
     */
    private static final String CURRENT_PATH = "config/current.properties";

    /**
     * 属性缓存
     */
    private static final PropertiesCache CACHE = PropertiesCache.build(GLOBAL_PATH, CURRENT_PATH);
}
