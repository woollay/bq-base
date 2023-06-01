package com.biuqu.utils;

import com.biuqu.exception.CommonException;
import org.junit.Assert;
import org.junit.Test;

public class RandomUtilTest
{
    @Test
    public void genLen()
    {
        Assert.assertTrue(null != RandomUtil.create());

        String num1 = RandomUtil.genLen(10);
        System.out.println("num1=" + num1);
        Assert.assertTrue(num1.length() == 10);

        int num2 = RandomUtil.genLenNum(9);
        System.out.println("num2=" + num2);
        Assert.assertTrue(String.valueOf(num2).length() == 9);
        Assert.assertTrue(num2 >= 100000000 && num2 <= 999999999);

        String num3 = RandomUtil.genLen(10, null, false);
        System.out.println("num3=" + num3);
        Assert.assertTrue(num3.length() == 10);

        int[] excludes = {8, 3};

        try
        {
            System.out.println("num4=" + RandomUtil.genLen(9, excludes, false));
        }
        catch (Exception e)
        {
            Assert.assertTrue(e instanceof CommonException);
        }

        String num5 = RandomUtil.genLen(8, excludes, false);
        System.out.println("num5=" + num5);
        Assert.assertTrue(num5.length() == 8);

        String num6 = RandomUtil.genLen(8, excludes, true);
        System.out.println("num6=" + num6);
        Assert.assertTrue(num6.length() == 8);

        int[] excludes7 = {5, 1, 9, 6, 4};
        int num7 = RandomUtil.genLenNum(5, excludes7, false);
        System.out.println("num7=" + num7);
        Assert.assertTrue(num7 > 10000 && num7 < 99999);
    }
}