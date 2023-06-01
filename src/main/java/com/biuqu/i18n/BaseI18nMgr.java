package com.biuqu.i18n;

import com.biuqu.utils.I18nUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * 抽象的国际化文件管理器
 *
 * @author BiuQu
 * @date 2023/1/7 19:02
 */
public abstract class BaseI18nMgr
{
    /**
     * 获取国际化配置(默认获取中文)
     *
     * @param key 国际化key
     * @return 国际化key对应的值
     */
    public String get(String key)
    {
        return this.get(CACHE, key);
    }

    /**
     * 获取国际化配置
     *
     * @param locale 语言
     * @param key    国际化key
     * @return 国际化key对应的值
     */
    public String get(Locale locale, String key)
    {
        return this.get(CACHE, locale, key);
    }

    /**
     * 获取指定语言的所有国际化配置(默认获取中文)
     *
     * @return 所有配置的国际化
     */
    public Map<String, String> getI18n()
    {
        return this.getI18n(CACHE);
    }

    /**
     * 获取指定语言的所有国际化配置
     *
     * @param locale 语言类型
     * @return 所有配置的国际化
     */
    public Map<String, String> getI18n(Locale locale)
    {
        return this.getI18n(CACHE, locale);
    }

    /**
     * 获取所有语言的所有国际化配置
     *
     * @return 所有配置的国际化
     */
    public Map<String, Map<String, String>> getI18ns()
    {
        return this.getI18ns(CACHE);
    }

    /**
     * 清除所有的国际化缓存
     */
    public void clear()
    {
        this.clear(CACHE);
    }

    /**
     * 加载国际化文件中的内容(读取默认路径下的文件:classpath:i18n/18n)
     *
     * @param locale 语言类型
     */
    public void loadI18n(Locale locale)
    {
        this.loadI18n(CACHE, locale);
    }

    /**
     * 加载国际化文件中的内容
     *
     * @param path   文件路径(注意不能带语言及properties后缀，即不能包含zh_CN.properties)
     * @param locale 语言类型
     */
    public void loadI18n(String path, Locale locale)
    {
        this.loadI18n(CACHE, path, locale);
    }

    /**
     * 获取国际化配置(默认获取中文)
     *
     * @param cache 缓存对象
     * @param key   国际化key
     * @return 国际化key对应的值
     */
    protected String get(Map<String, Map<String, String>> cache, String key)
    {
        return this.get(cache, Locale.SIMPLIFIED_CHINESE, key);
    }

    /**
     * 获取国际化配置
     *
     * @param cache  缓存对象
     * @param locale 语言
     * @param key    国际化key
     * @return 国际化key对应的值
     */
    protected String get(Map<String, Map<String, String>> cache, Locale locale, String key)
    {
        if (null == locale || StringUtils.isEmpty(key))
        {
            LOGGER.warn("invalid language i18n key.");
            return StringUtils.EMPTY;
        }

        Map<String, String> i18n = this.getI18n(cache, locale);
        if (!i18n.containsKey(key))
        {
            return StringUtils.EMPTY;
        }
        return i18n.get(key);
    }

    /**
     * 获取指定语言的所有国际化配置(默认获取中文)
     *
     * @param cache 缓存对象
     * @return 所有配置的国际化
     */
    protected Map<String, String> getI18n(Map<String, Map<String, String>> cache)
    {
        return this.getI18n(cache, Locale.SIMPLIFIED_CHINESE);
    }

    /**
     * 获取指定语言的所有国际化配置
     *
     * @param cache  缓存对象
     * @param locale 语言类型
     * @return 所有配置的国际化
     */
    protected Map<String, String> getI18n(Map<String, Map<String, String>> cache, Locale locale)
    {
        if (locale == null)
        {
            LOGGER.warn("invalid language i18n.");
            return Maps.newHashMap();
        }

        String language = locale.toString();
        if (!CACHE.containsKey(language))
        {
            LOGGER.warn("no language i18n:{}", language);
            return Maps.newHashMap();
        }

        return Collections.unmodifiableMap(cache.get(language));
    }

    /**
     * 获取所有语言的所有国际化配置
     *
     * @param cache 缓存对象
     * @return 所有配置的国际化
     */
    protected Map<String, Map<String, String>> getI18ns(Map<String, Map<String, String>> cache)
    {
        return Collections.unmodifiableMap(cache);
    }

    /**
     * 清除所有的国际化缓存
     *
     * @param cache 缓存对象
     */
    protected void clear(Map<String, Map<String, String>> cache)
    {
        LOGGER.info("clear all i18n cache now.");
        cache.clear();
    }

    /**
     * 加载国际化文件中的内容(读取默认路径下的文件:classpath:i18n/18n)
     *
     * @param cache  缓存对象
     * @param locale 语言类型
     */
    protected void loadI18n(Map<String, Map<String, String>> cache, Locale locale)
    {
        loadI18n(cache, this.getI18nPath(), locale);
    }

    /**
     * 加载国际化文件中的内容
     *
     * @param cache  缓存对象
     * @param path   文件路径(注意不能带语言及properties后缀，即不能包含zh_CN.properties)
     * @param locale 语言类型
     */
    protected void loadI18n(Map<String, Map<String, String>> cache, String path, Locale locale)
    {
        cache.putAll(I18nUtil.loadI18n(path, locale));
    }

    /**
     * 获取国际化文件的路径
     *
     * @return 国际化文件的路径
     */
    protected abstract String getI18nPath();

    /**
     * 国际化缓存
     */
    private static final Map<String, Map<String, String>> CACHE = Maps.newHashMap();

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseI18nMgr.class);
}
