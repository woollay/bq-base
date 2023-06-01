package com.biuqu.utils;

import com.biuqu.constants.Const;
import com.biuqu.model.JwtToken;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/**
 * Jwt处理工具类
 *
 * @author BiuQu
 * @date 2023/1/21 21:56
 */
public final class JwtUtil
{
    /**
     * 是否是Bearer类型的jwt token
     *
     * @param jwt jwt token json
     * @return true表示是bearer类型的jwt
     */
    public static boolean isBearer(String jwt)
    {
        return !StringUtils.isEmpty(jwt) && jwt.startsWith(Const.JWT_BEARER_TYPE);
    }

    /**
     * 去掉Bearer类型的jwt token
     *
     * @param jwt jwt token json
     * @return 没有bearer头的base64 jwt token
     */
    public static String getJwt(String jwt)
    {
        if (isBearer(jwt))
        {
            jwt = jwt.substring(Const.JWT_BEARER_TYPE.length()).trim();
        }
        return jwt;
    }

    /**
     * 解析出JwtToken对象
     * <p>
     * 如果需要同时判断jwt的合法性，请调用{@link com.biuqu.jwt.JwtMgr#parse(String)}
     *
     * @param jwt jwt base64串
     * @return JwtToken对象
     */
    public static JwtToken getJwtToken(String jwt)
    {
        jwt = getJwt(jwt);
        try
        {
            SignedJWT signedJwt = SignedJWT.parse(jwt);
            String json = JsonUtil.toJson(signedJwt.getJWTClaimsSet().getClaims());
            JwtToken jwtToken = JsonUtil.toObject(json, JwtToken.class, true);
            return jwtToken;
        }
        catch (ParseException e)
        {
            LOGGER.error("failed to parse jwt token.", e);
        }
        return null;
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
}
