package com.biuqu.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ImageCompressUtilTest
{

    @Test
    public void compress() throws IOException
    {
        String path = "pic/1.JPEG";
        byte[] data = FileUtil.read(path);
        Assert.assertTrue(null != data);

        byte[] newData = ImageCompressUtil.compress(data);
        Assert.assertTrue(null != newData);

        String testPath = ImageUtil.class.getResource("/").getPath();
        testPath = new File(testPath).getCanonicalPath() + "/testPic/drj-1.jpeg";
        ImageUtil.write(newData, testPath);
    }

    @Test
    public void compress2() throws IOException
    {
        for (int i = 1; i <= 13; i++)
        {
            String path = "pic/compress" + i + ".jpeg";
            if (i == 7)
            {
                path = "pic/compress" + i + ".png";
            }
            byte[] data = FileUtil.read(path);
            Assert.assertTrue(null != data);
            byte[] newData = ImageCompressUtil.compress(data);
            Assert.assertTrue(null != newData);
            String testPath = ImageUtil.class.getResource("/").getPath();
            testPath = new File(testPath).getCanonicalPath() + "/testPic/test-" + i + ".jpeg";
            ImageUtil.write(newData, testPath);
        }
    }
}