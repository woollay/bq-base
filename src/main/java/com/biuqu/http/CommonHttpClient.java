package com.biuqu.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 标准的HttpClient对象
 *
 * @author BiuQu
 * @date 2023/1/4 08:03
 */
public final class CommonHttpClient
{
    /**
     * 发起Get请求
     *
     * @param url    请求的url
     * @param params url后面的参数集合
     * @return 结果对象
     */
    public String get(String url, Map<String, String> params)
    {
        HttpGet request = new HttpGet(HttpHelper.toUrlParam(url, params));
        return invoke(request, true);
    }

    /**
     * 发起Get请求
     *
     * @param url     请求的url
     * @param params  url后面的参数集合
     * @param handler 结果处理对象
     * @param <T>     结果类型
     * @return 结果对象
     */
    public <T> T get(String url, Map<String, String> params, ResponseHandler<T> handler)
    {
        HttpGet request = new HttpGet(HttpHelper.toUrlParam(url, params));
        return invoke(request, handler);
    }

    /**
     * 发起http调用并获取指定类型的对象(默认复用http连接)
     *
     * @param url     请求的URL
     * @param headers header头集合
     * @param body    body参数
     * @param handler 结果处理对象
     * @param <T>     结果类型
     * @return 结果对象
     */
    public <T> T post(String url, List<Header> headers, HttpEntity body, ResponseHandler<T> handler)
    {
        return post(url, headers, body, handler, true);
    }

    /**
     * 发起http调用并获取指定类型的对象
     *
     * @param url  请求的URL
     * @param body body参数
     * @return 结果对象
     */
    public String post(String url, String body)
    {
        return post(url, null, HttpHelper.toBody(body));
    }

    /**
     * 发起http调用并获取指定类型的对象
     *
     * @param url     请求的URL
     * @param headers header头集合
     * @param body    body参数
     * @return 结果对象
     */
    public String post(String url, List<Header> headers, HttpEntity body)
    {
        return post(url, headers, body, true);
    }

    /**
     * 发起http调用并获取指定类型的对象
     *
     * @param url     请求的URL
     * @param headers header头集合
     * @param body    body参数
     * @param reuse   是否复用HttpClient，true表示复用，否则创建一个新的对象
     * @return 结果对象
     */
    public String post(String url, List<Header> headers, HttpEntity body, boolean reuse)
    {
        HttpPost request = HttpHelper.buildHttpPost(url, headers, body, clientMgr.getReqConf());
        return invoke(request, reuse);
    }

    /**
     * 发起http调用并获取指定类型的对象
     *
     * @param url     请求的URL
     * @param headers header头集合
     * @param body    body参数
     * @param handler 结果处理对象
     * @param reuse   是否复用HttpClient，true表示复用，否则创建一个新的对象
     * @param <T>     结果类型
     * @return 结果对象
     */
    public <T> T post(String url, List<Header> headers, HttpEntity body, ResponseHandler<T> handler, boolean reuse)
    {
        HttpPost request = HttpHelper.buildHttpPost(url, headers, body, clientMgr.getReqConf());
        return invoke(request, handler, reuse);
    }

    /**
     * 发起http调用并获取指定类型的对象(默认复用http连接)
     *
     * @param request 请求对象
     * @return 结果对象
     */
    public String invoke(HttpRequestBase request)
    {
        return invoke(request, true);
    }

    /**
     * 发起http调用并获取指定类型的对象(默认复用http连接)
     *
     * @param request 请求对象
     * @param handler 结果处理对象
     * @param <T>     结果类型
     * @return 结果对象
     */
    public <T> T invoke(HttpRequestBase request, ResponseHandler<T> handler)
    {
        return invoke(request, handler, true);
    }

    /**
     * 发起http调用并获取指定类型的对象
     *
     * @param request 请求对象
     * @param reuse   是否复用HttpClient，true表示复用，否则创建一个新的对象
     * @return 结果对象
     */
    public String invoke(HttpRequestBase request, boolean reuse)
    {
        HttpResponse response = invokeResult(request, reuse);
        if (null != response)
        {
            return HttpHelper.getResult(response);
        }
        return null;
    }

    /**
     * 发起http调用并获取指定类型的对象
     *
     * @param request 请求对象
     * @param handler 结果处理对象
     * @param reuse   是否复用HttpClient，true表示复用，否则创建一个新的对象
     * @return 结果对象
     */
    public <T> T invoke(HttpRequestBase request, ResponseHandler<T> handler, boolean reuse)
    {
        HttpResponse response = invokeResult(request, reuse);
        try
        {
            if (null != response)
            {
                return handler.handleResponse(response);
            }
        }
        catch (IOException e)
        {
            LOGGER.error("failed to get http response.", e);
        }
        return null;
    }

    /**
     * 发起http调用并获取指定类型的对象
     *
     * @param request 请求对象
     * @param reuse   是否复用HttpClient，true表示复用，否则创建一个新的对象
     * @return 结果对象
     */
    private HttpResponse invokeResult(HttpRequestBase request, boolean reuse)
    {
        long start = System.currentTimeMillis();
        CloseableHttpClient client = this.httpClient;
        if (!reuse)
        {
            client = this.clientMgr.clientBuilder().build();
        }
        if (null == request.getConfig())
        {
            request.setConfig(this.clientMgr.getReqConf());
        }
        try
        {
            return client.execute(request);
        }
        catch (IOException e)
        {
            LOGGER.error("failed to execute http request.", e);
        }
        finally
        {
            LOGGER.info("current http request cost {}ms.", (System.currentTimeMillis() - start));
        }
        return null;
    }

    /**
     * 构造方法
     *
     * @param clientMgr 自定义的连接管理器
     */
    public CommonHttpClient(HttpClientMgr clientMgr)
    {
        this.clientMgr = clientMgr;
        this.httpClient = clientMgr.clientBuilder().build();
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonHttpClient.class);

    /**
     * 自定义的连接管理器
     */
    private final HttpClientMgr clientMgr;

    /**
     * 标准的连接对象
     */
    private final CloseableHttpClient httpClient;
}
