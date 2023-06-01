package com.biuqu.configure;

import com.biuqu.context.ApplicationContextHolder;
import com.biuqu.model.ScheduleJob;
import com.biuqu.schedule.ScheduleTask;
import com.biuqu.thread.CommonThreadPool;
import com.biuqu.thread.ScheduleRunnable;
import com.biuqu.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.List;

/**
 * 标准的任务定时器配置类
 *
 * @author BiuQu
 * @date 2023/1/3 19:11
 */
@Slf4j
public abstract class BaseScheduleConfigurer implements SchedulingConfigurer
{
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar)
    {
        ThreadPoolTaskScheduler taskScheduler = ApplicationContextHolder.getBean(SCHEDULE_NAME);
        taskRegistrar.setTaskScheduler(taskScheduler);

        List<ScheduleJob> jobs = this.getJobs();
        log.info("current jobs:{}", JsonUtil.toJson(jobs));
        for (ScheduleJob job : jobs)
        {
            String taskId = job.getId();
            if (ApplicationContextHolder.containsBean(taskId))
            {
                ScheduleTask task = ApplicationContextHolder.getBean(taskId);
                taskRegistrar.addCronTask(new ScheduleRunnable(job, task), job.getCron());
                log.info("Add schedule task[{}],with cron:{}", taskId, job.getCron());
            }
        }
    }

    @Bean(name = SCHEDULE_NAME, destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler buildScheduler()
    {
        return CommonThreadPool.getScheduleExecutor("taskScheduler", CORE_NUM);
    }

    /**
     * 获取配置的任务列表
     *
     * @return 任务列表
     */
    protected abstract List<ScheduleJob> getJobs();

    /**
     * 周期定时器的名称
     */
    private static final String SCHEDULE_NAME = "taskScheduler";

    /**
     * 核心线程数
     */
    private static final int CORE_NUM = 10;
}
