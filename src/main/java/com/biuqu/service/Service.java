package com.biuqu.service;

import java.util.List;

/**
 * 业务服务接口
 *
 * @author BiuQu
 * @date 2023/1/21 22:21
 */
public interface Service<T>
{
    /**
     * 插入数据
     *
     * @param model 业务参数模型
     * @return 插入成功的数量(一般不关注)
     */
    int add(T model);

    /**
     * 查询数据
     *
     * @param model 业务参数模型
     * @return 获取单个业务模型数据
     */
    T get(T model);

    /**
     * 查询批量数据
     *
     * @param model 业务参数模型
     * @return 获取多个业务模型数据
     */
    List<T> getBatch(T model);

    /**
     * 查询批量数据
     *
     * @param batch 多个业务参数模型
     * @return 获取多个业务模型数据
     */
    List<T> batchGet(List<T> batch);

    /**
     * 更新数据
     *
     * @param model 业务参数模型
     * @return 变更的业务模型数据的数量
     */
    int update(T model);

    /**
     * 删除数据
     *
     * @param model 业务参数模型
     * @return 变更的业务模型数据的数量
     */
    int delete(T model);
}
