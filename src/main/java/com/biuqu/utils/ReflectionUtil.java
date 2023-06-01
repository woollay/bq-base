package com.biuqu.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 反射工具类
 * 在{@link org.springframework.util.ReflectionUtils}的基础上二次封装
 *
 * @author BiuQu
 * @date 2023/1/8 20:55
 */
public final class ReflectionUtil
{
    /**
     * 扫描包下的带有特定类注解的class
     *
     * @param pkg      包名
     * @param annClazz 类注解类型
     * @return 带有特定注解的class对象集合
     */
    public static Set<Class<?>> getPkgClass(String pkg, Class<? extends Annotation> annClazz)
    {
        Set<Class<?>> classes = Sets.newHashSet();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String pathPattern = ClassUtils.convertClassNameToResourcePath(pkg) + CLASS_PATH_PATTERN;
        pathPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + pathPattern;
        try
        {
            Resource[] resources = resolver.getResources(pathPattern);
            MetadataReaderFactory metaFactory = new CachingMetadataReaderFactory(resolver);
            for (Resource resource : resources)
            {
                MetadataReader reader = metaFactory.getMetadataReader(resource);
                String className = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);
                Annotation ann = clazz.getAnnotation(annClazz);
                if (null != ann)
                {
                    classes.add(clazz);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("failed to get package[{}]classes by annotation[{}]:{}", pkg, annClazz, e);
        }
        return classes;
    }

    /**
     * 根据属性上的注解获取属性列表
     *
     * @param instClazz     实例class类型
     * @param fieldAnnClazz 实例的属性上的注解class
     * @param <T>           注解类型
     * @return 属性列表
     */
    public static <T extends Annotation> Set<Field> getFields(Class<?> instClazz, Class<T> fieldAnnClazz)
    {
        Set<Field> fields = Sets.newHashSet();
        try
        {
            Field[] dFields = instClazz.getDeclaredFields();
            for (Field field : dFields)
            {
                T ann = field.getAnnotation(fieldAnnClazz);
                if (null != ann)
                {
                    fields.add(field);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("failed to get field names by annotation[{}].", fieldAnnClazz, e);
        }
        return fields;
    }

    /**
     * 根据属性上的注解获取配对的属性名集合
     *
     * @param instC 实例class类型
     * @param kC    属性上的key注解class
     * @param vC    属性上的key注解的配对class
     * @param <K>   key注解类型
     * @param <V>   key注解配对注解的类型
     * @return 为配对的属性名集合
     */
    public static <K extends Annotation, V extends Annotation> Map<String, String> getFields(Class<?> instC,
        Class<K> kC, Class<V> vC)
    {
        Map<String, String> map = Maps.newHashMap();
        Map<String, String> keyAnnMap = getFieldAnnMap(instC, kC);
        Map<String, String> valueAnnMap = getFieldAnnMap(instC, vC);
        Map<String, String> invertValueAnnMap = MapUtils.invertMap(valueAnnMap);
        for (String keyField : keyAnnMap.keySet())
        {
            String annValue = keyAnnMap.get(keyField);
            String valueField = invertValueAnnMap.get(annValue);
            map.put(keyField, valueField);
        }
        return map;
    }

    /**
     * 获取属性值
     *
     * @param instance  实例对象
     * @param fieldName 实例的属性名
     * @param <T>       实例的属性值对象类型
     * @return 获取属性值对象实例
     */
    public static <T> T getField(Object instance, String fieldName)
    {
        Field field = null;
        try
        {
            field = ReflectionUtils.findField(instance.getClass(), fieldName);
            if (null != field)
            {
                ReflectionUtils.makeAccessible(field);
                Object value = ReflectionUtils.getField(field, instance);
                return (T)value;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("failed to get field[{}].", fieldName, e);
        }
        finally
        {
            if (null != field)
            {
                field.setAccessible(false);
            }
        }
        return null;
    }

    /**
     * 更新实例的属性值
     *
     * @param instance  实例对象
     * @param fieldName 实例的属性名
     * @param value     实例的属性值
     */
    public static void updateField(Object instance, String fieldName, Object value)
    {
        Field field = null;
        try
        {
            field = ReflectionUtils.findField(instance.getClass(), fieldName);
            if (null != field)
            {
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, instance, value);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("failed to update field[{}].", fieldName, e);
        }
        finally
        {
            if (null != field)
            {
                field.setAccessible(false);
            }
        }
    }

    /**
     * 获取注解中的属性值
     *
     * @param ann          注解
     * @param annFieldName 注解中的属性名
     * @param <T>          注解类型
     * @return 注解中的属性值
     */
    public static <T extends Annotation> Object getAnnValue(T ann, String annFieldName)
    {
        Map<?, ?> mValues = getAnnValues(ann, true);
        if (!CollectionUtils.isEmpty(mValues))
        {
            return mValues.get(annFieldName);
        }
        return null;
    }

    /**
     * 获取注解中的属性键值对集合(特意处理成只读)
     *
     * @param ann 注解
     * @param <T> 注解类型
     * @return 注解中属性的键值对集合
     */
    public static <T extends Annotation> Map<Object, Object> getAnnValues(T ann)
    {
        return getAnnValues(ann, true);
    }

    /**
     * 更新注解上的属性值
     *
     * @param ann        注解对象
     * @param fieldName  注解属性名
     * @param fieldValue 注解属性值
     * @param <T>        注解类型
     */
    public static <T extends Annotation> void updateAnn(T ann, Object fieldName, Object fieldValue)
    {
        try
        {
            Map<Object, Object> mValues = getAnnValues(ann, false);
            Object value = mValues.get(fieldName);
            mValues.put(fieldName, fieldValue);
            LOGGER.info("replace annotation[{}/{}] from[{}] to[{}]", ann, fieldName, value, fieldValue);
        }
        catch (Exception e)
        {
            LOGGER.error("failed to update field annotation[{}/{}].", ann, fieldName, e);
        }
    }

    /**
     * 替换实例的属性注解中value属性对应的值
     *
     * @param instClazz 实例对象的class类型
     * @param annClazz  属性注解class类型
     * @param annValue  属性注解的value属性对应的新值
     * @param <T>       注解类型
     */
    public static <T extends Annotation> void updateFieldAnn(Class<?> instClazz, Class<T> annClazz, Object annValue)
    {
        updateFieldAnn(instClazz, annClazz, ANN_VALUE, annValue);
    }

    /**
     * 替换实例的属性注解中任意属性的值
     *
     * @param instC 实例对象的class类型
     * @param annC  属性注解class类型
     * @param annK  属性注解的名称
     * @param annV  属性注解名称对应的新值
     * @param <T>   注解类型
     */
    public static <T extends Annotation> void updateFieldAnn(Class<?> instC, Class<T> annC, String annK, Object annV)
    {
        Set<Field> fields = getFields(instC, annC);
        if (!CollectionUtils.isEmpty(fields))
        {
            for (Field field : fields)
            {
                T ann = field.getAnnotation(annC);
                updateAnn(ann, annK, annV);
            }
        }
    }

    /**
     * 获取实例对象上的方法集合
     *
     * @param instC          实例对象
     * @param methodAnnClazz 方法上的注解class
     * @param <T>            注解类型
     * @return 实例对象上的方法集合
     */
    public static <T extends Annotation> Set<Method> getMethods(Class<?> instC, Class<T> methodAnnClazz)
    {
        Set<Method> methods = Sets.newHashSet();
        try
        {
            Method[] declaredMethods = instC.getDeclaredMethods();
            for (Method method : declaredMethods)
            {
                T ann = method.getAnnotation(methodAnnClazz);
                if (null != ann)
                {
                    methods.add(method);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("failed to get field annotation[{}].", methodAnnClazz, e);
        }
        return methods;
    }

    /**
     * 替换实例的方法注解中value属性的值
     *
     * @param instC 实例对象的class类型
     * @param annC  方法注解class类型
     * @param annV  方法注解名称对应的新值
     * @param <T>   注解类型
     */
    public static <T extends Annotation> void updateMethodAnn(Class<?> instC, Class<T> annC, Object annV)
    {
        updateMethodAnn(instC, annC, ANN_VALUE, annV);
    }

    /**
     * 替换实例的方法注解中任意属性的值
     *
     * @param instC 实例对象的class类型
     * @param annC  方法注解class类型
     * @param annK  方法注解的名称
     * @param annV  方法注解名称对应的新值
     * @param <T>   注解类型
     */
    public static <T extends Annotation> void updateMethodAnn(Class<?> instC, Class<T> annC, String annK, Object annV)
    {
        Set<Method> methods = getMethods(instC, annC);
        if (!CollectionUtils.isEmpty(methods))
        {
            for (Method method : methods)
            {
                T ann = method.getAnnotation(annC);
                updateAnn(ann, annK, annV);
            }
        }
    }

    /**
     * 替换类的注解中value属性的值
     *
     * @param instC 实例对象的class类型
     * @param annC  类注解class类型
     * @param annV  类注解名称对应的新值
     * @param <T>   注解类型
     */
    public static <T extends Annotation> void updateClassAnn(Class<?> instC, Class<T> annC, Object annV)
    {
        updateClassAnn(instC, annC, ANN_VALUE, annV);
    }

    /**
     * 替换类的注解中任意属性的值
     *
     * @param instC 实例对象的class类型
     * @param annC  类注解class类型
     * @param annK  类注解的名称
     * @param annV  类注解名称对应的新值
     * @param <T>   注解类型
     */
    public static <T extends Annotation> void updateClassAnn(Class<?> instC, Class<T> annC, String annK, Object annV)
    {
        T ann = instC.getAnnotation(annC);
        if (null != ann)
        {
            updateAnn(ann, annK, annV);
        }
    }

    /**
     * 获取注解中的属性键值对集合(特意处理成只读)
     *
     * @param ann   注解
     * @param close 是否需要关闭注解中的访问权限
     * @param <T>   注解类型
     * @return 注解中属性的键值对集合
     */
    private static <T extends Annotation> Map<Object, Object> getAnnValues(T ann, boolean close)
    {
        Field annField = null;
        try
        {
            //1.获取注解上的代理handler
            InvocationHandler handler = Proxy.getInvocationHandler(ann);
            annField = handler.getClass().getDeclaredField(ANN_MAP);
            ReflectionUtils.makeAccessible(annField);
            //2.获取handler上存放注解属性键值对的map
            Map<Object, Object> map = (Map<Object, Object>)annField.get(handler);
            if (close)
            {
                map = Collections.unmodifiableMap(map);
            }
            return map;
        }
        catch (Exception e)
        {
            LOGGER.error("failed to get field annotation[{}].", ann, e);
        }
        finally
        {
            if (null != annField)
            {
                annField.setAccessible(false);
            }
        }
        return null;
    }

    /**
     * 根据属性上的注解获取属性及其注解值
     *
     * @param instC 实例class类型
     * @param annC  属性上的注解class
     * @param <T>   属性注解类型
     * @return 属性和特定注解的值的映射集合
     */
    private static <T extends Annotation> Map<String, String> getFieldAnnMap(Class<?> instC, Class<T> annC)
    {
        Map<String, String> fieldAnnMap = Maps.newHashMap();
        Set<Field> fields = getFields(instC, annC);
        for (Field field : fields)
        {
            T ann = field.getAnnotation(annC);
            Object annValue = getAnnValue(ann, ANN_VALUE);
            String value = null;
            if (annValue instanceof String[])
            {
                value = ((String[])annValue)[0];
            }
            else if (annValue instanceof String)
            {
                value = annValue.toString();
            }

            if (!StringUtils.isEmpty(value))
            {
                fieldAnnMap.put(field.getName(), value);
            }
        }
        return fieldAnnMap;
    }

    private ReflectionUtil()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 注解的值集合
     */
    private static final String ANN_MAP = "memberValues";

    /**
     * 注解的值
     */
    private static final String ANN_VALUE = "value";

    /**
     * class路径通配符
     */
    private static final String CLASS_PATH_PATTERN = "/**/*.class";
}
