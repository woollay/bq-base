package com.biuqu.context;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring上下文的静态获取器
 * <br>
 *
 * @author BiuQu
 * @date: 2019/7/9 16:15
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware
{
    /**
     * 静态获取Spring上下文
     *
     * @return 上下文对象
     */
    public static ApplicationContext getContext()
    {
        return context;
    }

    /**
     * 获取bean对象
     *
     * @param clazz 实例bean的class类型
     * @param <T>   实例bean类型
     * @return 实例bean对象
     */
    public static <T> T getBean(Class<T> clazz)
    {
        if (null == ApplicationContextHolder.context)
        {
            LOGGER.error("Failed to get spring context.");
            return null;
        }

        return ApplicationContextHolder.context.getBean(clazz);
    }

    /**
     * 获取bean对象
     *
     * @param name 实例bean的class类型
     * @param <T>  实例bean类型
     * @return 实例bean对象
     */
    public static <T> T getBean(String name)
    {
        if (null == ApplicationContextHolder.context)
        {
            LOGGER.error("Failed to get spring context.");
            return null;
        }

        if (StringUtils.isEmpty(name) || !containsBean(name))
        {
            LOGGER.error("Failed to get illegal bean.");
            return null;
        }
        return (T)ApplicationContextHolder.context.getBean(name);
    }

    /**
     * 是否包含实例对象
     *
     * @param name 类名
     * @return true表示包含
     */
    public static boolean containsBean(String name)
    {
        if (null != getContext())
        {
            return getContext().containsBean(name);
        }
        return false;
    }

    /**
     * 获取属性值
     *
     * @param name 属性名
     * @return 属性值
     */
    public static String getProperty(String name)
    {
        if (null != getContext() && !StringUtils.isEmpty(name))
        {
            return getContext().getEnvironment().getProperty(name.trim());
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
    {
        ApplicationContextHolder.context = applicationContext;
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextHolder.class);

    /**
     * 上下文对象
     */
    private static ApplicationContext context = null;
}
