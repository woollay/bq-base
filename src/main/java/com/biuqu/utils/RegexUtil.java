package com.biuqu.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * 替换可使用RegExUtils
 *
 * @author BiuQu
 * @date 2023/1/8 22:59
 */
public final class RegexUtil
{
    /**
     * 正则表达式是否匹配到
     *
     * @param text  原始字符串
     * @param regex 正则表达式
     * @return true表示匹配成功
     */
    public static boolean match(String text, String regex)
    {
        return getMatcher(text, regex).find();
    }

    /**
     * 获取匹配的所有子串
     *
     * @param text  原始字符串
     * @param regex 正则表达式
     * @return 匹配的所有子串
     */
    public static List<String> group(String text, String regex)
    {
        List<String> lists = Lists.newArrayList();
        if (StringUtils.isEmpty(text) || StringUtils.isEmpty(regex))
        {
            return lists;
        }

        Matcher matcher = getMatcher(text, regex);
        int start = 0;
        while (start < text.length() && matcher.find(start))
        {
            int count = matcher.groupCount();
            for (int i = 0; i < count; i++)
            {
                String sub = matcher.group(i);
                lists.add(sub);
            }
            start = matcher.end();
        }
        return lists;
    }

    /**
     * 获取匹配的第一个子串
     *
     * @param text  原始字符串
     * @param regex 正则表达式
     * @return 匹配的第一个子串
     */
    public static String regex(String text, String regex)
    {
        List<String> lists = group(text, regex);
        String sub = null;
        if (!CollectionUtils.isEmpty(lists))
        {
            sub = lists.get(0);
        }
        return sub;
    }

    /**
     * 构建匹配器
     *
     * @param text  原始字符串
     * @param regex 正则表达式
     * @return 匹配器
     */
    private static Matcher getMatcher(String text, String regex)
    {
        return Pattern.compile(regex, Pattern.DOTALL).matcher(text);
    }

    private RegexUtil()
    {
    }
}
