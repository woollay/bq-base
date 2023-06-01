package com.biuqu.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ByteUtilTest
{

    @Test
    public void test()
    {
        byte[] bytes1 = ByteUtil.from('a');
        System.out.println("bytes1=" + Arrays.toString(bytes1));
        Assert.assertTrue(bytes1.length == 4);
        int num1 = ByteUtil.to(bytes1);
        System.out.println("num1=" + num1);
        Assert.assertTrue(num1 == 97);

        byte[] bytes2 = ByteUtil.from(999999);
        System.out.println("bytes2=" + Arrays.toString(bytes2));
        Assert.assertTrue(bytes2.length == 4);
        int num2 = ByteUtil.to(bytes2);
        System.out.println("num2=" + num2);
        Assert.assertTrue(num2 == 999999);

        byte[] bytes3 = ByteUtil.fromRandom(20);
        System.out.println("bytes3=" + Arrays.toString(bytes3));
        Assert.assertTrue(bytes3.length == 20);
        ByteUtil.clear(bytes3);
        int num3 = ByteUtil.to(bytes3);
        Assert.assertTrue(num3 == 0);

        char[] chars = {'a', 'b', 'c', 'f'};
        byte[] bytes4 = ByteUtil.from(chars);
        System.out.println("bytes4=" + Arrays.toString(bytes4));
        Assert.assertTrue(bytes4.length == 4);
    }
}