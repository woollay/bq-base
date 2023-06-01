package com.biuqu.utils;

import com.biuqu.model.FileType;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * PDF处理工具类
 *
 * @author BiuQu
 * @date 2023/1/10 09:43
 */
public final class PdfUtil
{
    /**
     * 把pdf转成图片base64集合
     *
     * @param path 文件路径
     * @return 图片base64集合
     */
    public static List<String> toImages(String path)
    {
        return toImages(IoUtil.readInputStream(path));
    }

    /**
     * 把pdf转成图片base64集合
     *
     * @param in 文件输入流
     * @return 图片base64集合
     */
    public static List<String> toImages(InputStream in)
    {
        List<String> list = Lists.newArrayList();
        try
        {
            PDDocument document = PDDocument.load(in);
            int count = document.getNumberOfPages();

            PDFRenderer renderer = new PDFRenderer(document);
            for (int i = 0; i < count; i++)
            {
                BufferedImage image = renderer.renderImage(i);
                byte[] bytes = ImageUtil.toBytes(image, FileType.PNG.name());
                String base64 = ImageUtil.toBase64(bytes);
                if (!StringUtils.isEmpty(base64))
                {
                    list.add(base64);
                }
            }
        }
        catch (IOException e)
        {
            LOGGER.error("failed to get image from pdf.", e);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
        return list;
    }

    private PdfUtil()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfUtil.class);
}
