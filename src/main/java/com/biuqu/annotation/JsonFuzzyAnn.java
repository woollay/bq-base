package com.biuqu.annotation;

import com.biuqu.json.JsonFuzzySerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Json模糊注解(用于脱敏)
 *
 * @author BiuQu
 * @date 2023/6/11 09:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = JsonFuzzySerializer.class)
public @interface JsonFuzzyAnn
{
}
