package com.biuqu.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 自定义线程池
 * <br>
 *
 * @author BiuQu
 * @date: 2019/10/24 11:29
 */
public final class CommonThreadPool
{
    /**
     * 获取单线程的线程池
     *
     * @param prefix 线程池前缀
     * @return 线程池对象
     */
    public static ExecutorService getSingleExecutor(String prefix)
    {
        return getExecutor(prefix, ONE, ONE);
    }

    /**
     * 获取线程池
     *
     * @param prefix 线程池前缀
     * @return 线程池对象
     */
    public static ExecutorService getExecutor(String prefix)
    {
        return getExecutor(prefix, DEFAULT_CORE_NUM, DEFAULT_MAX_NUM);
    }

    /**
     * 构建线程池
     *
     * @param prefix  线程池前缀
     * @param coreNum 核心线程数
     * @param maxNum  最大线程数
     * @return 线程池对象
     */
    public static ExecutorService getExecutor(String prefix, int coreNum, int maxNum)
    {
        CommonThreadFactory threadFactory = CommonThreadFactory.getThreadFactory(prefix);
        if (coreNum > maxNum || coreNum <= 0)
        {
            coreNum = DEFAULT_CORE_NUM;
            maxNum = DEFAULT_MAX_NUM;
        }
        return buildExecutor(threadFactory, prefix, coreNum, maxNum);
    }

    /**
     * 构建周期任务线程池
     *
     * @param prefix 线程池前缀
     * @return 线程池对象
     */
    public static ThreadPoolTaskScheduler getScheduleExecutor(String prefix)
    {
        return getScheduleExecutor(prefix, DEFAULT_MAX_NUM);
    }

    /**
     * 构建周期任务线程池
     *
     * @param prefix 线程池前缀
     * @param maxNum 最大线程数
     * @return 线程池对象
     */
    public static ThreadPoolTaskScheduler getScheduleExecutor(String prefix, int maxNum)
    {
        int max = Math.max(ONE, maxNum);
        return buildScheduleExecutor(CommonThreadFactory.getThreadFactory(prefix), prefix, max);
    }

    /**
     * 关闭线程池
     *
     * @param prefix 线程池名称
     */
    public static void shutdown(String prefix)
    {
        if (CACHE.containsKey(prefix))
        {
            ExecutorService executor = CACHE.remove(prefix);
            executor.shutdownNow();
        }
        else if (SCHEDULE_CACHE.containsKey(prefix))
        {
            ThreadPoolTaskScheduler scheduler = SCHEDULE_CACHE.remove(prefix);
            scheduler.shutdown();
        }
    }

    /**
     * 构建周期任务线程池
     *
     * @param tf     线程工厂
     * @param prefix 线程池前缀
     * @param max    最大线程数
     * @return 线程池对象
     */
    private static synchronized ThreadPoolTaskScheduler buildScheduleExecutor(ThreadFactory tf, String prefix, int max)
    {
        ThreadPoolTaskScheduler executor = SCHEDULE_CACHE.get(prefix);
        if (null == executor)
        {
            try
            {
                executor = new ThreadPoolTaskScheduler();
                executor.setThreadFactory(tf);
                executor.setRejectedExecutionHandler(REJECTED_HANDLER);
                executor.setPoolSize(max);
                executor.setThreadNamePrefix(prefix);
                executor.setAwaitTerminationMillis(DEFAULT_KEEP_ALIVE);
                executor.setWaitForTasksToCompleteOnShutdown(true);

                SCHEDULE_CACHE.put(prefix, executor);
                LOGGER.info("create a schedule pool[{}]: size:{},activeCount:{}.", Thread.currentThread().getName(),
                    executor.getPoolSize(), executor.getActiveCount());

                // 关闭事件的挂钩
                Runtime.getRuntime().addShutdownHook(new Thread(() ->
                {
                    LOGGER.info("AsyncProcessor shutting down.");
                    SCHEDULE_CACHE.get(prefix).shutdown();
                }));
            }
            catch (Exception e)
            {
                LOGGER.error("AsyncProcessor init error.", e);
            }
        }
        return executor;
    }

    /**
     * 构建线程池
     *
     * @param tf     线程工厂
     * @param prefix 线程池前缀
     * @param core   核心线程数
     * @param max    最大线程数
     * @return 线程池对象
     */
    private static synchronized ExecutorService buildExecutor(ThreadFactory tf, String prefix, int core, int max)
    {
        ExecutorService executor = CACHE.get(prefix);
        if (null == executor)
        {
            try
            {
                executor = new ThreadPoolExecutor(core, max, DEFAULT_KEEP_ALIVE, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(DEFAULT_SIZE), tf, REJECTED_HANDLER);
                CACHE.put(prefix, executor);
                final ThreadPoolExecutor instance = (ThreadPoolExecutor)executor;
                LOGGER.info("create a pool[{}]: size:{},completedTask:{},activeCount:{},Queue:{}",
                    Thread.currentThread().getName(), instance.getPoolSize(), instance.getCompletedTaskCount(),
                    instance.getActiveCount(), instance.getQueue().size());

                // 关闭事件的挂钩
                Runtime.getRuntime().addShutdownHook(new Thread(() ->
                {
                    LOGGER.info("AsyncProcessor shutting down.");
                    instance.shutdown();
                }));
            }
            catch (Exception e)
            {
                LOGGER.error("AsyncProcessor init error.", e);
            }
        }
        return executor;
    }

    /**
     * 私有构造
     */
    private CommonThreadPool()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonThreadPool.class);

    /**
     * 默认核心并发数<br>
     */
    private static final int DEFAULT_CORE_NUM = 50;

    /**
     * 默认最大并发数<br>
     */
    private static final int DEFAULT_MAX_NUM = DEFAULT_CORE_NUM * 4 + 1;

    /**
     * 默认队列大小
     */
    private static final int DEFAULT_SIZE = 500;

    /**
     * 单线程的线程池的线程数量
     */
    private static final int ONE = 1;

    /**
     * 默认线程空闲后存活时间
     */
    private static final long DEFAULT_KEEP_ALIVE = 60L;

    /**
     * 防止重复创建同种命名的线程池
     */
    private static final Map<String, ExecutorService> CACHE = new HashMap<>(16);

    /**
     * 防止重复创建同种命名的线程池(周期线程池)
     */
    private static final Map<String, ThreadPoolTaskScheduler> SCHEDULE_CACHE = new HashMap<>(16);

    /**
     * 线程池拒绝策略
     */
    private static final RejectedExecutionHandler REJECTED_HANDLER = new RejectedExecutionHandler()
    {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor exec)
        {
            LOGGER.info("rejected pool size:{},thread:{}", exec.getPoolSize(), Thread.currentThread().getName());
        }
    };
}
