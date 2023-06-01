package com.biuqu.json;

import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * 忽略的属性配置
 *
 * @author BiuQu
 * @date 2023/1/4 17:34
 */
@JsonFilter(JsonMappers.IGNORE_ID)
public class JsonIgnoreField
{
}
