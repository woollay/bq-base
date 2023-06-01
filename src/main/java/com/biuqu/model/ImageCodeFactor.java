package com.biuqu.model;

import com.biuqu.utils.RandomUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

/**
 * 图片验证码的因子
 *
 * @author BiuQu
 * @date 2023/1/12 10:25
 */
@Data
public class ImageCodeFactor
{
    /**
     * 生成文本(如果不存在就随机生成)
     *
     * @return 指定内容的文本内容
     */
    public String genText()
    {
        if (StringUtils.isEmpty(this.text))
        {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < this.len; i++)
            {
                builder.append(this.scope.charAt(RandomUtil.next(0, this.scope.length())));
            }
            this.text = builder.toString();
        }
        return this.text;
    }

    /**
     * 文本内容
     */
    private String text;

    /**
     * 单个字符的宽度(可配置)
     */
    private int width = 33;

    /**
     * 显示的高度(可配置)
     */
    private int height = 40;

    /**
     * 随机的长度(可配置)
     */
    private int len = 4;

    /**
     * 干扰线(可配置)
     */
    private int blockLine = 6;

    /**
     * 干扰点(可配置)
     */
    private int blockPoint = 50;

    /**
     * 显示字体(可配置)
     */
    private Font font = new Font("Arial", Font.BOLD, 30);

    /**
     * 画图文字的取值范围(可配置)
     */
    private String scope = "1234567890";

    /**
     * x坐标的起始位置
     */
    private int x = 10;

    /**
     * y坐标的起始位置
     */
    private int y = 45;

    /**
     * 倾斜角
     */
    private int degree = 30;

    /**
     * 干扰点的宽度
     */
    private int pointWidth = 2;

    /**
     * 干扰点的高度
     */
    private int pointHeight = 3;
}
