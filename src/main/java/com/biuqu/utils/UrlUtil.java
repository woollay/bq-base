package com.biuqu.utils;

import com.biuqu.constants.Const;
import org.apache.commons.lang3.StringUtils;

/**
 * Url工具类
 *
 * @author BiuQu
 * @date 2023/2/5 02:27
 */
public final class UrlUtil
{
    /**
     * 获取短的url
     *
     * @param url url全路径
     * @return url短路径(去掉 ' ? ' 及后面部分)
     */
    public static String shortUrl(String url)
    {
        String shortUrl = url;
        if (!StringUtils.isEmpty(url))
        {
            if (url.contains(Const.ASK))
            {
                shortUrl = url.substring(0, url.indexOf(Const.ASK));
            }
        }
        return shortUrl;
    }

    /**
     * 替换请求url 替换"?"前面的，保留"?"后面的
     *
     * @param url         原始请求url
     * @param newShortUrl 新的请求url
     * @return 新的url
     */
    public static String changePrefix(String url, String newShortUrl)
    {
        //如果新的url不合法则还是保留旧的url
        if (StringUtils.isEmpty(newShortUrl))
        {
            return url;
        }
        if (StringUtils.isEmpty(url))
        {
            return newShortUrl;
        }

        String shortUrl = shortUrl(url);
        return newShortUrl + url.replace(shortUrl, StringUtils.EMPTY);
    }

    private UrlUtil()
    {
    }
}
