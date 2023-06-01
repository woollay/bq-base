package com.biuqu.json;

/**
 * 打码json属性的序列化的具体实现
 *
 * @author BiuQu
 * @date 2023/1/4 15:24
 */
public class JsonMaskSerializer extends BaseJsonSerializer<String>
{
    @Override
    protected Object getNewValue(String key, String value)
    {
        return JsonRuleMgr.applyRule(key, value);
    }
}
