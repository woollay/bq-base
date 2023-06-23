package com.biuqu.utils;

import com.biuqu.encryption.exception.EncryptionException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;

/**
 * IO工具类
 *
 * @author BiuQu
 * @date 2022/10/18 17:04
 **/
public final class IoUtil
{
    /**
     * 读取文件流
     * <pre>
     * 1.文件支持从class外部读取(class调试模式)和jar内部读取(jar包使用模式)
     * 2.以此class是否在jar包为依据，当不在时，优先从外部读取(证明是class调试模式，根本就没有jar包)
     * 3.如果上述方式读不到文件，则遵从spring的读取规则(使用spring的API读取)
     *
     * @param path 文件路径
     * @return 文件流
     */
    public static InputStream readInputStream(String path)
    {
        try
        {
            String clazzPath = IoUtil.class.getResource(FILE_SPLIT).getPath();
            boolean clazzInJar = clazzPath.startsWith(FILE_TYPE);
            boolean inClazzWithoutJar = clazzPath.startsWith(FILE_SPLIT) && !clazzInJar;
            boolean inJar = path.toLowerCase(Locale.US).startsWith(CLASSPATH) || clazzInJar;
            String realPath = path;
            //非jar包模式时，拼接全路径读取
            if (inClazzWithoutJar && !inJar)
            {
                if (!realPath.contains(FILE_FULL_PATH_TYPE) && !realPath.startsWith(FILE_SPLIT))
                {
                    realPath = clazzPath + realPath;
                }
                return new FileInputStream(realPath);
            }
            Resource resource = new PathMatchingResourcePatternResolver().getResource(path);
            boolean existFile = resource.exists();
            //jar包模式运行时,通过spring的api去读取
            if (existFile)
            {
                return resource.getInputStream();
            }
        }
        catch (Exception e)
        {
            throw new EncryptionException("read stream error.", e);
        }
        return null;
    }

    /**
     * 私有化构造方法
     */
    private IoUtil()
    {
    }

    /**
     * 非jar形式的文件全路径标记
     */
    public static final String FILE_FULL_PATH_TYPE = ":";

    /**
     * 文件类型
     */
    public static final String FILE_TYPE = "file:";

    /**
     * 文件分割类型
     */
    public static final String FILE_SPLIT = "/";

    /**
     * 配置类型
     */
    private static final String CLASSPATH = "classpath:";
}
