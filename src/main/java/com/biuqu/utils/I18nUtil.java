package com.biuqu.utils;

import com.biuqu.constants.Const;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 国际化处理的工具类
 *
 * @author BiuQu
 * @date 2023/1/7 18:46
 */
public class I18nUtil
{

    /**
     * 加载国际化文件中的内容
     *
     * @param path   文件路径(注意不能带语言及properties后缀，即不能包含zh_CN.properties)
     * @param locale 语言类型
     */
    public static Map<String, Map<String, String>> loadI18n(String path, Locale locale)
    {
        Map<String, Map<String, String>> i18nMap = Maps.newHashMap();
        if (null == locale || StringUtils.isEmpty(path))
        {
            LOGGER.warn("no language[{}] i18n[{}] to load.", locale, path);
            return i18nMap;
        }

        ResourceBundle bundle = getBundle(path, locale);
        if (null == bundle)
        {
            LOGGER.warn("no i18n[{}/{}] to load.", path, locale);
            return i18nMap;
        }

        Map<String, String> languageMap = Maps.newHashMap();
        for (String key : bundle.keySet())
        {
            languageMap.put(key, bundle.getString(key));
        }

        LOGGER.info("load language[{}] i18n[{}] with size:{}.", locale, path, languageMap.size());
        if (!CollectionUtils.isEmpty(languageMap))
        {
            i18nMap.put(locale.toString(), languageMap);
        }
        return i18nMap;
    }

    /**
     * 获取国际化文件中的内容
     *
     * @param path   文件路径(注意不能带语言及properties后缀)
     * @param locale 本地语言
     * @return 国际化文件内容对象
     */
    private static ResourceBundle getBundle(String path, Locale locale)
    {
        if (StringUtils.isEmpty(path))
        {
            LOGGER.warn("invalid resource bundle:{}", path);
            return null;
        }

        ResourceBundle bundle = null;
        try
        {
            if (!path.startsWith(Const.CLASSPATH))
            {
                bundle = ResourceBundle.getBundle(path, locale);
            }
            else
            {
                String newPath = path + Const.LINK + locale.toString() + Const.PROPERTIES_FILE_SUFFIX;
                bundle = new PropertyResourceBundle(IoUtil.readInputStream(newPath));
            }
        }
        catch (Exception e)
        {
            LOGGER.warn("failed to read properties file[{}] with exception:{}", path, e.getMessage());
        }
        return bundle;
    }

    private I18nUtil()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(I18nUtil.class);
}
