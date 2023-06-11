package com.biuqu.utils;

import com.biuqu.json.JsonMappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * json工具类
 *
 * @author BiuQu
 * @date 2023/1/4 18:06
 */
public final class JsonUtil
{
    /**
     * 把json字符串转换成指定类型的对象
     *
     * @param json  json字符串
     * @param clazz 模型的class类型
     * @param <T>   模型类型
     * @return 业务模型实例
     */
    public static <T> T toObject(String json, Class<T> clazz)
    {
        return toObject(json, clazz, false);
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
    public static <T> T toObject(String json, Class<T> clazz, boolean snake)
    {
        ObjectMapper mapper = JsonMappers.getMapper(snake);
        try
        {
            return mapper.readValue(json, clazz);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.error("parse object error.", e);
        }
        return null;
    }

    /**
     * 把json字符串转换成指定类型的List集合
     *
     * @param json  json字符串
     * @param clazz 模型的class类型
     * @param <T>   模型类型
     * @return 业务模型实例集合
     */
    public static <T> List<T> toList(String json, Class<T> clazz)
    {
        return toList(json, clazz, false);
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
    public static <T> List<T> toList(String json, Class<T> clazz, boolean snake)
    {
        ObjectMapper mapper = JsonMappers.getMapper(snake);
        JavaType type = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
        try
        {
            return mapper.readValue(json, type);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.error("parse list error.", e);
        }
        return null;
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
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClazz, Class<V> vClazz)
    {
        return toMap(json, kClazz, vClazz, false);
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
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClazz, Class<V> vClazz, boolean snake)
    {
        ObjectMapper mapper = JsonMappers.getMapper(snake);
        JavaType type = mapper.getTypeFactory().constructParametricType(Map.class, kClazz, vClazz);
        try
        {
            return mapper.readValue(json, type);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.error("parse map error.", e);
        }
        return null;
    }

    /**
     * 把json字符串转换成指定复杂类型的对象(对象有多层嵌套)
     *
     * @param json    json字符串
     * @param typeRef 模型的依赖类型
     * @param <T>     模型类型
     * @return 业务模型实例集合
     */
    public static <T> T toComplex(String json, TypeReference<T> typeRef)
    {
        return toComplex(json, typeRef, false);
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
    public static <T> T toComplex(String json, TypeReference<T> typeRef, boolean snake)
    {
        ObjectMapper mapper = JsonMappers.getMapper(snake);
        try
        {
            return mapper.readValue(json, typeRef);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.error("parse snake complex error.", e);
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
    public static <T> String toJson(T t)
    {
        return toJson(t, false);
    }

    /**
     * 获取json字符串
     *
     * @param t     业务模型
     * @param snake 是否支持驼峰转换
     * @param <T>   模型类型
     * @return json字符串
     */
    public static <T> String toJson(T t, boolean snake)
    {
        ObjectWriter writer = JsonMappers.getMapper(snake).writer();
        try
        {
            return writer.writeValueAsString(t);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.error("parse json error.", e);
        }
        return null;
    }

    /**
     * 获取带忽略属性列表的json字符串
     *
     * @param t            业务模型
     * @param ignoreFields 模型中待忽略的属性列表
     * @param <T>          模型类型
     * @return json字符串
     */
    public static <T> String toJson(T t, Set<String> ignoreFields)
    {
        return toJson(t, ignoreFields, false);
    }

    /**
     * 获取带忽略属性列表的json字符串
     *
     * @param t            业务模型
     * @param ignoreFields 模型中待忽略的属性列表
     * @param snake        是否支持驼峰转换
     * @param <T>          模型类型
     * @return json字符串
     */
    public static <T> String toJson(T t, Set<String> ignoreFields, boolean snake)
    {
        ObjectWriter writer = JsonMappers.getIgnoreWriter(ignoreFields, snake, false);
        try
        {
            return writer.writeValueAsString(t);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.error("parse json error.", e);
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
    public static <T> String toMask(T t)
    {
        return toMask(t, false);
    }

    /**
     * 获取json字符串
     *
     * @param t     业务模型
     * @param snake 是否支持驼峰转换
     * @param <T>   模型类型
     * @return json字符串
     */
    public static <T> String toMask(T t, boolean snake)
    {
        return toMask(t, null, snake);
    }

    /**
     * 获取带忽略属性列表的json字符串
     *
     * @param t            业务模型
     * @param ignoreFields 模型中待忽略的属性列表
     * @param <T>          模型类型
     * @return json字符串
     */
    public static <T> String toMask(T t, Set<String> ignoreFields)
    {
        return toMask(t, ignoreFields, false);
    }

    /**
     * 获取带忽略属性列表的json字符串
     *
     * @param t            业务模型
     * @param ignoreFields 模型中待忽略的属性列表
     * @param snake        是否支持驼峰转换
     * @param <T>          模型类型
     * @return json字符串
     */
    public static <T> String toMask(T t, Set<String> ignoreFields, boolean snake)
    {
        ObjectWriter writer = JsonMappers.getIgnoreWriter(ignoreFields, snake, true);
        try
        {
            return writer.writeValueAsString(t);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.error("parse json error.", e);
        }
        return null;
    }

    private JsonUtil()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
}
