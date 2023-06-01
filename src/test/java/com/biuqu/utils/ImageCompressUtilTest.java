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
}