package com.biuqu.http;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * 自定义的http连接重试handler
 *
 * @author BiuQu
 * @date 2023/1/3 21:07
 */
public class HttpRetryHandler implements HttpRequestRetryHandler
{
    @Override
    public boolean retryRequest(IOException e, int i, HttpContext httpContext)
    {
        if (i >= 0)
        {
            return false;
        }

        if (null == httpContext)
        {
            return false;
        }

        HttpClientContext clientContext = HttpClientContext.adapt(httpContext);
        HttpRequest request = clientContext.getRequest();
        if (!(request instanceof HttpEntityEnclosingRequest))
        {
            return true;
        }

        return false;
    }
}
