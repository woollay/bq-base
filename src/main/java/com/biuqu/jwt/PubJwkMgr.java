package com.biuqu.jwt;

import com.google.common.collect.Maps;
import com.nimbusds.jose.jwk.JWK;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 简易的只有公有JWK的秘钥管理器
 * <p>
 * 仅支持调用OAuth服务生成校验jwt的JWK
 *
 * @author BiuQu
 * @date 2023/3/15 00:34
 */
@Slf4j
public final class PubJwkMgr
{
    public PubJwkMgr(List<JWK> batchJwk)
    {
        for (JWK jwk : batchJwk)
        {
            BATCH_PUB_JWK.put(jwk.getKeyID(), jwk);
        }
    }

    /**
     * 获取公钥对象
     *
     * @return 公钥对象
     */
    public JWK getPubJwk(String kid)
    {
        return BATCH_PUB_JWK.get(kid);
    }

    /**
     * 公钥jwk
     */
    private final Map<String, JWK> BATCH_PUB_JWK = Maps.newHashMap();
}
