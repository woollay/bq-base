package com.biuqu.annotation;

/**
 * 禁用用安全加密
 * <p>
 * 仅对有实现类的服务生效(如service实现生效,基于接口注入的mybatis dao层不生效)
 *
 * @author BiuQu
 * @date 2023/2/16 21:27
 */
public @interface DisableSecurityAnn
{
}
