package com.biuqu.utils;

import com.biuqu.constants.Const;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
    public static String fromUnicode(char[] ch)
    {
        StringBuilder result = new StringBuilder(StringUtils.EMPTY);
        for (char c : ch)
        {
            result.append(fromUnicode(c));
        }
        return result.toString();
    }

    /**
     * 把unicode转换成字符串
     *
     * @param ch unicode字符
     * @return 字符对应的字符串显示
     */
    public static String fromUnicode(char ch)
    {
        return StringUtils.EMPTY + ch;
    }

    /**
     * 获取范围内的连续字符集合
     *
     * @param start 起始字符位置
     * @param end   结束字符位置
     * @return 字符对应的文本
     */
    public static List<String> fromUnicode(char[] start, char[] end)
    {
        List<String> lists = Lists.newArrayList();

        char minChar = start[start.length - Const.ONE];
        char maxChar = end[end.length - Const.ONE];
        boolean multiUnicode = (start.length > Const.ONE);
        for (int index = minChar; index <= maxChar; index++)
        {
            char[] chars = {(char)index};
            if (multiUnicode)
            {
                chars = new char[] {start[0], (char)index};
            }
            String ch = CharUtil.fromUnicode(chars);
            lists.add(ch);
        }
        return lists;
    }

    /**
     * 把字符转换成unicode
     *
     * @param ch 字符
     * @return 字符对应的unicode
     */
    public static String toUnicode(String ch)
    {
        StringBuilder result = new StringBuilder(StringUtils.EMPTY);
        if (!StringUtils.isEmpty(ch))
        {
            char[] chars = ch.toCharArray();
            for (char c : chars)
            {
                String hex = Integer.toHexString(c);
                int fillLen = CHAR_HEX_LEN - hex.length();
                if (fillLen > 0)
                {
                    hex = StringUtils.repeat(StringUtils.EMPTY + 0, fillLen) + hex;
                }
                result.append(UNICODE_TAG).append(hex);
            }
        }
        return result.toString();
    }

    /**
     * 是否是中文
     *
     * @param ch 中文字符集
     * @return true表示是中文
     */
    public static boolean isChinese(String ch)
    {
        return RegexUtil.match(ch, CHINESE_REGEX);
    }

    private CharUtil()
    {
    }

    /**
     * 一个字符对应的16进制位个数
     */
    private static final int CHAR_HEX_LEN = 4;

    /**
     * UNICODE起始标记
     */
    private static final String UNICODE_TAG = "\\u";

    /**
     * 中文名字等字符串匹配表达式
     */
    private static final String CHINESE_REGEX =
        "^([\\u3400-\\u4dbf\\u4e00-\\u9fff\\uf900-\\ufaff\\x{20000}-\\x{323af}\\u2180-\\u2ef3\\u2f00-\\u2fd5\\u2ff0-\\u2ffb\\u3105-\\u312f\\u31a0-\\u31bf\\u31c0-\\u31e3\\u3007·])+$";
}
