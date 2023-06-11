package com.biuqu.json;

import com.biuqu.annotation.JsonFuzzyAnn;
import com.biuqu.annotation.JsonMaskAnn;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.lang.annotation.Annotation;

/**
 * Jackson的自定义注解处理器
 *
 * @author BiuQu
 * @date 2023/1/4 14:51
 */
public class JsonDisableAnnIntrospector extends JacksonAnnotationIntrospector
{
    @Override
    public boolean isAnnotationBundle(Annotation ann)
    {
        //屏蔽注解
        if (ann instanceof JsonMaskAnn || ann instanceof JsonFuzzyAnn)
        {
            return false;
        }
        return super.isAnnotationBundle(ann);
    }
}
