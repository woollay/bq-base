package com.biuqu.model;

import lombok.Data;

/**
 * 业务Token
 *
 * @author BiuQu
 * @date 2023/1/21 17:07
 */
@Data
public class BizJwt
{
    /**
     * 标准的JwtToken
     */
    private String accessToken;

    /**
     * 刷新Token
     */
    private String refreshToken;

    /**
     * Token类型(如:Bearer等)
     */
    private String tokenType;

    /**
     * 唯一会标标识(Jwt ID)
     */
    private String jti;

    /**
     * 受众(Audience)
     */
    private String aud;

    /**
     * 过期时间(Expiration Time)
     */
    private long exp;
}
