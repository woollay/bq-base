package com.biuqu.utils;

import com.biuqu.model.FileType;
import com.biuqu.model.ImageFactor;
import com.biuqu.model.ImagePixel;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.resizers.Resizers;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片压缩工具类
 *
 * @author BiuQu
 * @date 2023/1/10 09:42
 */
public final class ImageCompressUtil
{
    /**
     * 基于图片二进制压缩(此仅为一种压缩场景，以此来理解压缩)：
     * 1.先固定图片分辨率(固定为640*480，也算一种压缩)
     * 2.再压缩图片文件大小为20k-30k(主要控制文件的quality系数和scale系数，同时改变)
     *
     * @param data 图片二进制
     * @return 压缩后的图片二进制
     */
    public static byte[] compress(byte[] data)
    {
        if (null == data)
        {
            LOGGER.info("invalid compress image data.");
            return null;
        }
        ImageFactor factor = new ImageFactor(data.length);
        return compress(data, factor);
    }

    /**
     * 基于图片大小压缩
     *
     * @param data   图片二进制
     * @param factor 压缩因子
     * @return 压缩后的图片二进制
     */
    public static byte[] compress(byte[] data, ImageFactor factor)
    {
        if (null == data)
        {
            LOGGER.info("invalid compress image data.");
            return null;
        }

        long start = System.currentTimeMillis();
        int size = data.length;
        int newSize = size;
        try
        {
            ImagePixel pixel = ImageUtil.getImagePixel(data);
            if (null == pixel)
            {
                LOGGER.info("no compress parameter.");
                return data;
            }
            byte[] newData = mixCompress(data, pixel, factor);
            if (null != newData && newData.length > factor.getMaxSize())
            {
                ImageFactor nextFactor = factor.next(newData.length);
                byte[] multiData = multiFactorCompress(newData, nextFactor);
                newData = getBestData(newData, multiData);
            }
            if (null != newData)
            {
                newSize = newData.length;
            }
            return newData;
        }
        finally
        {
            LOGGER.info("compress[{}->{}] totally cost:{}", size, newSize, (System.currentTimeMillis() - start));
        }
    }

    /**
     * 多因子压缩
     * 基于图片文件大小去压缩图片的质量和图片的像素大小
     *
     * @param data   图片二进制
     * @param factor 图片压缩因子
     * @return 压缩后的图片二进制
     */
    public static byte[] multiFactorCompress(byte[] data, ImageFactor factor)
    {
        while (factor.canCompress())
        {
            byte[] newData = factorCompress(data, factor);
            if (null == newData)
            {
                break;
            }
            data = newData;

            ImageFactor next = factor.next(newData.length);
            if (factor == next)
            {
                break;
            }
            factor = next;
        }
        return data;
    }

    /**
     * 多因子压缩
     * 基于图片文件大小去压缩图片的质量和图片的像素大小
     *
     * @param data   图片二进制
     * @param factor 图片压缩因子
     * @return 压缩后的图片二进制
     */
    public static byte[] factorCompress(byte[] data, ImageFactor factor)
    {
        long start = System.currentTimeMillis();
        if (null == data)
        {
            return null;
        }
        int size = data.length;
        int newSize = size;
        float quality = factor.toQualityRate();
        float scale = factor.toScaleRate();

        InputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(in).outputFormat(FileType.JPEG.name());
        try
        {

            builder.outputQuality(quality).scale(scale).toOutputStream(out);
            byte[] newData = out.toByteArray();
            newSize = newData.length;
            return newData;
        }
        catch (IOException e)
        {
            LOGGER.error("failed to compress by quality or scale.", e);
        }
        finally
        {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
            long cost = System.currentTimeMillis() - start;
            LOGGER.info("compress factor:{}/{},result:{}->{} bytes,cost:{} ms.", quality, scale, size, newSize, cost);
        }
        return null;
    }

