package com.biuqu.utils;

import com.biuqu.model.FileType;
import com.biuqu.model.ImageCodeFactor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Locale;

/**
 * 图像验证码工具类
 *
 * @author BiuQu
 * @date 2023/1/12 10:22
 */
public final class ImageCodeUtil
{
    /**
     * 生成图片，并画若干干扰线(验证码场景)
     *
     * @param factor 验证码的配置因子,调用后，随机数可通过factor.getText()获取
     * @return 图片二进制数据
     */
    public static byte[] drawImage(ImageCodeFactor factor)
    {
        return drawImage(factor, FileType.PNG.name().toLowerCase(Locale.US));
    }

    /**
     * 生成图片，并画若干干扰线(验证码场景)
     *
     * @param factor    验证码的配置因子,调用后，随机数可通过factor.getText()获取
     * @param imageType 图片类型
     * @return 图片二进制数据
     */
    public static byte[] drawImage(ImageCodeFactor factor, String imageType)
    {
        int width = factor.getWidth() * factor.getLen();
        BufferedImage image = new BufferedImage(width, factor.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, factor.getHeight());
        graphics.setFont(factor.getFont());

        drawText(graphics, factor);
        drawLine(graphics, factor);
        drawPoint(graphics, factor);

        return ImageUtil.toBytes(image, imageType);
    }

    /**
     * 生成RGB颜色对象
     *
     * @return 颜色对象
     */
    public static Color genRgb()
    {
        int red = RandomUtil.next(0, MAX_COLOR_NUM);
        int green = RandomUtil.next(0, MAX_COLOR_NUM);
        int blue = RandomUtil.next(0, MAX_COLOR_NUM);
        return new Color(red, green, blue);
    }

    /**
     * 画文本
     *
     * @param graphics 画图对象
     * @param factor   配置参数
     */
    public static void drawText(Graphics2D graphics, ImageCodeFactor factor)
    {
        int startX = factor.getX();
        String text = factor.genText();
        for (int i = 0; i < text.length(); i++)
        {
            graphics.setColor(genRgb());
            int degree = RandomUtil.next(0, MAX_DEGREE);
            graphics.rotate(Math.toRadians(degree), startX, factor.getY());
            graphics.drawString(String.valueOf(text.charAt(i)), startX, factor.getWidth());
            graphics.rotate(-Math.toRadians(degree), startX, factor.getY());

            startX += factor.getWidth();
        }
    }

    /**
     * 画线
     *
     * @param graphics 画图对象
     * @param factor   配置参数
     */
    public static void drawLine(Graphics2D graphics, ImageCodeFactor factor)
    {
        int width = factor.getWidth() * factor.getLen();
        SecureRandom random = RandomUtil.create();
        for (int i = 0; i < factor.getBlockLine(); i++)
        {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(factor.getHeight());

            int x2 = random.nextInt(width);
            int y2 = random.nextInt(factor.getHeight());

            graphics.setColor(genRgb());
            graphics.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 画点
     *
     * @param graphics 画图对象
     * @param factor   配置参数
     */
    public static void drawPoint(Graphics2D graphics, ImageCodeFactor factor)
    {
        int width = factor.getWidth() * factor.getLen();
        SecureRandom random = RandomUtil.create();
        for (int i = 0; i < factor.getBlockPoint(); i++)
        {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(factor.getHeight());

            graphics.setColor(genRgb());
            graphics.fillRect(x1, y1, factor.getPointWidth(), factor.getPointHeight());
        }
    }

    private ImageCodeUtil()
    {
    }

    /**
     * 最大的颜色值
     */
    private static final int MAX_COLOR_NUM = 256;

    /**
     * 最大的角度
     */
    private static final int MAX_DEGREE = 30;
}
