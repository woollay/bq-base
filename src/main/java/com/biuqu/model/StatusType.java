package com.biuqu.model;

/**
 * 状态类型
 *
 * @author BiuQu
 * @date 2023/2/18 12:13
 */
public enum StatusType
{
    /**
     * 正常
     */
    ENABLE(1),

    /**
     * 不可用
     */
    DISABLE(2),

    /**
     * 删除态(软删除)
     */
    DELETE(3);

    /**
     * 状态是否正常
     *
     * @param status 状态值
     * @return true表示状态正常
     */
    public static boolean enable(int status)
    {
        return ENABLE.status == status;
    }

    public int getStatus()
    {
        return status;
    }

    /**
     * 状态
     */
    private int status;

    StatusType(int status)
    {
        this.status = status;
    }
}
