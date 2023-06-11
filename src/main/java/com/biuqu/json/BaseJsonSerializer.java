package com.biuqu.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.Instant;

/**
 * Json属性序列化处理器
 *
 * @author BiuQu
 * @date 2023/1/4 14:59
 */
public abstract class BaseJsonSerializer<T> extends JsonSerializer<T>
{
    @Override
    public void serialize(T value, JsonGenerator jsonGen, SerializerProvider provider) throws IOException
    {
        String name = jsonGen.getOutputContext().getCurrentName();
        Object object = provider.getGenerator().currentValue();
        Object newValue = getNewValue(object, name, value);
        if (value instanceof String)
        {
            if (newValue instanceof String)
            {
                jsonGen.writeString(newValue.toString());
            }
        }
        else if (value instanceof Instant)
        {
            jsonGen.writeNumber(Long.parseLong(newValue + StringUtils.EMPTY));
        }
    }

    /**
     * 获取序列后的新值
     *
     * @param object 原始对象
     * @param key    键值对的key
     * @param value  键值对的value
     * @return 新值
     */
    protected abstract Object getNewValue(Object object, String key, T value);
}
