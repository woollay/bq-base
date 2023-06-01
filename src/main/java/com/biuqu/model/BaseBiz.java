package com.biuqu.model;

import com.biuqu.constants.Const;
import com.biuqu.utils.IdUtil;
import com.biuqu.utils.JwtUtil;
import com.biuqu.utils.UrlUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 抽象的业务基类模型(所有业务接口均依赖此模型)
 *
 * @param <O> 业务的最终出参模型(outer)
 * @author BiuQu
 * @date 2023/2/4 14:24
 */
@Data
public abstract class BaseBiz<O> extends BaseSecurity
{
    /**
     * 模型初始化
     *
     * @param req 请求对象
     * @return true表示初始化成功
     */
    public boolean init(HttpServletRequest req)
    {
        JwtToken jwtToken = JwtUtil.getJwtToken(req.getHeader(HttpHeaders.AUTHORIZATION));
        if (null != jwtToken && !StringUtils.isEmpty(jwtToken.toClientId()))
        {
            this.setUserId(jwtToken.toClientId());

            if (StringUtils.isEmpty(this.reqId) && !StringUtils.isEmpty(req.getHeader(Const.HTTP_HEADERS_REQUEST)))
            {
                this.setReqId(req.getHeader(Const.HTTP_HEADERS_REQUEST));
            }
            if (StringUtils.isEmpty(this.reqId))
            {
                this.setReqId(IdUtil.uuid());
            }

            this.setUrl(UrlUtil.shortUrl(req.getRequestURI()));
            this.setRespId(IdUtil.uuid());
            return true;
        }
        return false;
    }

    /**
     * 添加全局业务配置(存在接口差异化，比如根据appId设置不同)
     *
     * @param configs 该业务配置的所有全局配置参数
     */
    public void appendConf(List<GlobalConfig> configs)
    {
        for (GlobalConfig config : configs)
        {
            //设置渠道id
            if (!config.isEmpty() && CHANNEL_ID.equalsIgnoreCase(config.getSvcId()))
            {
                if (!StringUtils.isEmpty(config.getSvcValue()))
                {
                    this.setChannelId(config.getSvcValue());
                }
            }
        }
    }

    /**
     * 把标准模型转换成出参模型(添加默认实现)
     *
     * @return 出参模型
     */
    public O toModel()
    {
        return null;
    }

    /**
     * 把标准模型转成调用第三方的入参模型
     *
     * @return 调用第三方的入参模型
     */
    public Object toRemote()
    {
        return null;
    }

    /**
     * 转换成指定的返回对象结果(单个outer)
     * <p>
     * TODO 远程调用必须要覆写
     *
     * @return 返回对象结果
     */
    public TypeReference<ResultCode<O>> toTypeRef()
    {
        TypeReference<ResultCode<O>> typeReference = new TypeReference<ResultCode<O>>()
        {
        };
        return typeReference;
    }

    /**
     * 转换成指定的返回对象结果(批量outer)
     * <p>
     * TODO 远程调用必须要覆写
     *
     * @return 返回对象结果
     */
    public TypeReference<ResultCode<List<O>>> toTypeRefs()
    {
        TypeReference<ResultCode<List<O>>> typeRefs = new TypeReference<ResultCode<List<O>>>()
        {
        };
        return typeRefs;
    }

    /**
     * config的渠道id
     */
    private static final String CHANNEL_ID = "client.to.channel";

    /**
     * 接口id
     */
    @JsonIgnore
    private String userId;

    /**
     * 渠道id
     */
    @JsonIgnore
    private String channelId;

    /**
     * 请求id
     */
    private String reqId;

    /**
     * 响应id
     */
    private String respId;

    /**
     * 访问的url
     */
    @JsonIgnore
    private String url;

    /**
     * 访问url对应的
     */
    @JsonIgnore
    private String urlId;
}
