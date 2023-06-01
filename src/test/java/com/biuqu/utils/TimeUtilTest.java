package com.biuqu.utils;

import org.junit.Test;

public class TimeUtilTest
{

    @Test
    public void getDate()
    {
    }

    @Test
    public void getTodayUtcMills()
    {
        long cur = TimeUtil.getTodayUtcMills();
        System.out.println("today's first mills:" + cur + ",and date is:" + TimeUtil.getDate(cur));
    }

    @Test
    public void testGetTodayUtcMills()
    {
    }
}