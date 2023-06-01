package com.biuqu.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.Set;

/**
 * Json Mapper转换器
 *
 * @author BiuQu
 * @date 2023/1/4 15:38
 */
public final class JsonMappers
{
    /**
     * 获取驼峰转换的Mapper对象
     *
     * @return Mapper对象
     */
    public static ObjectMapper getMapper()
    {
        return MAPPER;
    }

    /**
     * 获取驼峰转换的Mapper对象
     *
     * @param snake 是否兼容驼峰和下划线转换
     * @return Mapper对象
     */
    public static ObjectMapper getMapper(boolean snake)
    {
        if (snake)
        {
            return SNAKE_MAPPER;
        }
        return getMapper();
    }

    /**
     * 获取带忽略属性的Mapper对象
     *
     * @param ignoreFields 忽略的属性列表
     * @param mask         是否打码
     * @param snake        是否兼容驼峰和下划线转换
     * @return Mapper对象的新Writer对象
     */
    public static ObjectWriter getIgnoreWriter(Set<String> ignoreFields, boolean mask, boolean snake)
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        if (!mask)
        {
            mapper.setAnnotationIntrospector(new JsonDisableAnnIntrospector());
        }
        if (snake)
        {
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        }
        if (!CollectionUtils.isEmpty(ignoreFields))
        {
            SimpleFilterProvider provider = new SimpleFilterProvider();
            SimpleBeanPropertyFilter fieldFilter = SimpleBeanPropertyFilter.serializeAllExcept(ignoreFields);
            provider.addFilter(IGNORE_ID, fieldFilter);
            //对所有的Object的子类都生效的属性过滤
            mapper.addMixIn(Object.class, JsonIgnoreField.class);
            return mapper.writer(provider);
        }
        return mapper.writer();
    }

    private JsonMappers()
    {
    }

    /**
     * 唯一的忽略ID标记
     */
    public static final String IGNORE_ID = "ignoreId";

    /**
     * 驼峰的Mapper
     */
    private static final ObjectMapper SNAKE_MAPPER = new ObjectMapper();

    /**
     * 普通的Mapper
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static
    {
        JavaTimeModule timeModule = new JavaTimeModule();
        JsonInstantSerializer instantSerializer = new JsonInstantSerializer();
        //替换其中的Instant时间转换(从秒转到毫秒)
        timeModule.addSerializer(Instant.class, instantSerializer);

        SNAKE_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SNAKE_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SNAKE_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SNAKE_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        SNAKE_MAPPER.setAnnotationIntrospector(new JsonDisableAnnIntrospector());
        //注册时间处理模块,注册全部模块方法:findAndRegisterModules
        SNAKE_MAPPER.registerModule(timeModule);

        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.setAnnotationIntrospector(new JsonDisableAnnIntrospector());
        MAPPER.registerModule(timeModule);
    }
}
