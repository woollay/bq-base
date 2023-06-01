package com.biuqu.utils;

import com.biuqu.constants.Const;
import com.biuqu.errcode.ErrCodeMgr;
import com.biuqu.exception.CommonException;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * 随机数工具类
 *
 * @author BiuQu
 * @date 2023/1/8 16:22
 */
public final class RandomUtil
{
    /**
     * 创建随机数对象
     *
     * @return 安全的随机数对象
     */
    public static SecureRandom create()
    {
        try
        {
            SecureRandom random = SecureRandom.getInstance(RANDOM_MODE);
            random.setSeed(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
            return random;
        }
        catch (Exception e)
        {
            LOGGER.error("failed to create random.", e);
            throw new CommonException(ErrCodeMgr.getServerErr().getCode());
        }
    }

    /**
     * 生成指定长度的正整数字符串
     *
     * @param len 字符串长度
     * @return 指定长度的正整数字符串
     */
    public static String genLen(int len)
    {
        SecureRandom random = create();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++)
        {
            builder.append(next(random, 0, Const.TEN));
        }
        return builder.toString();
    }

    /**
     * 生成指定长度的数字
     *
     * @param len 数字的长度
     * @return 指定长度的数字
     */
    public static int genLenNum(int len)
    {
        return genLenNum(len, null, true);
    }

    /**
     * 生成指定长度的数字字符串
     *
     * @param len      字符串长度
     * @param excludes 不包含的数字
     * @param repeat   true表示可以重复
     * @return 指定长度的数字字符串
     */
    public static int genLenNum(int len, int[] excludes, boolean repeat)
    {
        return Integer.parseInt(genLen(len, excludes, repeat));
    }

    /**
     * 生成指定长度的数字字符串
     *
     * @param len      字符串长度
     * @param excludes 不包含的数字
     * @param repeat   true表示可以重复
     * @return 指定长度的数字字符串
     */
    public static String genLen(int len, int[] excludes, boolean repeat)
    {
        int maxLen = len;
        if (null != excludes)
        {
            maxLen += excludes.length;
        }
        if (maxLen > Const.TEN)
        {
            LOGGER.error("beyond random size[{},{}].", maxLen, len);
            throw new CommonException(ErrCodeMgr.getServerErr().getCode());
        }

        StringBuilder builder = new StringBuilder();
        SecureRandom random = create();
        int num = next(random, Const.ONE, Const.TEN);
        while (ArrayUtils.contains(excludes, num))
        {
            num = next(random, Const.ONE, Const.TEN);
        }
        builder.append(num);

        for (int i = Const.ONE; i < len; i++)
        {
            int nextNum = next(random, 0, Const.TEN);
            boolean needNext = !repeat && builder.toString().contains(String.valueOf(nextNum));
            while (needNext || ArrayUtils.contains(excludes, nextNum))
            {
                nextNum = next(random, 0, Const.TEN);
                needNext = !repeat && builder.toString().contains(String.valueOf(nextNum));
            }
            builder.append(nextNum);
        }
        return builder.toString();
    }

    /**
     * 随机产生一个整数
     *
     * @param includeMin 最小值(包含)
     * @param excludeMax 最大值(不包含)
     * @return 取值范围内的整数
     */
    public static int next(int includeMin, int excludeMax)
    {
        SecureRandom random = create();
        return next(random, includeMin, excludeMax);
    }

    /**
     * 随机产生一个整数
     *
     * @param random     随机数对象
     * @param includeMin 最小值(包含)
     * @param excludeMax 最大值(不包含)
     * @return 取值范围内的整数
     */
    private static int next(SecureRandom random, int includeMin, int excludeMax)
    {
        if (includeMin >= excludeMax)
        {
            LOGGER.error("invalid random scope[{},{}].", includeMin, excludeMax);
            throw new CommonException(ErrCodeMgr.getServerErr().getCode());
        }
        int next = random.nextInt(excludeMax);
        while (next < includeMin)
        {
            next = random.nextInt(excludeMax);
        }
        return next;
    }

    private RandomUtil()
    {
    }

    /**
     * 随机数填充模式
     */
    private static final String RANDOM_MODE = "SHA1PRNG";

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomUtil.class);
}
