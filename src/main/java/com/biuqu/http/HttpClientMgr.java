package com.biuqu.http;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClient管理器
 *
 * @author BiuQu
 * @date 2023/1/3 20:23
 */
public final class HttpClientMgr
{
    public HttpClientMgr(HttpParam param)
    {
        this.param = param;
        this.reqConf = this.buildReqConf();
        this.connMgr = this.buildConnMgr();
        this.retryHandler = new HttpRetryHandler();
        LOGGER.info("create http connection[{}] now.", this.param.getId());
    }

    /**
     * 获取连接builder
     *
     * @return 连接构造器
     */
    public HttpClientBuilder clientBuilder()
    {
        LOGGER.info("create a new http client[{}] now.", this.param.getId());
        return HttpClients.custom().setConnectionManager(connMgr).setRetryHandler(retryHandler);
    }

    /**
     * 获取连接配置参数
     *
     * @return 连接参数对象
     */
    public RequestConfig getReqConf()
    {
        return reqConf;
    }

    /**
     * 构建默认的连接管理器
     *
     * @return http连接管理器
     */
    private PoolingHttpClientConnectionManager buildConnMgr()
    {
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();
        //最大的连接数
        connMgr.setMaxTotal(this.param.getMaxConn());
        //单通道的最大连接数
        connMgr.setDefaultMaxPerRoute(this.param.getMaxConn());

        SocketConfig.Builder builder = SocketConfig.custom();
        builder.setSoKeepAlive(true);
        builder.setTcpNoDelay(true);
        if (this.param.getSoLinger() > 0)
        {
            builder.setSoLinger(this.param.getSoLinger());
        }
        connMgr.setDefaultSocketConfig(builder.build());
        return connMgr;
    }

    /**
     * 构建默认的连接对象
     *
     * @return 连接配置
     */
    private RequestConfig buildReqConf()
    {
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setSocketTimeout(this.param.getTimeout());
        builder.setConnectTimeout(this.param.getConnTimeout());
        builder.setConnectionRequestTimeout(this.param.getConnTimeout());
        return builder.build();
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientMgr.class);

    /**
     * http配置参数
     */
    private final HttpParam param;

    /**
     * 连接配置参数
     */
    private RequestConfig reqConf;

    /**
     * 连接管理器
     */
    private PoolingHttpClientConnectionManager connMgr;

    /**
     * 重试控制器
     */
    private HttpRequestRetryHandler retryHandler;
}
