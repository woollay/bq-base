package com.biuqu.i18n;

import com.biuqu.utils.I18nUtil;
import com.google.common.collect.Maps;

import java.util.Locale;
import java.util.Map;

/**
 * 错误码国际化管理器
 *
 * @author BiuQu
 * @date 2023/1/7 19:45
 */
public class ErrCodeI18nMgr extends BaseI18nMgr
{
    /**
     * 获取国际化单例
     *
     * @return 单例对象
     */
    public static ErrCodeI18nMgr getInstance()
    {
        return INSTANCE;
    }

    /**
     * 获取外部错误码的国际化配置(默认获取中文)
     *
     * @param key 国际化key
     * @return 国际化key对应的值
     */
    public String getOut(String key)
    {
        return this.get(OUT_CACHE, key);
    }

    /**
     * 获取内部错误码的国际化配置(默认获取中文)
     *
     * @param key 国际化key
     * @return 国际化key对应的值
     */
    public String getIn(String key)
    {
        return this.get(IN_CACHE, key);
    }

    /**
     * 获取参数校验错误码的国际化配置(默认获取中文)
     *
     * @param key 国际化key
     * @return 国际化key对应的值
     */
    public String getValid(String key)
    {
        return this.get(VALID_CACHE, key);
    }

    /**
     * 获取外部错误码的国际化配置
     *
     * @param locale 语言
     * @param key    国际化key
     * @return 国际化key对应的值
     */
    public String getOut(Locale locale, String key)
    {
        return this.get(OUT_CACHE, locale, key);
    }

    /**
     * 获取内部错误码的国际化配置
     *
     * @param locale 语言
     * @param key    国际化key
     * @return 国际化key对应的值
     */
    public String getIn(Locale locale, String key)
    {
        return this.get(IN_CACHE, locale, key);
    }

    /**
     * 获取参数校验错误码的国际化配置
     *
     * @param locale 语言
     * @param key    国际化key
     * @return 国际化key对应的值
     */
    public String getValid(Locale locale, String key)
    {
        return this.get(VALID_CACHE, locale, key);
    }

    /**
     * 获取外部错误码的指定语言的所有国际化配置(默认获取中文)
     *
     * @return 所有配置的国际化
     */
    public Map<String, String> getI18nOut()
    {
        return this.getI18n(OUT_CACHE);
    }

    /**
     * 获取内部错误码的指定语言的所有国际化配置(默认获取中文)
     *
     * @return 所有配置的国际化
     */
    public Map<String, String> getI18nIn()
    {
        return this.getI18n(IN_CACHE);
    }

    /**
     * 获取参数校验错误码的指定语言的所有国际化配置(默认获取中文)
     *
     * @return 所有配置的国际化
     */
    public Map<String, String> getI18nValid()
    {
        return this.getI18n(VALID_CACHE);
    }

    /**
     * 获取外部错误码的指定语言的所有国际化配置
     *
     * @param locale 语言类型
     * @return 所有配置的国际化
     */
    public Map<String, String> getI18nOut(Locale locale)
    {
        return this.getI18n(OUT_CACHE, locale);
    }

    /**
     * 获取内部错误码的指定语言的所有国际化配置
     *
     * @param locale 语言类型
     * @return 所有配置的国际化
     */
    public Map<String, String> getI18nIn(Locale locale)
    {
        return this.getI18n(IN_CACHE, locale);
    }

    /**
     * 获取参数校验错误码的指定语言的所有国际化配置
     *
     * @param locale 语言类型
     * @return 所有配置的国际化
     */
    public Map<String, String> getI18nValid(Locale locale)
    {
        return this.getI18n(VALID_CACHE, locale);
    }

    /**
     * 获取外部错误码的所有语言的所有国际化配置
     *
     * @return 所有配置的国际化
     */
    public Map<String, Map<String, String>> getI18nsOut()
    {
        return this.getI18ns(OUT_CACHE);
    }

    /**
     * 获取内部错误码的所有语言的所有国际化配置
     *
     * @return 所有配置的国际化
     */
    public Map<String, Map<String, String>> getI18nsIn()
    {
        return this.getI18ns(IN_CACHE);
    }

    /**
     * 获取参数校验错误码的所有语言的所有国际化配置
     *
     * @return 所有配置的国际化
     */
    public Map<String, Map<String, String>> getI18nsValid()
    {
        return this.getI18ns(VALID_CACHE);
    }

