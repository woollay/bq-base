package com.biuqu.model;

import lombok.Data;

import java.util.List;

/**
 * Json打码配置
 *
 * @author BiuQu
 * @date 2023/6/9 16:57
 */
@Data
public class JsonMask
{
    /**
     * 模型全路径类名
     */
    private String clazz;

    /**
     * 规则列表
     */
    List<JsonRule> rules;
}
