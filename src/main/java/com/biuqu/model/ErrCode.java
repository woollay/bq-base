package com.biuqu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 错误码对象
 *
 * @author BiuQu
 * @date 2023/1/3 16:12
 */
@Data
public class ErrCode
{
    /**
     * 构建错误码对象
     *
     * @param code 错误码code
     * @param msg  错误码msg
     * @return 错误码对象
     */
    public static ErrCode build(String code, String msg)
    {
        return build(code, msg, 0);
    }

    /**
     * 构建错误码对象
     *
     * @param code 错误码code
     * @param msg  错误码msg
     * @param type 错误码类型,0:标准的错误码;<0:内部错误码;>0:外包部错误码
     * @return 错误码对象
     */
    public static ErrCode build(String code, String msg, int type)
    {
        return new ErrCode(code, msg, type);
    }

    /**
     * 是否为标准错误码
     *
     * @return true表示为标准错误码
     */
    @JsonIgnore
    public boolean isStandard()
    {
        return this.type == 0;
    }

    /**
     * 是否为内部错误码
     *
     * @return true表示为内部错误码
     */
    @JsonIgnore
    public boolean isIn()
    {
        return this.type < 0;
    }

    /**
     * 是否为外部错误码
     *
     * @return true表示为外部错误码
     */
    @JsonIgnore
    public boolean isOut()
    {
        return this.type > 0;
    }

    private ErrCode(String code, String msg, int type)
    {
        this.code = code;
        this.msg = msg;
        this.type = type;
    }

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误内容
     */
    private String msg;

    /**
     * 错误详情
     */
    private String detail;

    /**
     * 错误码类型，仅限内部使用，不对外暴露
     * <p>
     * 0:表示标准的错误码
     * <0:表示内部错误码
     * >0:表示外包部错误码
     */
    @JsonIgnore
    private int type;
}
