package com.biuqu.model;

import lombok.Data;

import java.util.Set;

/**
 * Json忽略集合
 *
 * @author BiuQu
 * @date 2023/6/8 19:32
 */
@Data
public class JsonIgnores
{
    /**
     * 带包名的class类型
     */
    private String clazz;

    /**
     * 忽略的属性列表
     */
    private Set<String> fields;
}
