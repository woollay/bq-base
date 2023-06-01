package com.biuqu.model;

import com.biuqu.errcode.ErrCodeEnum;
import com.biuqu.errcode.ErrCodeMgr;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * 结果对象
 *
 * @author BiuQu
 * @date 2023/1/5 19:38
 */
@Data
public final class ResultCode<T> implements Serializable
{
    /**
     * 构建成功的结果对象(默认为标准的中文错误码)
     *
     * @param data 业务数据
     * @param <T>  业务数据类型
     * @return 结果对象
     */
    public static <T> ResultCode<T> ok(T data)
    {
        return build(ErrCodeEnum.SUCCESS.getCode(), data);
    }

    /**
     * 构建结果对象(默认为标准的中文错误码)
     *
     * @param code 错误码code
     * @param data 业务数据
     * @param <T>  业务数据类型
     * @return 结果对象
     */
    public static <T> ResultCode<T> build(String code, T data)
    {
        return build(ErrCodeMgr.get(code), data);
    }

    /**
     * 构建结果对象(默认为中文的错误码，可兼容支持内部错误码、标准错误码和外部错误码)
     *
     * @param errCode 错误码对象
     * @param <T>     业务数据类型
     * @return 结果对象
     */
    public static <T> ResultCode<T> build(ErrCode errCode)
    {
        return build(errCode, null);
    }

    /**
     * 构建结果对象(默认为中文的错误码，可兼容支持内部错误码、标准错误码和外部错误码)
     *
     * @param errCode 错误码对象
     * @param data    业务数据
     * @param <T>     业务数据类型
     * @return 结果对象
     */
    public static <T> ResultCode<T> build(ErrCode errCode, T data)
    {
        ResultCode<T> resultCode = new ResultCode<>();
        resultCode.setCode(errCode.getCode());
        resultCode.setMsg(errCode.getMsg());
        resultCode.setData(data);
        return resultCode;
    }

    /**
     * 构建失败的结果对象
     *
     * @param code 错误码code
     * @param <T>  业务数据类型
     * @return 结果对象
     */
    public static <T> ResultCode<T> error(String code)
    {
        return build(ErrCodeMgr.get(code));
    }

    public ResultCode()
    {
    }

    /**
     * 是否成功
     *
     * @return true表示成功
     */
    public boolean ok()
    {
        return ErrCodeEnum.SUCCESS.getCode().equalsIgnoreCase(this.code);
    }

    /**
     * 请求id
     */
    private String reqId;

    /**
     * 响应id
     */
    private String respId;

    /**
     * 渠道id
     */
    @JsonIgnore
    private String channelId;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误描述
     */
    @JsonAlias({"message"})
    private String msg;

    /**
     * 业务数据
     */
    private T data;

    /**
     * 耗时
     */
    private long cost;
}
