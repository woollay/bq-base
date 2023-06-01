package com.biuqu.utils;

import com.biuqu.constants.Const;

import java.math.BigDecimal;

/**
 * 数学计算工具类
 *
 * @author BiuQu
 * @date 2023/1/12 20:49
 */
public class MathUtil
{
    /**
     * 计算平均数
     *
     * @param a 数字1
     * @param b 数字2
     * @return 平均数
     */
    public static int avg(int a, int b)
    {
        return (a + b) / Const.TWO;
    }

    /**
     * 把百分制转换成百分比
     *
     * @param score 百分制分数
     * @return 百分比
     */
    public static float toRate(int score)
    {
        BigDecimal rate = new BigDecimal((float)score / MAX_NUM);
        return rate.setScale(Const.TWO, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 把百分制转换成百分比
     *
     * @param score 百分制分数
     * @param size  最大大小
     * @return 百分比
     */
    public static float toRate(int score, int size)
    {
        BigDecimal rate = new BigDecimal((float)score / size);
        return rate.setScale(Const.TWO, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 获取阈值(取不到就给默认值)
     *
     * @param value      配置值
     * @param defaultVal 默认值
     * @return 配置的阈值
     */
    public static long getLong(String value, long defaultVal)
    {
        long threshold = defaultVal;
        try
        {
            threshold = Long.parseLong(value);
        }
        catch (NumberFormatException e)
        {
        }
        return threshold;
    }

    private MathUtil()
    {
    }

    /**
     * 最大质量系数(默认值)
     */
    private static final int MAX_NUM = 100;
}
