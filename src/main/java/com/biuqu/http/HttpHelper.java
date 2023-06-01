package com.biuqu.http;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Http处理的辅助类
 *
 * @author BiuQu
 * @date 2023/1/4 08:14
 */
public final class HttpHelper
{
    /**
     * 解析结果字符串
     *
     * @param response http响应对象
     * @return 结果字符串
     */
    public static String getResult(HttpResponse response)
    {
        if (null != response)
        {
            int code = response.getStatusLine().getStatusCode();
            LOGGER.info("current http result code:{}", code);
            if (code == HttpStatus.SC_OK)
            {
                try
                {
                    return EntityUtils.toString(response.getEntity());
                }
                catch (IOException e)
                {
                    LOGGER.error("failed to get http result.", e);
                }
            }
        }
        return null;
    }

    /**
     * 解析结果字符串
     *
     * @param response http响应对象(兼容处理RestTemplate客户端)
     * @return 结果字符串
     */
    public static String getResult(ResponseEntity<String> response)
    {
        if (null != response)
        {
            int code = response.getStatusCode().value();
            LOGGER.info("current http result code:{}", code);
            if (code == HttpStatus.SC_OK)
            {
                return response.getBody();
            }
        }
        return null;
    }

    /**
     * 构建http post对象
     *
     * @param url     请求的url
     * @param headers header头集合
     * @param body    body对象
     * @param reqConf 请求参数
     * @return http post对象
     */
    public static HttpPost buildHttpPost(String url, List<Header> headers, HttpEntity body, RequestConfig reqConf)
    {
        try
        {
            HttpPost request = new HttpPost(new URIBuilder(url).build());
            if (!CollectionUtils.isEmpty(headers))
            {
                for (Header header : headers)
                {
                    request.addHeader(header);
                }
            }
            request.setConfig(reqConf);
            request.setEntity(body);
            return request;
        }
        catch (URISyntaxException e)
        {
            LOGGER.error("failed to get http request.", e);
        }
        return null;
    }

    /**
     * 获取body对象
     *
     * @param body body字符串
     * @return body对象
     */
    public static StringEntity toBody(String body)
    {
        if (!StringUtils.isEmpty(body))
        {
            StringEntity entity = new StringEntity(body, StandardCharsets.UTF_8);
            String contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8).toString();
            entity.setContentType(contentType);
            entity.setContentEncoding(StandardCharsets.UTF_8.toString());
            return entity;
        }
        return null;
    }

    /**
     * 转换成header对象集合(兼容RestTemplate客户端)
     *
     * @param headers header对象集合
     * @return header对象集合
     */
    public static HttpHeaders toHeaders(List<Header> headers)
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (!CollectionUtils.isEmpty(headers))
        {
            for (Header header : headers)
            {
                httpHeaders.add(header.getName(), header.getValue());
            }
        }
        return httpHeaders;
    }

    /**
     * 转换成header对象集合
     *
     * @param headers header对象集合
     * @return header对象集合
     */
    public static List<Header> toHeaders(Map<String, String> headers)
    {
        List<Header> pairs = Lists.newArrayList();
        for (Map.Entry<String, String> entry : headers.entrySet())
        {
            pairs.add(new BasicHeader(entry.getKey(), entry.getValue()));
        }
        return pairs;
    }

    /**
     * 转换成header对象集合
     *
     * @param headerJson header json
     * @return header对象集合
     */
    public static List<Header> toHeaders(String headerJson)
    {
        List<Header> headers = Lists.newArrayList();
        JSONObject json = new JSONObject(headerJson);
        if (null != json && !json.isEmpty())
        {
            for (String key : json.keySet())
            {
                Object value = json.get(key);
                if (value instanceof String)
                {
                    headers.add(new BasicHeader(key, value.toString()));
                }
            }
        }
        return headers;
    }

    /**
     * 获取地址栏参数(主要是Get请求)
     *
     * @param url    请求地址(不带参数)
     * @param params url后面的参数
     * @return 带参数的url
     */
    public static String toUrlParam(String url, Map<String, String> params)
    {
        StringBuilder builder = new StringBuilder(url);
        if (!CollectionUtils.isEmpty(params))
        {
            List<Header> headers = Lists.newArrayList();
            for (Map.Entry<String, String> entry : params.entrySet())
            {
                headers.add(new BasicHeader(entry.getKey(), entry.getValue()));
            }
            builder.append(URL_PARAM_SPLIT);
            try
            {
                builder.append(EntityUtils.toString(new UrlEncodedFormEntity(headers, StandardCharsets.UTF_8)));
            }
            catch (IOException e)
            {
                LOGGER.error("failed to get http param.", e);
            }
        }
        return builder.toString();
    }

    private HttpHelper()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    /**
     * 地址栏URL和参数的分界符
     */
    private static final String URL_PARAM_SPLIT = "?";
}
