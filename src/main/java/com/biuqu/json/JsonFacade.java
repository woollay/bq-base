package com.biuqu.json;

import com.biuqu.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Json门面
 *
 * @author BiuQu
 * @date 2023/6/8 20:47
 */
@Slf4j
public class JsonFacade
{
    /**
     * 把json字符串转换成指定类型的对象
     *
     * @param json  json字符串
     * @param clazz 模型的class类型
     * @param <T>   模型类型
     * @return 业务模型实例
     */
    public <T> T toObject(String json, Class<T> clazz)
    {
        return JsonUtil.toObject(json, clazz);
    }

    /**
     * 把json字符串转换成指定类型的对象
     *
     * @param json  json字符串
     * @param clazz 模型的class类型
     * @param snake 是否下划线转驼峰
     * @param <T>   模型类型
     * @return 业务模型实例
     */
    public <T> T toObject(String json, Class<T> clazz, boolean snake)
    {
        return JsonUtil.toObject(json, clazz, snake);
    }

    /**
     * 把json字符串转换成指定类型的List集合
     *
     * @param json  json字符串
     * @param clazz 模型的class类型
     * @param <T>   模型类型
     * @return 业务模型实例集合
     */
    public <T> List<T> toList(String json, Class<T> clazz)
    {
        return JsonUtil.toList(json, clazz);
    }

    /**
     * 把json字符串转换成指定类型的List集合
     *
     * @param json  json字符串
     * @param clazz 模型的class类型
     * @param snake 是否下划线转驼峰
     * @param <T>   模型类型
     * @return 业务模型实例集合
     */
    public <T> List<T> toList(String json, Class<T> clazz, boolean snake)
    {
        return JsonUtil.toList(json, clazz, snake);
    }

    /**
     * 获取Map集合
     *
     * @param json   json字符串
     * @param kClazz 模型的key class类型
     * @param vClazz 模型的value class类型
     * @param <K>    模型的key类型
     * @param <V>    模型的value类型
     * @return 业务模型实例集合
     */
    public <K, V> Map<K, V> toMap(String json, Class<K> kClazz, Class<V> vClazz)
    {
        return JsonUtil.toMap(json, kClazz, vClazz);
    }

    /**
     * 获取Map集合
     *
     * @param json   json字符串
     * @param kClazz 模型的key class类型
     * @param vClazz 模型的value class类型
     * @param snake  是否下划线转驼峰
     * @param <K>    模型的key类型
     * @param <V>    模型的value类型
     * @return 业务模型实例集合
     */
    public <K, V> Map<K, V> toMap(String json, Class<K> kClazz, Class<V> vClazz, boolean snake)
    {
        return JsonUtil.toMap(json, kClazz, vClazz, snake);
    }

    /**
     * 把json字符串转换成指定复杂类型的对象(对象有多层嵌套)
     *
     * @param json    json字符串
     * @param typeRef 模型的依赖类型
     * @param <T>     模型类型
     * @return 业务模型实例集合
     */
    public <T> T toComplex(String json, TypeReference<T> typeRef)
    {
        return JsonUtil.toComplex(json, typeRef);
    }

    /**
     * 把json字符串转换成指定复杂类型的对象(对象有多层嵌套)
     *
     * @param json    json字符串
     * @param typeRef 模型的依赖类型
     * @param <T>     模型类型
     * @param snake   是否下划线转驼峰
     * @return 业务模型实例集合
     */
    public <T> T toComplex(String json, TypeReference<T> typeRef, boolean snake)
    {
        return JsonUtil.toComplex(json, typeRef, snake);
    }

    /**
     * 获取json字符串
     *
     * @param t   业务模型
     * @param <T> 模型类型
     * @return json字符串
     */
    public <T> String toJson(T t)
    {
        return this.toJson(t, false);
    }

    /**
     * 获取json字符串(开启时还做驼峰转换)
     *
     * @param t     业务模型
     * @param snake 是否支持驼峰转换
     * @param <T>   模型类型
     * @return json字符串
     */
    public <T> String toJson(T t, boolean snake)
    {
        return this.toJson(t, snake, false, false);
    }

    /**
     * 获取json字符串
     *
     * @param t      业务模型
     * @param snake  true表示支持驼峰转换
     * @param mask   true表示支持打码
     * @param ignore true表示支持忽略
     * @param <T>    模型类型
     * @return json字符串
     */
    public <T> String toJson(T t, boolean snake, boolean mask, boolean ignore)
    {
        Set<String> ignoreFields = Sets.newHashSet();
        if (ignore)
        {
            ignoreFields = ignoreMgr.getIgnores(t);
        }
        ObjectWriter writer = JsonMappers.getIgnoreWriter(ignoreFields, snake, mask);
        try
        {
            return writer.writeValueAsString(t);
        }
        catch (JsonProcessingException e)
        {
            log.error("parse json error.", e);
        }
        return null;
    }

    /**
     * 获取json字符串
     *
     * @param t   业务模型
     * @param <T> 模型类型
     * @return json字符串
     */
    public <T> String toIgnore(T t)
    {
        return this.toIgnore(t, false);
    }

    /**
     * 获取json字符串
     *
     * @param t     业务模型
     * @param snake 是否支持驼峰转换
     * @param <T>   模型类型
     * @return json字符串
     */
    public <T> String toIgnore(T t, boolean snake)
    {
        return this.toJson(t, snake, false, true);
    }

    /**
     * 获取json字符串(带打码)
     *
     * @param t   业务模型
     * @param <T> 模型类型
     * @return json字符串
     */
    public <T> String toMask(T t)
    {
        return this.toMask(t, false);
    }

    /**
     * 获取json字符串
     *
     * @param t     业务模型
     * @param snake 是否支持驼峰转换
     * @param <T>   模型类型
     * @return json字符串
     */
    public <T> String toMask(T t, boolean snake)
    {
        return this.toJson(t, snake, true, false);
    }

    public JsonFacade(JsonIgnoreMgr ignoreMgr)
    {
        this.ignoreMgr = ignoreMgr;
    }

    /**
     * json属性忽略管理器
     */
    private final JsonIgnoreMgr ignoreMgr;
}
