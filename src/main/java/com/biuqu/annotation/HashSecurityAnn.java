package com.biuqu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Hash注解(不可逆)
 *
 * @author BiuQu
 * @date 2023/1/18 09:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface HashSecurityAnn
{
}
