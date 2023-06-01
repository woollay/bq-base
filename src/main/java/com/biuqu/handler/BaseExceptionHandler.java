package com.biuqu.handler;

import com.biuqu.errcode.ErrCodeMgr;
import com.biuqu.exception.CommonException;
import com.biuqu.model.ErrCode;
import com.biuqu.model.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象的异常处理器(全局异常处理的基类)
 * <p>
 * 注意:为了同时兼容spring-webmvc和spring-webflux两种模式而抽象
 *
 * @author BiuQu
 * @date 2023/1/7 18:35
 */
public abstract class BaseExceptionHandler
{
    /**
     * 标准的异常处理逻辑
     *
     * @param url 请求url
     * @param e   异常对象
     * @return 结果对象
     */
    public ResultCode<?> handle(String url, Exception e)
    {
        ErrCode error;
        if (e instanceof CommonException)
        {
            error = ((CommonException)e).getErrCode();
        }
        else
        {
            error = getByUrl(url, ErrCodeMgr.getServerErr().getCode());
            LOGGER.error("global rest exception.", e);
        }
        return ResultCode.error(error.getCode());
    }

    /**
     * 根据url获取错误码
     *
     * @param url  请求url
     * @param code 错误码code
     * @return 错误码对象
     */
    protected abstract ErrCode getByUrl(String url, String code);

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseExceptionHandler.class);
}
