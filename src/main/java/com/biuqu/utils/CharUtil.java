package com.biuqu.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 获取字符的unicode编码对应的实际值
 *
 * @author BiuQu
 * @date 2023/3/24 09:15
 */
public final class CharUtil
{
    /**
     * 把unicode转换成字符串
     *
     * @param ch unicode字符
     * @return 字符对应的字符串显示
     */
    public static String fromUnicode(char ch)
    {
        return "\\u" + Integer.toHexString(ch);
    }

    /**
     * 把字符转换成unicode(只截取第一个字符)
     *
     * @param ch 字符
     * @return 字符对应的unicode
     */
    public static String toUnicode(String ch)
    {
        String result = StringUtils.EMPTY;
        if (!StringUtils.isEmpty(ch))
        {
            result = "\\u" + Integer.toHexString(ch.toCharArray()[0]);
        }
        return result;
    }

    private CharUtil()
    {
    }
}
