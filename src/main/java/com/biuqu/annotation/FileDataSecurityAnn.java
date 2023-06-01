package com.biuqu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 文件内容安全注解(用于文件内容)
 *
 * @author BiuQu
 * @date 2023/1/18 09:39
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface FileDataSecurityAnn
{
    String[] value() default {};
}
