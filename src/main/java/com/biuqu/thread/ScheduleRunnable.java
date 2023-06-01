package com.biuqu.thread;

import com.biuqu.model.ScheduleJob;
import com.biuqu.schedule.ScheduleTask;
import lombok.extern.slf4j.Slf4j;

/**
 * 周期任务线程的runnable对象
 *
 * @author BiuQu
 * @date 2023/1/3 15:55
 */
@Slf4j
public class ScheduleRunnable implements Runnable
{
    /**
     * 构造方法
     *
     * @param job  任务配置对象
     * @param task 任务执行器
     */
    public ScheduleRunnable(ScheduleJob job, ScheduleTask task)
    {
        this.job = job;
        this.task = task;
    }

    @Override
    public void run()
    {
        long start = System.currentTimeMillis();
        String key = this.job.getId() + this.job.getCron();
        boolean result = false;
        try
        {
            if (this.job.isSingle())
            {
                result = this.task.execute(key, this.job.getMaxTime());
            }
            else
            {
                this.task.doTask(key);
            }
        }
        catch (Exception e)
        {
            log.error("execute schedule task[{}] error.", key, e);
        }
        finally
        {
            if (result)
            {
                this.task.release(key, this.job.getReleaseDelay());
            }
            log.info("execute schedule task[{}] cost:{}ms.", key, System.currentTimeMillis() - start);
        }
    }

    /**
     * 任务配置
     */
    private ScheduleJob job;

    /**
     * 任务执行器
     */
    private ScheduleTask task;
}
