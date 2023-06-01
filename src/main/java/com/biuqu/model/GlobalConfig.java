package com.biuqu.model;

import com.biuqu.constants.Const;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 基于接口/客户的全局配置表模型
 *
 * @author BiuQu
 * @date 2023/1/28 22:42
 */
@Data
public class GlobalConfig extends BaseSecurity
{
    /**
     * 根据guava key构造全局缓存对象
     *
     * @param key 查询key
     * @return 全局通用配置对象
     */
    public static GlobalConfig toBean(String key)
    {
        String[] keys = StringUtils.split(key, Const.SPLIT);

        GlobalConfig config = new GlobalConfig();
        int i = 0;
        String clientId = keys[i++];
        if (!StringUtils.isEmpty(clientId))
        {
            config.setClientId(clientId);
        }

        String urlId = keys[i++];
        if (!StringUtils.isEmpty(urlId))
        {
            config.setUrlId(urlId);
        }

        config.setSvcId(keys[i]);
        return config;
    }

    /**
     * 是否为空对象
     *
     * @return true表示空对象
     */
    @Override
    public boolean isEmpty()
    {
        return StringUtils.isEmpty(this.svcId);
    }

    /**
     * 拼接限流guava key
     * <p>
     * 如：app001,CLIENT_TOKEN_API,client.limit.qps
     *
     * @return 限流key
     */
    @Override
    public String toKey()
    {
        return this.linkKeys(this.clientId, this.urlId, this.svcId);
    }

    @Override
    public String toBatchKey()
    {
        return this.toKey();
    }

    /**
     * 客户唯一标识
     */
    private String clientId;

    /**
     * 接口唯一标识
     */
    private String urlId;

    /**
     * 业务主键
     */
    private String svcId;

    /**
     * 业务值
     */
    private String svcValue;

    /**
     * 创建时间(ms)
     */
    private long createTime;
}
