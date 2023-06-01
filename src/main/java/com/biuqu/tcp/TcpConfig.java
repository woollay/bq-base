package com.biuqu.tcp;

import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 连接配置参数
 *
 * @author BiuQu
 * @date 2023/1/6 20:32
 */
@Data
public class TcpConfig extends GenericObjectPoolConfig
{
    /**
     * 编码格式
     */
    private String encoding;

    /**
     * 主机IP/域名
     */
    private String host;

    /**
     * 主机端口
     */
    private int port;

    /**
     * socket建立连接的超时时间
     */
    private int connTimeout;

    /**
     * socket超时时间
     */
    private int soTimeout;

    /**
     * socket关闭的延迟时间
     */
    private int soLinger;
}
