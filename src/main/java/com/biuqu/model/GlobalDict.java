package com.biuqu.model;

import com.biuqu.constants.Const;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 全局字典模型
 *
 * @author BiuQu
 * @date 2023/1/29 19:47
 */
@Data
public class GlobalDict extends BaseSecurity
{
    /**
     * 转换成客户参数的字典类型对象
     *
     * @return 字典参数对象
     */
    public static GlobalDict toClientDict()
    {
        GlobalDict dict = new GlobalDict();
        dict.setType(CLIENT_CONFIG_TYPE);
        return dict;
    }

    /**
     * 转换成渠道参数的字典类型对象
     *
     * @return 字典参数对象
     */
    public static GlobalDict toChannelDict()
    {
        GlobalDict dict = new GlobalDict();
        dict.setType(CHANNEL_CONFIG_TYPE);
        return dict;
    }

    /**
     * 转换成渠道url参数的字典类型对象
     *
     * @return 字典参数对象
     */
    public static GlobalDict toChannelUrl()
    {
        GlobalDict dict = new GlobalDict();
        dict.setType(CHANNEL_URL_TYPE);
        return dict;
    }

    /**
     * 转换成渠道参数的字典类型对象
     *
     * @return 字典参数对象
     */
    public static GlobalDict toChannelStatus()
    {
        GlobalDict dict = new GlobalDict();
        dict.setKey(CHANNEL_STATUS);
        return dict;
    }

    /**
     * 转换成渠道参数的字典类型对象
     *
     * @return 字典参数对象
     */
    public static GlobalDict toChannelSnake()
    {
        GlobalDict dict = new GlobalDict();
        dict.setKey(CHANNEL_SNAKE);
        return dict;
    }

    /**
     * 把缓存key还原成模型对象
     *
     * @param key 缓存key
     * @return 模型参数对象
     */
    public static GlobalDict toDict(String key)
    {
        GlobalDict dict = new GlobalDict();
        //1.兼容id查询场景
        if (!key.contains(Const.SPLIT))
        {
            dict.setId(key);
        }
        else
        {
            int i = 0;
            String[] keys = StringUtils.split(key, Const.SPLIT);
            String val = keys[i++];
            dict.setType(keys[i]);
            if (val.contains(VALUE_SUFFIX))
            {
                val = val.replace(VALUE_SUFFIX, StringUtils.EMPTY);
                dict.setValue(val);
            }
            else
            {
                dict.setKey(val);
            }
        }
        return dict;
    }

    @Override
    public String toKey()
    {
        //1.支持id查询
        String key = this.getId();
        //2.支持key查询value，也支持value查询key
        if (StringUtils.isEmpty(key))
        {
            String val = StringUtils.EMPTY;
            if (!StringUtils.isEmpty(this.getKey()))
            {
                val = this.getKey();
            }
            //value查询key
            else if (!StringUtils.isEmpty(this.getValue()))
            {
                val = this.getValue() + VALUE_SUFFIX;
            }

            //基于key或者value拼接缓存key
            key = StringUtils.joinWith(Const.SPLIT, val, this.getType());
        }
        return key;
    }

    @Override
    public String toBatchKey()
    {
        return this.getType();
    }

    @Override
    public boolean isEmpty()
    {
        return StringUtils.isEmpty(this.key) && StringUtils.isEmpty(this.value) && StringUtils.isEmpty(this.type);
    }

    @Override
    public GlobalDict toDict()
    {
        GlobalDict dict = new GlobalDict();
        dict.setType(CLIENT_URL_TYPE);
        if (!StringUtils.isEmpty(this.key))
        {
            dict.setKey(this.key);
        }
        else if (!StringUtils.isEmpty(this.value))
        {
            dict.setValue(this.value);
        }
        return dict;
    }

    /**
     * key后缀
     */
    private static final String VALUE_SUFFIX = ":val";

    /**
     * 客户的url配置
     */
    private static final String CLIENT_URL_TYPE = "ClientUrl";

    /**
     * 客户的业务参数配置
     */
    private static final String CLIENT_CONFIG_TYPE = "ClientConfig";

    /**
     * 渠道的url配置
     */
    private static final String CHANNEL_URL_TYPE = "ChannelUrl";

    /**
     * 渠道的业务参数配置
     */
    private static final String CHANNEL_CONFIG_TYPE = "ChannelConfig";

    /**
     * 渠道接口是否是驼峰的key
     */
    private static final String CHANNEL_SNAKE = "channel.snake";

    /**
     * 渠道接口状态key
     */
    private static final String CHANNEL_STATUS = "channel.status";

    /**
     * 业务模型类型
     */
    private String type;

    /**
     * 业务字典key
     */
    private String key;

    /**
     * 业务字典值
     */
    private String value;
}
