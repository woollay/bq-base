package com.biuqu.annotation;

import com.biuqu.json.JsonMaskSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Jackson针对对象的属性打码注解
 *
 * @author BiuQu
 * @date 2023/1/4 15:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = JsonMaskSerializer.class)
public @interface JsonMaskAnn
{
}
