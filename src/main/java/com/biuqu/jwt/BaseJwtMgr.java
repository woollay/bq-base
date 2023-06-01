package com.biuqu.jwt;

import com.biuqu.model.JwtToken;
import com.biuqu.utils.JwtUtil;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JwtToken管理器
 *
 * @author BiuQu
 * @date 2023/1/21 21:09
 */
public abstract class BaseJwtMgr
{
    /**
     * 校验Token是否合法
     *
     * @param jwt jwt base64串
     * @return true表示校验通过
     */
    public boolean valid(String jwt)
    {
        try
        {
            jwt = JwtUtil.getJwt(jwt);

            SignedJWT signedJwt = SignedJWT.parse(jwt);
            return valid(signedJwt);
        }
        catch (Exception e)
        {
            LOGGER.error("failed to valid jwt.", e);
        }
        return false;
    }

    /**
     * 校验Token是否合法
     *
     * @param signedJwt token对象
     * @return true表示校验通过
     */
    public boolean valid(SignedJWT signedJwt)
    {
        try
        {
            String kid = signedJwt.getHeader().getKeyID();
            JWK jwk = this.getPubJwk(kid);
            //校验公钥签名
            boolean verifyResult = signedJwt.verify(new RSASSAVerifier(jwk.toRSAKey()));
            if (verifyResult)
            {
                //校验是否合法(如：token过期等)
                DefaultJWTClaimsVerifier<SecurityContext> verifier = new DefaultJWTClaimsVerifier<>(null, null);
                verifier.verify(signedJwt.getJWTClaimsSet(), null);
                return true;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("failed to valid jwt.", e);
        }
        return false;
    }

    /**
     * 解析出合法的JwtToken对象
     *
     * @param jwt jwt base64
     * @return JwtToken对象
     */
    public JwtToken parse(String jwt)
    {
        try
        {
            if (valid(jwt))
            {
                return JwtUtil.getJwtToken(jwt);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("failed to valid jwt.", e);
        }
        return null;
    }

    /**
     * 根据kid获取JWK
     *
     * @param kid 秘钥id
     * @return 公钥对象
     */
    protected abstract JWK getPubJwk(String kid);

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseJwtMgr.class);
}
