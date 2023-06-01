package com.biuqu.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间转换工具类
 *
 * @author BiuQu
 * @date 2023/1/31 11:40
 */
public final class TimeUtil
{
    /**
     * 获取对应时间格式'yyyy-MM-dd HH:mm:ss'的时间
     *
     * @param currentMills 毫秒数
     * @return 对应时间格式'yyyy-MM-dd HH:mm:ss'的时间
     */
    public static String getDate(long currentMills)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        return sdf.format(new Date(currentMills));
    }

    /**
     * 获取今天今天的0时0分0秒(UTC时间)
     *
     * @return 今天起始的毫秒
     */
    public static long getTodayUtcMills()
    {
        return getTodayUtcMills(TODAY_TIME_FORMAT);
    }

    /**
     * 获取当前指定时间格式的毫秒数
     *
     * @param format 时间格式
     * @return 对应时间格式的毫秒数
     */
    public static long getTodayUtcMills(String format)
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getDefault());
        String now = sdf.format(date);
        try
        {
            Date newDate = sdf.parse(now);
            return newDate.getTime();
        }
        catch (ParseException e)
        {
            LOGGER.error("failed to get time.", e);
        }
        return 0;
    }

    private TimeUtil()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtil.class);

    /**
     * 标准的时间模板
     */
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 标准的时间模板2
     */
    public static final String SIMPLE_TIME_FORMAT = "yy-MM-dd HH:mm:ss.SSS";

    /**
     * 今天的时间模板
     */
    public static final String TODAY_TIME_FORMAT = "yyyy-MM-dd";
}
