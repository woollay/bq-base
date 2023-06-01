package com.biuqu.http;

import lombok.Data;

/**
 * Http配置参数
 *
 * @author BiuQu
 * @date 2023/2/2 19:36
 */
@Data
public class HttpParam
{
    /**
     * 连接ID
     */
    private String id;

    /**
     * 连接超时时间(ms)
     */
    private int connTimeout = 1000;

    /**
     * 请求超时时间(ms)
     */
    private int timeout = 3000;

    /**
     * 关闭socket之后延迟关闭http通道的时间(ms)
     */
    private int soLinger = 1000;

    /**
     * 最大连接数
     */
    private int maxConn = 1000;
}
