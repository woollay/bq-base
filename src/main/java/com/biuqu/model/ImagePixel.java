package com.biuqu.model;

import lombok.Data;

/**
 * 图片像素大小(默认640*480)
 * <p>
 * 等比例缩放
 *
 * @author BiuQu
 * @date 2023/1/12 20:10
 */
@Data
public class ImagePixel
{
    /**
     * 构造图片像素对象
     *
     * @param width     宽度像素
     * @param height    高度像素
     * @param colorType 色彩类型
     * @return 获取新的像素对象
     */
    public static ImagePixel build(int width, int height, int colorType)
    {
        return new ImagePixel(width, height, colorType);
    }

    /**
     * 重置图片的像素大小(用内置的像素大小去压缩)
     *
     * @return 获取新的像素对象
     */
    public ImagePixel compress()
    {
        return compress(MIN_WIDTH, MIN_HEIGHT);
    }

    /**
     * 重置图片的像素大小
     *
     * @param width  期望的宽度像素
     * @param height 期望的高度像素
     * @return 获取新的实际像素对象(宽或者高达到期望)
     */
    public ImagePixel compress(int width, int height)
    {
        int newWidth = width;
        int newHeight = height;
        if (this.width > width)
        {
            newHeight = newWidth * height / width;
        }
        return new ImagePixel(newWidth, newHeight, this.colorType);
    }

    private ImagePixel(int width, int height, int colorType)
    {
        this.width = width;
        this.height = height;
        this.colorType = colorType;
    }

    /**
     * 图片宽度像素
     */
    private int width;

    /**
     * 图片高度像素
     */
    private int height;

    /**
     * 色彩类型
     */
    private int colorType;

    /**
     * 图片的像素压缩率
     */
    private static final float MIN_RATE = 0.55f;

    /**
     * 图片的最小宽度像素
     */
    private static final int MIN_WIDTH = 640;

    /**
     * 图片的最小高度像素
     */
    private static final int MIN_HEIGHT = 480;
}
