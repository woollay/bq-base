package com.biuqu.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Json打码规则
 *
 * @author BiuQu
 * @date 2023/1/5 08:27
 */
@Data
public class JsonRule
{
    /**
     * 应用打码规则
     *
     * @param value 打码前的值
     * @return 打码后的值
     */
    public String apply(String value)
    {
        if (StringUtils.isEmpty(value) || this.index >= value.length() || this.len >= value.length())
        {
            return StringUtils.EMPTY;
        }

        StringBuilder builder = new StringBuilder();
        int start = this.index;
        int size = value.length();
        //保留固定长度的打码(start为非正数)
        if (start <= 0)
        {
            if (this.len <= 0)
            {
                return StringUtils.EMPTY;
            }
            builder.append(value, 0, this.len);
            builder.append(StringUtils.repeat(this.mask, MASK_LEN));
        }
        //前置和后置之外的字符打码
        else if (this.len < 0)
        {
            int last = size + this.len;
            if (last <= this.index)
            {
                builder.append(value, 0, last);
                builder.append(StringUtils.repeat(this.mask, (size - last)));
            }
            else
            {
                builder.append(value, 0, this.index);
                builder.append(StringUtils.repeat(this.mask, (last - this.index)));
                builder.append(value, last, size);
            }
        }
        //后面的全部打码
        else if (this.len == 0)
        {
            builder.append(value, 0, start);
            builder.append(StringUtils.repeat(this.mask, (size - start)));
        }
        //常规的指定正数长度打码
        else
        {
            int end = Math.min(this.len + start, size);
            builder.append(value, 0, start);
            builder.append(StringUtils.repeat(this.mask, (end - start)));
            if (end < size)
            {
                builder.append(value, end, size);
            }
        }
        return builder.toString();
    }

    /**
     * 缩短长度时，显示的打码符长度
     */
    private static final int MASK_LEN = 6;

    /**
     * 打码的属性名
     */
    private String name;

    /**
     * 打码的替换字符(默认为*)
     */
    private String mask = "*";

    /**
     * 打码的替换字符的长度
     * 负数:表示前置打码
     * 正数:后置打码
     */
    private int len;

    /**
     * 打码的起始位置
     * 非正数:表示长度缩减
     */
    private int index;
}
