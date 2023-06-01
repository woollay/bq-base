package com.biuqu.jwt;

import com.biuqu.encryption.BaseSingleSignature;
import com.biuqu.encryption.factory.EncryptionFactory;
import com.biuqu.model.Channel;
import com.biuqu.utils.IdUtil;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import org.bouncycastle.util.encoders.Hex;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * JwtToken对应的认证秘钥管理器(Json Web Keys)
 * <p>
 * 支持本地读取公私钥生成JWK(JWK可用于生成、校验jwt)
 * 也支持本地仅读取公钥生成JWK(JWK只可用于校验jwt)
 *
 * @author BiuQu
 * @date 2023/1/21 20:13
 */
public final class JwkMgr
{
    public JwkMgr(Channel channel)
    {
        this.channel = channel;
        byte[] pubBytes = Hex.decode(this.channel.getPubKey());
        this.pubJwk = genRsaKey(null, pubBytes, this.channel.getServiceId());
        if (null != this.channel.getPriKey())
        {
            byte[] priBytes = Hex.decode(this.channel.getPriKey());
            this.priJwk = genRsaKey(priBytes, pubBytes, this.channel.getServiceId());
        }
    }

    /**
     * 生成标准的JWK对象
     *
     * @return JWK秘钥对象
     */
    public JWK getJwk()
    {
        return this.priJwk;
    }

    /**
     * 生成只有公钥的JWK对象
     *
     * @return JWK秘钥对象
     */
    public JWK getPubJwk()
    {
        return this.pubJwk;
    }

    /**
     * 生成只有公钥的JWK对象
     *
     * @param kid 随机盐值
     * @return JWK秘钥对象
     */
    public JWK getPubJwk(String kid)
    {
        byte[] pubBytes = Hex.decode(this.channel.getPubKey());
        return genRsaKey(null, pubBytes, kid);
    }

    /**
     * 生成JWK对象
     *
     * @param priKey 私钥
     * @param pubKey 公钥
     * @return JWK秘钥对象
     */
    public static JWK getJwk(byte[] priKey, byte[] pubKey)
    {
        return genRsaKey(priKey, pubKey, null);
    }

    /**
     * 生成只有公钥的JWK对象
     *
     * @param pubKey 公钥
     * @param kid    随机盐值(每个token都带有)
     * @return JWK秘钥对象
     */
    public static JWK getPubJwk(byte[] pubKey, String kid)
    {
        return genRsaKey(null, pubKey, kid);
    }

    /**
     * 生成JWK对象
     *
     * @param priKey 私钥(非必传时，表示仅需公钥验证)
     * @param pubKey 公钥
     * @param kid    秘钥id(可重新设置，重启后对所有客户端生效)
     * @return JWK秘钥对象
     */
    private static RSAKey genRsaKey(byte[] priKey, byte[] pubKey, String kid)
    {
        RSAPublicKey rsaKey = (RSAPublicKey)ENCRYPTION.toPubKey(pubKey);
        RSAKey.Builder builder = new RSAKey.Builder(rsaKey);
        if (null != priKey)
        {
            PrivateKey rsaPriKey = ENCRYPTION.toPriKey(priKey);
            builder.privateKey(rsaPriKey);
        }
        if (null == kid)
        {
            kid = IdUtil.uuid();
        }
        return builder.keyID(kid).build();
    }

    /**
     * 加密算法
     */
    private final static BaseSingleSignature ENCRYPTION = EncryptionFactory.RSA.createAlgorithm();

    /**
     * 秘钥配置
     */
    private final Channel channel;

    /**
     * 公钥JWK
     */
    private final JWK pubJwk;

    /**
     * 私钥JWK
     */
    private JWK priJwk;
}
