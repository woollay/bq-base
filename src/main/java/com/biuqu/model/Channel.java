package com.biuqu.model;

import lombok.Data;

/**
 * 渠道参数配置
 *
 * @author BiuQu
 * @date 2023/1/21 14:29
 */
@Data
public class Channel
{
    /**
     * 针对秘钥对接场景的私钥
     */
    private String priKey;

    /**
     * 针对秘钥对接场景的公钥
     */
    private String pubKey;

    /**
     * 针对JwtToken认证场景的客户id
     */
    private String appId;
    
    /**
     * 针对JwtToken认证场景的客户key
     */
    private String appKey;

    /**
     * 认证类型
     */
    private String type;

    /**
     * 认证证书路径
     */
    private String keyPath;

    /**
     * 系统码
     */
    private String sysCode;

    /**
     * 服务id
     */
    private String serviceId;

    /**
     * 前期的认证url
     */
    private String authUrl;

    /**
     * 对接地址
     */
    private String url;

    /**
     * 对接主机名名
     */
    private String host;

    /**
     * 对接端口
     */
    private int port;

    /**
     * 对接超时时间
     */
    private int timeout;

    /**
     * 对接连接超时时间
     */
    private int connTimeout;
}
