package com.biuqu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加密性忽略注解
 *
 * @author BiuQu
 * @date 2023/1/18 09:41
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EncryptionIgnoreAnn
{
}
