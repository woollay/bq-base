package com.biuqu.model;

import com.biuqu.constants.Const;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 业务安全基础模型
 *
 * @author BiuQu
 * @date 2023/1/18 20:29
 */
@Data
public abstract class BaseSecurity
{
    /**
     * 业务字典模型转换成标准的字典模型
     *
     * @return 字典模型
     */
    public GlobalDict toDict()
    {
        return GlobalDict.toClientDict();
    }

    /**
     * 获取key
     *
     * @return 业务key
     */
    public String toKey()
    {
        return this.getId();
    }

    /**
     * 批量的key
     *
     * @return 批量时的key
     */
    public String toBatchKey()
    {
        return this.toKey();
    }

    /**
     * 生成完整性的明文(拼接业务字段,由各业务按照安全规则自己定义)
     *
     * @return 拼接后的完整性明文
     */
    public String toIntegrity()
    {
        return this.secKey;
    }

    /**
     * 是否为空对象
     *
     * @return true表示为空
     */
    @JsonIgnore
    public boolean isEmpty()
    {
        return false;
    }

    /**
     * 拼接key
     *
     * @param keys 拼接前的key关键字段集合
     * @return 拼接key
     */
    protected final String linkKeys(String... keys)
    {
        List<String> batchKey = Lists.newArrayList();
        int size = 0;
        if (null != keys)
        {
            size = keys.length;
        }

        for (int i = 0; i < size; i++)
        {
            String element = StringUtils.EMPTY;
            if (!StringUtils.isEmpty(keys[i]))
            {
                element = keys[i];
            }
            batchKey.add(element);
        }

        String link = StringUtils.EMPTY;
        if (size > 0)
        {
            link = StringUtils.join(keys, Const.SPLIT, 0, size);
        }
        return link;
    }

    /**
     * 启动时间
     */
    private long start;

    /**
     * 模型id
     */
    private String id;

    /**
     * 业务安全key
     */
    private String secKey;
}
