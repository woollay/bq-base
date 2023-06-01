package com.biuqu.utils;

import org.junit.Assert;
import org.junit.Test;

public class IoUtilTest
{
    @Test
    public void test()
    {
        //场景1：读取相对路径下的文件
        String path1 = "file/direnjie.jpeg";
        Assert.assertTrue(null != IoUtil.readInputStream(path1));

        System.out.println("path1:" + (null != IoUtil.readInputStream(path1)));

        //场景2：读取全路径下的文件
        String path2 = getClass().getResource(IoUtil.FILE_SPLIT).getPath() + path1;
        System.out.println("path2:" + path2);

        Assert.assertTrue(null != IoUtil.readInputStream(path2));

        //场景3：读取spring指定的文件
        String path3 = "classpath:file/direnjie.jpeg";
        Assert.assertTrue(null != IoUtil.readInputStream(path3));
    }
}