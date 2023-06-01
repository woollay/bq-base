package com.biuqu.aop;

import com.biuqu.constants.Const;
import com.biuqu.errcode.ErrCodeEnum;
import com.biuqu.exception.CommonException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 切面基类
 *
 * @author BiuQu
 * @date 2023/1/17 21:47
 */
public abstract class BaseAop
{
    /**
     * 前置切面
     *
     * @param joinPoint 切面对象
     */
    public void before(JoinPoint joinPoint)
    {
        long start = System.currentTimeMillis();
        String methodName = getMethodName(joinPoint);
        try
        {
            this.doBefore(joinPoint);
        }
        catch (CommonException e)
        {
            if (ErrCodeEnum.LIMIT_ERROR.getCode().equalsIgnoreCase(e.getErrCode().getCode()))
            {
                throw e;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("invoke before aop[{}] failed:{}.", methodName, e);
        }
        finally
        {
            LOGGER.info("invoke before aop[{}] cost:{}ms.", methodName, System.currentTimeMillis() - start);
        }
    }

    /**
     * 环绕切面
     *
     * @param joinPoint 切面对象
     * @return 业务处理逻辑的结果
     */
    public Object around(ProceedingJoinPoint joinPoint)
    {
        Object result = null;
        long start = System.currentTimeMillis();
        String methodName = getMethodName(joinPoint);
        try
        {
            result = this.doAround(joinPoint);
        }
        catch (Exception e)
        {
            LOGGER.error("invoke around aop[{}] failed:{}.", methodName, e);
        }
        finally
        {
            LOGGER.info("invoke around aop[{}] cost:{}ms.", methodName, System.currentTimeMillis() - start);
        }
        return result;
    }

    /**
     * 后置切面
     *
     * @param joinPoint 切面对象
     * @param result    结果对象
     */
    public void after(JoinPoint joinPoint, Object result)
    {
        long start = System.currentTimeMillis();
        String methodName = getMethodName(joinPoint);
        try
        {
            this.doAfter(joinPoint, result);
        }
        catch (CommonException e)
        {
            if (e.getErrCode().getCode().equalsIgnoreCase(ErrCodeEnum.SIGNATURE_ERROR.getCode()))
            {
                throw e;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("invoke after aop[{}] failed:{}.", methodName, e);
        }
        finally
        {
            LOGGER.info("invoke after aop[{}] cost:{}ms.", methodName, System.currentTimeMillis() - start);
        }
    }

    /**
     * 前置切面(可覆写)
     *
     * @param joinPoint 切面对象
     */
    protected void doBefore(JoinPoint joinPoint)
    {
        this.doBefore(getMethod(joinPoint), joinPoint.getArgs());
    }

    /**
     * 前置切面(可覆写)
     *
     * @param method 方法对象
     * @param args   方法参数
     */
    protected void doBefore(Method method, Object[] args)
    {
    }

    /**
     * 环绕切面(可覆写)
     *
     * @param joinPoint 切面对象
     * @return 业务处理逻辑的结果
     */
    protected Object doAround(ProceedingJoinPoint joinPoint)
    {
        Object param = this.doAroundBefore(getMethod(joinPoint), joinPoint.getArgs());
        Object result;
        try
        {
            result = joinPoint.proceed();
            this.doAroundAfter(param, result);
        }
        catch (Throwable e)
        {
            LOGGER.error("invoke around aop[{}] failed:{}.", getMethodName(joinPoint), e);
            throw new CommonException(ErrCodeEnum.SERVER_ERROR.getCode());
        }
        return result;
    }

    /**
     * 做环绕切面的前置处理
     *
     * @param method 方法对象
     * @param args   方法参数
     * @return 前置处理的结果
     */
    protected Object doAroundBefore(Method method, Object[] args)
    {
        return null;
    }

    /**
     * 做环绕切面的后置处理
     *
     * @param param  方法对象
     * @param result 方法执行结果
     */
    protected void doAroundAfter(Object param, Object result)
    {
    }

    /**
     * 后置切面(可覆写)
     *
     * @param joinPoint 切面对象
     * @param result    结果对象
     */
    protected void doAfter(JoinPoint joinPoint, Object result)
    {
        this.doAfter(getMethod(joinPoint), joinPoint.getArgs(), result);
    }

    /**
     * 后置切面(可覆写)
     *
     * @param method 方法对象
     * @param args   方法参数
     * @param result 结果对象
     */
    protected void doAfter(Method method, Object[] args, Object result)
    {
    }

    /**
     * 获取到方法对象
     *
     * @param joinPoint 切面对象
     * @return 方法对象
     */
    protected final Method getMethod(JoinPoint joinPoint)
    {
        return ((MethodSignature)joinPoint.getSignature()).getMethod();
    }

    /**
     * 获取到方法名
     *
     * @param joinPoint 切面对象
     * @return 方法对象名
     */
    protected final String getMethodName(JoinPoint joinPoint)
    {
        Method method = getMethod(joinPoint);
        String className = method.getDeclaringClass().getSimpleName();
        return className + Const.POINT + method.getName();
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseAop.class);
}
