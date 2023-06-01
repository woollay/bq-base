package com.biuqu.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;

/**
 * Spring RestTemplate的构建工厂类(还是基于Apache HttpComponent构建)
 *
 * @author BiuQu
 * @date 2023/1/4 11:49
 */
public final class RestTemplateFactory
{
    public RestTemplateFactory(HttpClientMgr clientMgr)
    {
        this.clientMgr = clientMgr;
    }

    /**
     * 创建一个通用http连接对象
     *
     * @return 标准的http连接对象
     */
    public CommonRestTemplate create()
    {
        CloseableHttpClient httpClient = clientMgr.clientBuilder().build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RequestConfig reqConf = clientMgr.getReqConf();
        factory.setConnectTimeout(reqConf.getConnectTimeout());
        factory.setReadTimeout(reqConf.getSocketTimeout());
        factory.setConnectionRequestTimeout(reqConf.getConnectionRequestTimeout());

        CommonRestTemplate restTemplate = new CommonRestTemplate();
        restTemplate.setRequestFactory(factory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

        return restTemplate;
    }

    /**
     * http管理器
     */
    private final HttpClientMgr clientMgr;
}
