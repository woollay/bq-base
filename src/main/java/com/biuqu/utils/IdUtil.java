package com.biuqu.utils;

import com.biuqu.constants.Const;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Id工具类
 *
 * @author BiuQu
 * @date 2023/1/21 22:15
 */
public final class IdUtil
{
    /**
     * 生成不带中划线的uuid(有些场景下不能带,比如obs的key)
     *
     * @return 随机数
     */
    public static String uuid()
    {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll(Const.MID_LINK, StringUtils.EMPTY);
    }

    private IdUtil()
    {
    }
}
