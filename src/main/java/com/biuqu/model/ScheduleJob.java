package com.biuqu.model;

import lombok.Data;

/**
 * 定时任务对象
 *
 * @author BiuQu
 * @date 2023/1/3 15:05
 */
@Data
public class ScheduleJob
{
    /**
     * 任务的唯一ID
     */
    private String id;

    /**
     * 任务的CRON表达式
     */
    private String cron;

    /**
     * 任务是否为单实例运行
     */
    private boolean single;

    /**
     * 任务完成后的释放时间
     */
    private long releaseDelay;

    /**
     * 任务的最大存活时间
     */
    private long maxTime;
}
