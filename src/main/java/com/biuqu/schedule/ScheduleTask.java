package com.biuqu.schedule;

/**
 * 周期任务接口
 *
 * @author BiuQu
 * @date 2023/1/3 15:11
 */
public interface ScheduleTask
{
    /**
     * 执行任务
     *
     * @param key     任务KEY
     * @param maxTime 任务的最大存活时间
     * @return true表示任务执行成功
     */
    boolean execute(String key, long maxTime);

    /**
     * 释放任务
     *
     * @param key          任务KEY
     * @param releaseDelay 任务释放的延迟时间
     */
    void release(String key, long releaseDelay);

    /**
     * 执行真实的任务
     *
     * @param key 任务KEY
     */
    void doTask(String key);
}
