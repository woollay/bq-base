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
public final class PubJwtMgr extends BaseJwtMgr
{
    public PubJwtMgr(PubJwkMgrFacade jwkMgr)
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
    private final PubJwkMgrFacade jwkMgr;
}
