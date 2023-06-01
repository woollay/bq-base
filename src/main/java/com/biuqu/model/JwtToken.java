package com.biuqu.model;

import com.biuqu.constants.Const;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * JwtToken内置参数
 *
 * @author BiuQu
 * @date 2023/1/21 14:52
 */
@Data
public class JwtToken
{
    /**
     * 返回唯一的客户id
     *
     * @return 客户id
     */
    public String toClientId()
    {
        if (!ArrayUtils.isEmpty(aud))
        {
            return aud[0];
        }
        return null;
    }

    /**
     * 是否是SDK渠道(即只是接口调用，不是人触发的，只需要记接口日志即可)
     *
     * @return true表示是SDK接口调用渠道
     */
    @JsonIgnore
    public boolean isSdk()
    {
        return JwtSourceType.SDK.name().equalsIgnoreCase(this.sourceType);
    }

    /**
     * 是否是刷新token
     *
     * @return true表示是
     */
    @JsonIgnore
    public boolean isRefresh()
    {
        return Const.JWT_TYPE_REFRESH.equalsIgnoreCase(jwtType);
    }

    /**
     * 主题(Subject)
     */
    private String sub;

    /**
     * 受众(Audience)
     */
    private String[] aud;

    /**
     * 发行人(Issuer)
     */
    private String iss = StringUtils.EMPTY;

    /**
     * 唯一会标标识(Jwt ID)
     */
    private String jti;

    /**
     * 过期时间(Expiration Time)
     */
    private long exp;

    /**
     * 签发时间(Issued At)
     */
    private long iat;

    /**
     * 生效的起始时间(Not Before)
     */
    private long nbf;

    /**
     * 来源类型(扩展字段,认证的终端为什么类型,如:接口服务/小程序/Web端等)
     */
    private String sourceType = JwtSourceType.SDK.name();

    /**
     * JwtToken类型(扩展字段,是正常的JwtToken还是只用于JwtToken刷新接口的刷新Token)
     */
    private String jwtType;

    /**
     * Token类型(如:Bearer等)
     */
    private String tokenType;

    /**
     * 资源列表
     */
    private String[] resources;
}
