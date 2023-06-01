package com.biuqu.jwt;

import com.nimbusds.jose.jwk.JWK;
import lombok.extern.slf4j.Slf4j;

/**
 * JwtToken管理器
 *
 * @author BiuQu
 * @date 2023/1/21 21:09
 */
@Slf4j
public final class JwtMgr extends BaseJwtMgr
{
    public JwtMgr(JwkMgr jwkMgr)
    {
        this.jwkMgr = jwkMgr;
    }

    @Override
    protected JWK getPubJwk(String kid)
    {
        return this.jwkMgr.getPubJwk(kid);
    }

    /**
     * 秘钥管理器
     */
    private final JwkMgr jwkMgr;
}
