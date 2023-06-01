package com.biuqu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 完整性注解
 *
 * @author BiuQu
 * @date 2023/1/4 15:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface IntegritySecurityAnn
{
}
