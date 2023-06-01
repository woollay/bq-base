package com.biuqu.exception;

import com.biuqu.errcode.ErrCodeMgr;
import com.biuqu.model.ErrCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 标准异常
 *
 * @author BiuQu
 * @date 2023/1/3 16:07
 */
public class CommonException extends RuntimeException
{
    /**
     * 默认构造方法
     */
    public CommonException()
    {
    }

    /**
     * 默认构造方法
     *
     * @param code 错误码
     */
    public CommonException(String code)
    {
        this.errCode = ErrCodeMgr.get(code);
    }

    /**
     * 带错误码的构造方法
     *
     * @param code 错误码
     * @param msg  错误描述
     */
    public CommonException(String code, String msg)
    {
        this.errCode = ErrCodeMgr.get(code);
        if (!StringUtils.isEmpty(msg))
        {
            this.errCode.setMsg(msg);
        }
    }

    /**
     * 获取错误码对象
     *
     * @return 错误码对象
     */
    public ErrCode getErrCode()
    {
        return errCode;
    }

    /**
     * 错误码对象
     */
    private ErrCode errCode;
}
