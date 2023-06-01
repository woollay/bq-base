package com.biuqu.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RegexUtilTest
{

    @Test
    public void match()
    {
        System.out.println("find=" + RegexUtil.match(TEXT, REGEX));
        Assert.assertTrue(RegexUtil.match(TEXT, REGEX));
    }

    @Test
    public void group()
    {
        List<String> groups = RegexUtil.group(TEXT, REGEX);
        System.out.println(JsonUtil.toJson(groups));
        Assert.assertTrue(groups.size() == 2);
    }

    @Test
    public void regex()
    {
        System.out.println("regex=" + RegexUtil.regex(TEXT, REGEX));
        Assert.assertTrue("abc".equals(RegexUtil.regex(TEXT, REGEX)));
    }

    private static final String TEXT = "@annotation(exp=\"${abc}\",value=\"${bcd}\")";
    //非贪婪匹配'(.*?)'中的'?'
    private static final String REGEX = "(?<=\\$\\{)(.*?)(?=\\})";
}