package com.biuqu.json;

import java.time.Instant;

/**
 * Instant时间序列化(序列化成毫秒数)
 *
 * @author BiuQu
 * @date 2023/2/19 21:33
 */
public class JsonInstantSerializer extends BaseJsonSerializer<Instant>
{
    @Override
    protected Object getNewValue(Object object, String key, Instant value)
    {
        return value.toEpochMilli();
    }
}