    @Override
    public void clear()
    {
        super.clear();
        this.clear(OUT_CACHE);
        this.clear(IN_CACHE);
    }

    /**
     * 加载外部错误码的国际化文件中的内容(读取默认路径下的文件:classpath:i18n/out_error_code)
     *
     * @param locale 语言类型
     */
    public void loadI18nOut(Locale locale)
    {
        this.loadI18n(OUT_CACHE, locale);
    }

    /**
     * 加载内部错误码的国际化文件中的内容(读取默认路径下的文件:classpath:i18n/in_error_code)
     *
     * @param locale 语言类型
     */
    public void loadI18nIn(Locale locale)
    {
        this.loadI18n(IN_CACHE, locale);
    }

    /**
     * 加载参数校验错误码的国际化文件中的内容(读取默认路径下的文件:classpath:i18n/in_error_code)
     *
     * @param locale 语言类型
     */
    public void loadI18nValid(Locale locale)
    {
        this.loadI18n(VALID_CACHE, locale);
    }

    /**
     * 加载外部错误码的国际化文件中的内容
     *
     * @param path   文件路径(注意不能带语言及properties后缀，即不能包含zh_CN.properties)
     * @param locale 语言类型
     */
    public void loadI18nOut(String path, Locale locale)
    {
        this.loadI18n(OUT_CACHE, path, locale);
    }

    /**
     * 加载内部错误码的国际化文件中的内容
     *
     * @param path   文件路径(注意不能带语言及properties后缀，即不能包含zh_CN.properties)
     * @param locale 语言类型
     */
    public void loadI18nIn(String path, Locale locale)
    {
        this.loadI18n(IN_CACHE, path, locale);
    }

    /**
     * 加载参数校验错误码的国际化文件中的内容
     *
     * @param path   文件路径(注意不能带语言及properties后缀，即不能包含zh_CN.properties)
     * @param locale 语言类型
     */
    public void loadI18nValid(String path, Locale locale)
    {
        this.loadI18n(VALID_CACHE, path, locale);
    }

    @Override
    protected String getI18nPath()
    {
        return ERROR_CODE_PATH;
    }

    private ErrCodeI18nMgr()
    {
    }

    /**
     * 单例
     */
    private static final ErrCodeI18nMgr INSTANCE = new ErrCodeI18nMgr();

    /**
     * 标准错误码的文件路径
     */
    private static final String ERROR_CODE_PATH = "errcode/errcode";

    /**
     * 业务调用第三方时的错误码映射文件路径
     */
    private static final String IN_ERROR_CODE_PATH = "errcode/errcode_inner";

    /**
     * 业务返回时，兼容老接口时的错误码映射文件路径
     */
    private static final String OUT_ERROR_CODE_PATH = "errcode/errcode_outer";

    /**
     * 参数校验错误码文件路径
     */
    private static final String VALID_ERROR_CODE_PATH = "errcode/errcode_valid";

    /**
     * 内部错误码缓存
     */
    private static final Map<String, Map<String, String>> IN_CACHE = Maps.newHashMap();

    /**
     * 外部错误码缓存
     */
    private static final Map<String, Map<String, String>> OUT_CACHE = Maps.newHashMap();

    /**
     * 校验错误码缓存
     */
    private static final Map<String, Map<String, String>> VALID_CACHE = Maps.newHashMap();

    static
    {
        //1.默认加载标准错误码的2种语言的国际化
        INSTANCE.loadI18n(Locale.US);
        INSTANCE.loadI18n(Locale.SIMPLIFIED_CHINESE);

        //2.加载内部错误码国际化
        IN_CACHE.putAll(I18nUtil.loadI18n(IN_ERROR_CODE_PATH, Locale.US));
        IN_CACHE.putAll(I18nUtil.loadI18n(IN_ERROR_CODE_PATH, Locale.SIMPLIFIED_CHINESE));

        //3.加载外部错误码国际化
        OUT_CACHE.putAll(I18nUtil.loadI18n(OUT_ERROR_CODE_PATH, Locale.US));
        OUT_CACHE.putAll(I18nUtil.loadI18n(OUT_ERROR_CODE_PATH, Locale.SIMPLIFIED_CHINESE));

        //4.加载参数校验错误码国际化
        VALID_CACHE.putAll(I18nUtil.loadI18n(VALID_ERROR_CODE_PATH, Locale.US));
        VALID_CACHE.putAll(I18nUtil.loadI18n(VALID_ERROR_CODE_PATH, Locale.SIMPLIFIED_CHINESE));
    }
}
