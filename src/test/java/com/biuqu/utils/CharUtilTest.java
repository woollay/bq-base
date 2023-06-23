package com.biuqu.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CharUtilTest
{
    @Test
    public void fromUnicode()
    {
        char ch = '\u77be';
        System.out.println(CharUtil.fromUnicode(ch) + "=" + CharUtil.toUnicode("" + ch));
    }

    @Test
    public void toUnicode()
    {
        String ch = "瞾";
        String format = "%s[%s]=%s=>%s";
        String unicode = CharUtil.fromUnicode(ch.toCharArray());
        System.out.println(String.format(format, ch, ch.toCharArray().length, CharUtil.toUnicode(ch), unicode));

        String ch2 = "\uD84C\uDC7E";
        String unicode2 = CharUtil.fromUnicode(ch2.toCharArray());
        System.out.println(String.format(format, ch2, ch2.toCharArray().length, CharUtil.toUnicode(ch2), unicode2));
    }

    @Test
    public void toUnicode2()
    {
        //        char[] starts1 = {'\u4e00'};
        //        char[] ends1 = {'\u9fa5'};
        //        toUnicode(starts1, ends1);

        char[] starts2 = {'\u9FA6'};
        char[] ends2 = {'\u9FFF'};
        toUnicode(starts2, ends2);

        //        char[] starts4 = {'\u0002', '\u0000'};
        //        char[] ends4 = {'\u0002', '\uA6DF'};
        //        toUnicode(starts4, ends4);
    }

    @Test
    public void isChinese()
    {
        String ch1 = "\uD84C\uDC7E";
        Assert.assertTrue(CharUtil.isChinese(ch1));

        String ch2 = "\u1000";
        Assert.assertTrue(!CharUtil.isChinese(ch2));

        String ch3 = "\u77be";
        Assert.assertTrue(CharUtil.isChinese(ch3));

        String ch4 = "\u77be\u1000";
        Assert.assertTrue(!CharUtil.isChinese(ch4));

        String ch5 = "\u77be\uD84C\uDC7E";
        Assert.assertTrue(CharUtil.isChinese(ch5));

        String ch6 = "\u1000\u77be\uD84C\uDC7E";
        Assert.assertTrue(!CharUtil.isChinese(ch6));

        String ch7 = "\u77be\u1000\uD84C\uDC7E";
        Assert.assertTrue(!CharUtil.isChinese(ch7));
    }

    private void toUnicode(char[] starts, char[] ends)
    {
        List<String> chars = CharUtil.fromUnicode(starts, ends);
        int i = 0;
        for (String ch : chars)
        {
            System.out.println(String.format("[%s]%s=%s", i, ch, CharUtil.toUnicode(ch)));
            i++;
        }
        System.out.println("******************************************total=" + chars.size());
    }
}