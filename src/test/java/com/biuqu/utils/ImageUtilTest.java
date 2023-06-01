package com.biuqu.utils;

import com.biuqu.model.FileType;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtilTest
{

    @Test
    public void toBytes() throws IOException
    {
        byte[] data1 = ImageUtil.toBytes(ImageUtil.toBase64("file/direnjie.jpeg"));
        Assert.assertTrue(null != data1);
        System.out.println("data1.size=" + data1.length);

        BufferedImage image2 = ImageIO.read(IoUtil.readInputStream("file/direnjie.jpeg"));
        byte[] data2 = ImageUtil.toBytes(image2);
        Assert.assertTrue(null != data2);
        System.out.println("data2=" + data2.length + "," + ImageUtil.getType(data2) + "/" + FileType.getType(data2));

        byte[] data3 = ImageUtil.toBytes(image2, "jpeg");
        Assert.assertTrue(null != data3);
        System.out.println("data3=" + data3.length + "," + ImageUtil.getType(data3) + "/" + FileType.getType(data3));
    }

    @Test
    public void toBase64()
    {
        String base64_1 = ImageUtil.toBase64("file/direnjie.jpeg");
        System.out.println("base64_1.size=" + base64_1.length());
        Assert.assertTrue(null != base64_1);

        byte[] data = FileUtil.read("file/direnjie.jpeg");
        String base64_2 = ImageUtil.toBase64(data);
        System.out.println("base64_2.size=" + base64_2.length());
        Assert.assertTrue(null != base64_2);
    }

    @Test
    public void write() throws IOException
    {
        String path = ImageUtil.class.getResource("/").getPath();
        path = new File(path).getCanonicalPath() + "/pic/";
        System.out.println("current path=" + path);
        byte[] data1 = ImageUtil.toBytes(ImageUtil.toBase64("file/direnjie.jpeg"));
        Assert.assertTrue(null != data1);
        System.out.println("data1.size=" + data1.length);
        String path1 = path + "1." + ImageUtil.getType(data1);
        ImageUtil.write(data1, path1);
        System.out.println("write data1.size=" + FileUtil.read(path1).length);
        Assert.assertTrue(new File(path1).exists());

        BufferedImage image2 = ImageIO.read(IoUtil.readInputStream("file/direnjie.jpeg"));
        byte[] data2 = ImageUtil.toBytes(image2);
        Assert.assertTrue(null != data2);
        System.out.println("data2=" + data2.length + "," + ImageUtil.getType(data2) + "/" + FileType.getType(data2));
        String path2 = path + "2";
        ImageUtil.write(data2, path2, true);
        System.out.println("write data2.size=" + FileUtil.read(path2 + "." + ImageUtil.getType(data2)).length);
        Assert.assertTrue(new File(path2 + "." + ImageUtil.getType(data2)).exists());

        byte[] data3 = ImageUtil.toBytes(image2, "jpeg");
        Assert.assertTrue(null != data3);
        String path3 = path + "3";
        System.out.println("data3=" + data3.length + "," + ImageUtil.getType(data3) + "/" + FileType.getType(data3));
        ImageUtil.write(data3, path3, true);
        System.out.println("write data3.size=" + FileUtil.read(path3 + "." + ImageUtil.getType(data3)).length);
        Assert.assertTrue(new File(path3 + "." + ImageUtil.getType(data3)).exists());

        FileUtils.forceDelete(new File(path));
    }
}