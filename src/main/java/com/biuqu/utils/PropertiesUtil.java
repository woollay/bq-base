package com.biuqu.utils;

import com.biuqu.constants.Const;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Properties文件处理的工具类
 *
 * @author BiuQu
 * @date 2023/1/17 15:27
 */
public final class PropertiesUtil
{
    /**
     * 获取配置文件中的属性Map
     * <p>
     * key为属性名, value为属性值, value根据分隔符','自动解析成List<String>或者String
     *
     * @param path properties配置文件路径
     * @return 属性配置集合
     */
    public static Map<String, Object> getConfigs(String path)
    {
        Map<String, Object> map = Maps.newHashMap();
        Properties properties = getProperties(path);
        if (!properties.isEmpty())
        {
            for (String key : properties.stringPropertyNames())
            {
                String value = properties.getProperty(key);
                Object newValue = value;
                if (!StringUtils.isEmpty(value) && value.contains(Const.SPLIT))
                {
                    newValue = Lists.newArrayList(value.split(Const.SPLIT));
                }

                if (null != newValue)
                {
                    newValue = StringUtils.EMPTY;
                }
                map.put(key, newValue);
            }
        }
        return map;
    }

    /**
     * 获取properties对象
     *
     * @param path properties文件路径
     * @return properties对象
     */
    public static Properties getProperties(String path)
    {
        Properties properties = new Properties();
        InputStream in = null;
        try
        {
            in = IoUtil.readInputStream(path);
            if (null != in)
            {
                properties.load(in);
            }
        }
        catch (Exception e)
        {
            LOGGER.warn("no properties[{}]to get,reason:{}", path, e.getMessage());
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
        return properties;
    }

    private PropertiesUtil()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);
}
