package com.biuqu.utils;

import com.biuqu.model.ImageCodeFactor;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ImageCodeUtilTest
{

    @Test
    public void drawImage() throws IOException
    {
        ImageCodeFactor factor = new ImageCodeFactor();
        byte[] data1 = ImageCodeUtil.drawImage(factor);
        Assert.assertTrue(null != data1);
        System.out.println("text1=" + factor.getText() + ",base64-1=" + ImageUtil.toBase64(data1));

        String path = getClass().getResource("/").getPath();
        path = new File(path).getCanonicalPath() + "/code/";
        System.out.println("current path1=" + path);

        ImageUtil.write(data1, path + UUID.randomUUID(), true);

        byte[] data2 = ImageCodeUtil.drawImage(factor, "jpeg");
        Assert.assertTrue(null != data2);
        System.out.println("text2=" + factor.getText() + ",base64-2=" + ImageUtil.toBase64(data2));

        ImageUtil.write(data2, path + UUID.randomUUID(), true);
    }
}