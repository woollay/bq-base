package com.biuqu.model;

import com.biuqu.constants.Const;
import com.biuqu.utils.MathUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 图片的质量系数
 *
 * @author BiuQu
 * @date 2023/1/12 09:53
 */
@Data
public class ImageFactor
{
    public ImageFactor(int size)
    {
        this.size = size;
    }

    /**
     * 是否是合法的大小
     *
     * @param size    压缩前的大小
     * @param newSize 新图片文件大小
     * @return true表示合理压缩, false表示压缩过度
     */
    public static boolean validSize(int size, int newSize)
    {
        return MIN_SIZE_RATE < MathUtil.toRate(newSize, size);
    }

    /**
     * 是否能压缩
     *
     * @return true表示能压缩
     */
    public boolean canCompress()
    {
        return this.leftCompressTimes > 0 && this.size <= this.maxSize && this.size >= this.minSize;
    }

    /**
     * 计算下一个质量因子
     *
     * @param newSize 压缩后的大小
     * @return 下一次压缩的质量因子
     */
    public ImageFactor next(int newSize)
    {
        ImageFactor factor = new ImageFactor(this.size);
        BeanUtils.copyProperties(this, factor);
        this.leftCompressTimes--;

        if (newSize > this.maxSize)
        {
            this.beyondTimes++;
            if (this.beyondTimes >= this.timesThreshold)
            {
                int scale = MathUtil.avg(this.maxSize, this.size) * MAX_Q / this.size;
                if (this.scale < MAX_Q)
                {
                    scale *= MathUtil.avg(this.scale, MAX_Q);
                }
                factor.setScale(scale);
                factor.setQuality(DEFAULT_Q);
                factor.setMinQuality(MIN_Q);
                factor.setMaxQuality(MAX_Q);
            }
            else
            {
                factor.setQuality(MathUtil.avg(this.maxQuality, this.quality));
                factor.setMaxQuality(this.quality);
            }
        }
        else if (newSize < this.minSize)
        {
            this.beyondTimes = 0;
            factor.setQuality(MathUtil.avg(this.maxQuality, this.quality));
            factor.setMinQuality(this.quality);
        }
        else
        {
            this.beyondTimes = 0;
            return this;
        }

        factor.setLeftCompressTimes(this.leftCompressTimes);
        factor.setSize(newSize);
        factor.setBeyondTimes(this.beyondTimes);
        return factor;
    }

    /**
     * scale系数(百分比)
     *
     * @return 大小系数
     */
    public float toScaleRate()
    {
        return MathUtil.toRate(this.scale);
    }

    /**
     * 质量系数(百分比)
     *
     * @return 质量系数
     */
    public float toQualityRate()
    {
        return MathUtil.toRate(this.quality);
    }

    /**
     * 大小系数，大小在(0,100)之间
     */
    private int scale = MAX_Q;

    /**
     * 质量系数，大小在(0,100)之间
     */
    private int quality = DEFAULT_Q;

    /**
     * 最小质量系数
     */
    private int minQuality = MIN_Q;

    /**
     * 最大质量系数
     */
    private int maxQuality = MAX_Q;

    /**
     * 连续过大或者过小的持续次数
     */
    private int beyondTimes;

    /**
     * 最大压缩次数
     */
    private int maxCompressTimes = 5;

    /**
     * 剩余压缩次数
     */
    private int leftCompressTimes = maxCompressTimes;

    /**
     * 连续过大或者过小的持续次数
     */
    private int timesThreshold = Const.TWO;

    /**
     * 图片文件大小
     */
    private int size;

    /**
     * 最小图片大小
     */
    private int minSize = 20 * 1024;

    /**
     * 最大图片大小
     */
    private int maxSize = 30 * 1024;

    /**
     * 最大质量系数
     */
    private static final int MAX_Q = 100;

    /**
     * 最大质量系数(默认值)
     */
    private static final int DEFAULT_Q = 55;

    /**
     * 最小质量系数(默认值)
     */
    private static final int MIN_Q = 30;

    /**
     * 最小的图片大小压缩率
     */
    private static final float MIN_SIZE_RATE = 0.01f;
}
