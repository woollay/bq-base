package com.biuqu.model;

import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础的业务入参模型
 * <p>
 * //TODO 后续给业务参数加上hibernate validator校验注解
 *
 * @author BiuQu
 * @date 2023/2/4 15:07
 */
@Data
public abstract class BaseBizInner<T extends BaseBiz>
{
    /**
     * 转换成微服务需要的标准业务模型
     *
     * @return 通用业务模型
     */
    public T toModel()
    {
        T model = this.genModel();
        model.setStart(System.currentTimeMillis());
        model.setReqId(this.reqId);

        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        boolean success = model.init(req);
        if (!success)
        {
            return null;
        }
        return model;
    }

    /**
     * 生成标准的业务模型
     *
     * @return 标准业务模型
     */
    protected abstract T genModel();

    /**
     * 请求id
     */
    private String reqId;
}
