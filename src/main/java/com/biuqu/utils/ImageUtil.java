package com.biuqu.utils;

import com.biuqu.constants.Const;
import com.biuqu.model.FileType;
import com.biuqu.model.ImagePixel;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Iterator;
import java.util.Locale;

/**
 * 图片工具类
 *
 * @author BiuQu
 * @date 2023/1/10 09:40
 */
public final class ImageUtil
{
    /**
     * 获取图片的像素(宽和高)
     *
     * @param data 二进制图片数据
     * @return 图片的原始像素对象
     */
    public static ImagePixel getImagePixel(byte[] data)
    {
        InputStream in = null;
        try
        {
            in = new ByteArrayInputStream(data);
            BufferedImage image = ImageIO.read(in);

            ImagePixel pixel = ImagePixel.build(image.getWidth(), image.getHeight(), image.getType());
            LOGGER.info("image[{}] pixel is :{}.", ImageUtil.getType(data), JsonUtil.toJson(pixel));
            return pixel;
        }
        catch (IOException e)
        {
            LOGGER.error("failed to get image size.", e);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
        return null;
    }

    /**
     * 把图片对象转换成二进制(默认为png格式)
     *
     * @param image 图片对象
     * @return 图片二进制对象
     */
    public static byte[] toBytes(BufferedImage image)
    {
        return toBytes(image, FileType.PNG.name().toLowerCase(Locale.US));
    }

    /**
     * 把图片对象转换成二进制
     *
     * @param image 图片对象
     * @param type  图片类型
     * @return 图片二进制对象
     */
    public static byte[] toBytes(BufferedImage image, String type)
    {
        if (null == image || StringUtils.isEmpty(type))
        {
            LOGGER.error("invalid image data.");
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            ImageIO.write(image, type, out);
            return out.toByteArray();
        }
        catch (IOException e)
        {
            LOGGER.error("failed to get byte[] from image.", e);
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
        return null;
    }

    /**
     * 从bas464中转换出图片二进制数据
     *
     * @param base64 图片base64
     * @return 图片二进制数据
     */
    public static byte[] toBytes(String base64)
    {
        if (StringUtils.isEmpty(base64))
        {
            LOGGER.error("invalid base64.");
            return null;
        }
        try
        {
            if (base64.contains(Const.SPLIT))
            {
                //去掉base64中的文件头前缀(如：'data:image/png;base64,')
                base64 = base64.substring(base64.lastIndexOf(Const.SPLIT));
            }
            byte[] data = Base64.getMimeDecoder().decode(base64);
            LOGGER.info("current image file type:{}", FileType.getType(data));
            return data;
        }
        catch (Exception e)
        {
            LOGGER.error("failed to get byte[] from base64.", e);
        }
        return null;
    }

    /**
     * 二进制图片数据转换成base64
     *
     * @param data 图片二进制数据
     * @return 图片base64
     */
    public static String toBase64(byte[] data)
    {
        if (null == data)
        {
            LOGGER.error("invalid image file data.");
            return null;
        }

        try
        {
            LOGGER.info("current image file type:{}", FileType.getType(data));
            String base64 = Base64.getEncoder().encodeToString(data);
            if (!StringUtils.isEmpty(base64) && base64.contains(Const.SPLIT))
            {
                //去掉base64中的文件头前缀(如：'data:image/png;base64,')
                base64 = base64.substring(base64.lastIndexOf(Const.SPLIT));
            }
            return base64;
        }
        catch (Exception e)
        {
            LOGGER.error("failed to get base64 by byte[].", e);
        }
        return null;
    }

    /**
     * 图片文件转base64
     *
     * @param path 文件路径
     * @return 文件的base64
     */
    public static String toBase64(String path)
    {
        return toBase64(FileUtil.read(path));
    }

    /**
     * 图片二进制写入成文件
     *
     * @param data 图片二进制
     * @param path 图片文件路径
     */
    public static void write(byte[] data, String path)
    {
        write(data, path, false);
    }

    /**
     * 图片二进制写入成文件
     *
     * @param data     图片二进制
     * @param path     图片文件路径
     * @param needType 是否需要添加图片格式
     */
    public static void write(byte[] data, String path, boolean needType)
    {
        if (null == data || StringUtils.isEmpty(path))
        {
            LOGGER.error("invalid image file.");
            return;
        }

        if (needType)
        {
            String suffix = Const.POINT + getType(data);
            if (!StringUtils.endsWithIgnoreCase(path, suffix))
            {
                path += suffix;
            }
        }
        FileUtil.write(data, path);
    }

    /**
     * 获取图片数据的图片类型
     *
     * @param data 图片二进制数据
     * @return 图片类型，如jpeg/jpg/gif/png等
     */
    public static String getType(byte[] data)
    {
        if (null == data)
        {
            LOGGER.error("invalid image data.");
            return null;
        }

        ImageInputStream in = null;
        try
        {
            in = new MemoryCacheImageInputStream(new ByteArrayInputStream(data));
            Iterator<ImageReader> iterator = ImageIO.getImageReaders(in);
            if (iterator.hasNext())
            {
                String imageType = iterator.next().getFormatName();
                LOGGER.info("current image type:{}", imageType);
                return imageType;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("failed to get image type:{},with exception:{}.", FileType.getType(data), e);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
        return null;
    }

    private ImageUtil()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtil.class);
}