    /**
     * 基于像素(宽和高)去压缩图片(存在等比例拉升/缩窄的可能)
     *
     * @param data  图片二进制对象
     * @param pixel 图片新的像素
     * @return 压缩后的新图片二进制对象
     */
    public static byte[] pixelCompress(byte[] data, ImagePixel pixel)
    {
        return pixelCompress(data, pixel, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * 基于像素(宽和高)去压缩图片
     *
     * @param data     图片二进制对象
     * @param pixel    图片新的像素
     * @param colorTye 色彩类型
     * @return 压缩后的新图片二进制对象
     */
    public static byte[] pixelCompress(byte[] data, ImagePixel pixel, int colorTye)
    {
        if (null == data || null == pixel)
        {
            LOGGER.error("invalid pixel compress parameter.");
            return null;
        }

        long start = System.currentTimeMillis();
        InputStream in = null;
        try
        {
            in = new ByteArrayInputStream(data);
            BufferedImage image = ImageIO.read(in);

            int width = image.getWidth();
            int height = image.getHeight();
            LOGGER.info("pixel from [{},{}] to [{},{}].", width, height, pixel.getWidth(), pixel.getHeight());
            BufferedImage newImage = resize(image, pixel, colorTye);
            byte[] newData = ImageUtil.toBytes(newImage);
            LOGGER.info("resize image from {} to {}.", data.length, newData.length);
            return newData;
        }
        catch (IOException e)
        {
            LOGGER.error("failed to resize image.", e);
        }
        finally
        {
            IOUtils.closeQuietly(in);
            LOGGER.info("resize compress cost:{}", System.currentTimeMillis() - start);
        }
        return null;
    }

    /**
     * 重置图片的像素
     *
     * @param image 图片对象
     * @param pixel 图片的像素
     * @param type  图片对象指定的色彩类型，比如BufferedImage.TYPE_INT_RGB表示基于RGB三原色
     * @return 新的图片对象
     */
    public static BufferedImage resize(BufferedImage image, ImagePixel pixel, int type)
    {
        long start = System.currentTimeMillis();
        BufferedImage newImage = new BufferedImageBuilder(pixel.getWidth(), pixel.getHeight(), type).build();
        Resizers.PROGRESSIVE.resize(image, newImage);
        LOGGER.info("resize image cost:{}", (System.currentTimeMillis() - start));
        return newImage;
    }

    /**
     * 综合使用图片像素和质量系数各压缩1次
     * <p>
     * 1.优先使用标准的RGB3原色压缩一轮图片像素大小，如果大小大于限定大小，则做一轮质量压缩(质量系数0.55)[个人经验做法]
     * 2.如果质量压缩后，图片急剧变小，则重新使用原图片的色彩类型重新按照上述第1条再次压缩一遍；
     * 3.选取最佳的图片大小二进制：压缩后如果还需要继续压缩，则选取压缩后的图片二进制，否则选择压缩前的图片二进制；
     *
     * @param data   图片二进制
     * @param pixel  图片原始像素
     * @param factor 图片压缩多因子参数
     * @return 综合压缩后的新图片二进制
     */
    private static byte[] mixCompress(byte[] data, ImagePixel pixel, ImageFactor factor)
    {
        //1.先基于像素大小压缩一轮(使用标准的色彩类型，有可能导致图片严重失真)
        byte[] pixelData1 = pixelCompress(data, pixel.compress());
        //图片的限定大小
        int maxSize = factor.getMaxSize();
        if (needCompress(pixelData1, maxSize))
        {
            //2.再基于图片的大小压缩一轮图片质量和scale(scale对应的就是图片像素大小系数)
            byte[] factorData1 = factorCompress(pixelData1, factor);
            if (null != factorData1 && factorData1.length < maxSize)
            {
                //图片文件大小急剧变小(图片严重失真)
                if (!ImageFactor.validSize(pixelData1.length, factorData1.length))
                {
                    //3.如果第一轮像素大小压缩后导致图片文件大小急剧变小(图片严重失真)，重新基于原图的色彩类型再来压缩一次
                    byte[] pixelData2 = pixelCompress(data, pixel.compress(), pixel.getColorType());
                    if (needCompress(pixelData2, maxSize))
                    {
                        byte[] factorData2 = factorCompress(pixelData2, factor);
                        return getBestData(pixelData2, factorData2);
                    }
                    return getBestData(data, pixelData2);
                }
            }
            return getBestData(pixelData1, factorData1);
        }

        return getBestData(data, pixelData1);
    }

    /**
     * 是否需要压缩
     *
     * @param data 压缩后的图片二进制
     * @param size 图片文件最大的大小限制
     * @return true表示需要压缩
     */
    private static boolean needCompress(byte[] data, int size)
    {
        return null != data && data.length > size;
    }

    /**
     * 获取最佳图片数组大小
     *
     * @param data    压缩前的二进制
     * @param newData 压缩后的二进制
     * @return 最佳的二进制
     */
    private static byte[] getBestData(byte[] data, byte[] newData)
    {
        if (null != newData)
        {
            return newData;
        }
        return data;
    }

    private ImageCompressUtil()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageCompressUtil.class);
}
