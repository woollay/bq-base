package com.biuqu.json;

import com.biuqu.constants.Const;
import com.biuqu.context.ApplicationContextHolder;

/**
 * 打码json属性的序列化的具体实现
 *
 * @author BiuQu
 * @date 2023/1/4 15:24
 */
public class JsonMaskSerializer extends BaseJsonSerializer<String>
{
    @Override
    protected Object getNewValue(Object object, String key, String value)
    {
        if (ApplicationContextHolder.containsBean(Const.JSON_MASK_SVC))
        {
            JsonMaskMgr maskMgr = ApplicationContextHolder.getBean(Const.JSON_MASK_SVC);
            return maskMgr.applyRule(object.getClass().getName(), key, value);
        }
        else
        {
            return JsonRuleMgr.applyRule(key, value);
        }
    }
}
