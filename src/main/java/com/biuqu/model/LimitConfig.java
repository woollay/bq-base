package com.biuqu.model;

/**
 * 限流对象
 *
 * @author BiuQu
 * @date 2023/2/11 15:24
 */
public class LimitConfig extends GlobalConfig
{
    /**
     * 构建渠道限流配置对象
     *
     * @return 限流配置对象
     */
    public static LimitConfig channelConf()
    {
        return build(CHANNEL_TYPE);
    }

    /**
     * 构建客户端限流配置对象
     *
     * @return 限流配置对象
     */
    public static LimitConfig clientConf()
    {
        return build(CLIENT_TYPE);
    }

    /**
     * 构建相同类型的限流对象
     *
     * @return 限流配置对象
     */
    public LimitConfig rebuild()
    {
        LimitConfig config = build(this.type);
        config.setUrlId(this.getUrlId());
        config.setClientId(this.getClientId());
        config.setSvcId(this.getSvcId());
        return config;
    }

    /**
     * 获取最大调用量的限流key
     *
     * @return 最大调用量的限流key
     */
    public String toMaxDbKey()
    {
        return this.type + MAX_DB_KEY;
    }

    /**
     * 获取最大调用量的限流对应的单位key
     *
     * @return 最大调用量的限流对应的单位key
     */
    public String toMaxDbUnitKey()
    {
        return this.toMaxDbKey() + LIMIT_DB_UNIT;
    }

    /**
     * 获取qps的限流key
     *
     * @return qps的限流key
     */
    public String toQpsDbKey()
    {
        return this.type + QPS_DB_KEY;
    }

    /**
     * 获取qps的限流对应的单位key
     *
     * @return qps的限流对应的单位key
     */
    public String toQpsDbUnitKey()
    {
        return this.toQpsDbKey() + LIMIT_DB_UNIT;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    private LimitConfig(String type)
    {
        this.type = type;
    }

    /**
     * 构建限流配置对象
     *
     * @param type 限流配置的类型(客户端限流还是渠道限流)
     * @return 限流配置对象
     */
    private static LimitConfig build(String type)
    {
        if (!CHANNEL_TYPE.equalsIgnoreCase(type))
        {
            type = CLIENT_TYPE;
        }
        return new LimitConfig(type);
    }

    /**
     * 客户限流类型
     */
    private static final String CLIENT_TYPE = "client";

    /**
     * 渠道限流类型
     */
    private static final String CHANNEL_TYPE = "channel";

    /**
     * max限流key
     */
    private static final String MAX_DB_KEY = ".limit.max";

    /**
     * qps限流key
     */
    private static final String QPS_DB_KEY = ".limit.qps";

    /**
     * 限流的阈值单位
     */
    private static final String LIMIT_DB_UNIT = ".unit";

    /**
     * 限流类型(client or channel)
     */
    private final String type;

    /**
     * 单位
     */
    private String unit;
}
