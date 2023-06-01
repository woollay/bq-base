package com.biuqu.errcode;

/**
 * 错误码常量(可继承扩展)
 * <pre>
 * 错误码由6位组成:
 * 1)1-2位表示服务模块
 * 2)3-4位表示业务模块
 * 3)5-6位表示业务状态类型
 *
 * @author BiuQu
 * @date 2023/1/5 19:55
 */
public enum ErrCodeEnum
{
    /**
     * 请求成功(10表示公共服务模块)
     */
    SUCCESS("100001"),

    /**
     * 签名错误(10表示公共服务模块)
     */
    SIGNATURE_ERROR("100002"),

    /**
     * 流量超限(10表示公共服务模块)
     */
    LIMIT_ERROR("100003"),

    /**
     * 认证失败(10表示公共服务模块)
     */
    AUTH_ERROR("100004"),

    /**
     * 参数校验失败(10表示公共服务模块)
     */
    VALID_ERROR("100005"),

    /**
     * 渠道异常(10表示公共服务模块)
     */
    CHANNEL_ERROR("100006"),

    /**
     * 添加数据异常(10表示公共服务模块)
     */
    ADD_ERROR("100007"),

    /**
     * 修改数据异常(10表示公共服务模块)
     */
    UPDATE_ERROR("100008"),

    /**
     * 删除数据异常(10表示公共服务模块)
     */
    DELETE_ERROR("100009"),

    /**
     * 内部错误(10表示公共服务模块)
     */
    SERVER_ERROR("100098"),

    /**
     * 请求失败(10表示公共服务模块)
     */
    FAILURE("100099");

    /**
     * 获取错误码code
     *
     * @return 错误码code
     */
    public String getCode()
    {
        return code;
    }

    ErrCodeEnum(String code)
    {
        this.code = code;
    }

    /**
     * 错误码code
     */
    private String code;
}
