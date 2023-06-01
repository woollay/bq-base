package com.biuqu.utils;

import org.junit.Test;

public class CharUtilTest
{
    @Test
    public void fromUnicode()
    {
        char ch = '\u77be';
        System.out.println(CharUtil.fromUnicode(ch) + "=" + ch);
    }

    @Test
    public void toUnicode()
    {
        String ch = "瞾";
        System.out.println(ch + "=" + CharUtil.toUnicode(ch));
    }
}