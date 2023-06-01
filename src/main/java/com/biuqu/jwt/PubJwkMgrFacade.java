package com.biuqu.jwt;

import com.nimbusds.jose.jwk.JWK;

/**
 * 公钥管理器门面
 *
 * @author BiuQu
 * @date 2023/3/15 08:20
 */
public final class PubJwkMgrFacade
{
    public PubJwkMgrFacade(JwkMgr localJwkMgr)
    {
        this.localJwkMgr = localJwkMgr;
    }

    /**
     * 追加远程调用获取的秘钥管理对象
     *
     * @param remoteJwkMgr 远程调用获取的秘钥管理对象
     */
    public void append(PubJwkMgr remoteJwkMgr)
    {
        this.remoteJwkMgr = remoteJwkMgr;
    }

    /**
     * 获取公钥对象
     * <p>
     * 先用远程获取的，不成功则用本地的
     *
     * @return 公钥对象
     */
    public JWK getPubJwk(String kid)
    {
        JWK jwk = null;
        if (null != remoteJwkMgr)
        {
            jwk = remoteJwkMgr.getPubJwk(kid);
        }
        if (null == jwk)
        {
            jwk = localJwkMgr.getPubJwk(kid);
        }
        return jwk;
    }

    /**
     * 通过网络获取的公钥管理器
     */
    private PubJwkMgr remoteJwkMgr;

    /**
     * 通过本地获取的公钥管理器
     */
    private final JwkMgr localJwkMgr;
}
