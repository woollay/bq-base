package com.biuqu.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.spi.MDCAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂
 * <br>
 *
 * @author BiuQu
 * @date: 2019/10/24 15:48
 */
public class CommonThreadFactory implements ThreadFactory
{
    /**
     * 线程工厂
     *
     * @param poolPrefix 线程池名前缀
     * @return 带缓存的线程工厂
     */
    public static synchronized CommonThreadFactory getThreadFactory(String poolPrefix)
    {
        CommonThreadFactory factory;
        if (CACHE.containsKey(poolPrefix))
        {
            factory = CACHE.get(poolPrefix);
        }
        else
        {
            factory = new CommonThreadFactory(poolPrefix);
            CACHE.put(poolPrefix, factory);
        }
        return factory;
    }

    @Override
    public Thread newThread(Runnable r)
    {
        //获取主线程的链路信息
        MDCAdapter mdc = MDC.getMDCAdapter();
        Map<String, String> map = mdc.getCopyOfContextMap();
        Thread t = new Thread(r, this.poolPrefix + "-thread-" + THREAD_ID.getAndIncrement())
        {
            @Override
            public void run()
            {
                try
                {
                    //把链路追踪设置到线程池中的线程
                    if (null != map)
                    {
                        MDC.getMDCAdapter().setContextMap(map);
                    }
                    super.run();
                }
                finally
                {
                    //使用完毕后，清理缓存，避免内存溢出
                    MDC.clear();
                }
            }
        };
        return t;
    }

    static
    {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException(Thread t, Throwable e)
            {
                LOGGER.error("Current rejected thread is {},error:{}", t.getName(), e);
            }
        });
    }

    /**
     * 私有化构造方法
     *
     * @param poolPrefix 线程池前缀
     */
    private CommonThreadFactory(String poolPrefix)
    {
        this.poolPrefix = poolPrefix;
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonThreadFactory.class);

    /**
     * 防止创建同种命名的线程工厂
     */
    private static final Map<String, CommonThreadFactory> CACHE = new HashMap<>(16);

    /**
     * 线程ID
     */
    private final AtomicInteger THREAD_ID = new AtomicInteger(1);

    /**
     * 线程池前缀
     */
    private String poolPrefix;
}

