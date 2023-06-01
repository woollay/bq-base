package com.biuqu.model;

import lombok.Data;

/**
 * Jwt结果
 * <p>
 * 封装后的对外结果对象
 *
 * @author BiuQu
 * @date 2023/2/21 14:35
 */
@Data
public class JwtResult
{
    /**
     * jwt token base64,内容对应{@link JwtToken}
     */
    private String accessToken;

    /**
     * token类型(如:bearer)
     */
    private String tokenType;

    /**
     * 限定的使用范围
     */
    private String scope;

    /**
     * refresh jwt token base64,内容对应{@link JwtToken}
     */
    private String refreshToken;

    /**
     * 客户唯一标识(扩展字段，对应{@link JwtToken#getAud()})
     */
    private String clientId;

    /**
     * jwt token唯一标识(对应{@link JwtToken#getJti()})
     */
    private String jti;

    /**
     * 资源列表(扩展字段)
     */
    private String[] resources;

    /**
     * 过期时长(s,对应{@link JwtToken#getExp()})
     */
    private long expiresIn;

}
