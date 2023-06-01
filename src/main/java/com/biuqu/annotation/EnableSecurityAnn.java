package com.biuqu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用加密安全策略
 * <p>
 * 仅对有实现类的服务生效(如service实现生效,基于接口注入的mybatis dao层不生效)
 *
 * @author BiuQu
 * @date 2023/2/7 22:41
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface EnableSecurityAnn
{
}
