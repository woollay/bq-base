package com.biuqu.http;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 封装后的RestTemplate对象(使用apache的httpClient底层实现)
 *
 * @author BiuQu
 * @date 2023/1/4 11:37
 */
public class CommonRestTemplate extends RestTemplate
{
    /**
     * 获取http调用结果
     *
     * @param url     请求的url
     * @param headers header头集合
     * @param body    body json
     * @return 结果字符串
     */
    public String invoke(String url, List<Header> headers, String body)
    {
        HttpHeaders httpHeaders = HttpHelper.toHeaders(headers);
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        httpHeaders.setContentType(contentType);
        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);
        try
        {
            ResponseEntity<String> response = this.postForEntity(new URI(url), request, String.class);
            return HttpHelper.getResult(response);
        }
        catch (URISyntaxException e)
        {
            LOGGER.error("failed to invoke url[{}]", url, e);
        }
        return null;
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonRestTemplate.class);
}
