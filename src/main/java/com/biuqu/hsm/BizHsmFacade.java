package com.biuqu.hsm;

import com.biuqu.annotation.*;
import com.biuqu.errcode.ErrCodeEnum;
import com.biuqu.exception.CommonException;
import com.biuqu.hsm.facade.HsmFacade;
import com.biuqu.model.BaseSecurity;
import com.biuqu.utils.FileUtil;
import com.biuqu.utils.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 业务安全管理器
 *
 * @author BiuQu
 * @date 2023/1/19 12:25
 */
@Slf4j
public final class BizHsmFacade
{
    public BizHsmFacade(HsmFacade hsm)
    {
        this.hsm = hsm;
    }

    /**
     * 前置安全加密和签名
     *
     * @param model 业务模型
     * @param <T>   业务模型的类型
     */
    public <T extends BaseSecurity> void before(T model)
    {
        //1.加密
        this.beforeEncryption(model, EncryptionSecurityAnn.class);
        this.beforeEncryption(model, FileSecurityAnn.class);
        this.beforeEncryption(model, HashSecurityAnn.class);

        //2.签名
        this.beforeIntegrity(model, IntegritySecurityAnn.class);
    }

    /**
     * 前置安全加密和签名
     *
     * @param models 批量业务模型
     * @param <T>    业务模型的类型
     */
    public <T extends BaseSecurity> void before(List<T> models)
    {
        for (T model : models)
        {
            this.before(model);
        }
    }

    /**
     * 批量的前置安全签名
     *
     * @param models 批量业务模型
     * @param <T>    业务模型类型
     */
    public <T extends BaseSecurity> void beforeIntegrity(List<T> models)
    {
        for (T model : models)
        {
            this.beforeIntegrity(model, IntegritySecurityAnn.class);
        }
    }

    /**
     * 后置安全解密和验签
     *
     * @param model 业务模型
     * @param <T>   业务模型的类型
     */
    public <T extends BaseSecurity> void after(T model)
    {
        //1.验签
        this.afterIntegrity(model, IntegritySecurityAnn.class);

        //2.解密
        this.afterEncryption(model, EncryptionSecurityAnn.class);
        this.afterEncryption(model, FileSecurityAnn.class);
    }

    /**
     * 后置安全解密和验签
     *
     * @param models 批量业务模型
     * @param <T>    业务模型的类型
     */
    public <T extends BaseSecurity> void after(List<T> models)
    {
        for (T model : models)
        {
            this.after(model);
        }
    }

    /**
     * 后置安全验签
     *
     * @param models 批量业务模型
     * @param <T>    业务模型的类型
     */
    public <T extends BaseSecurity> void afterIntegrity(List<T> models)
    {
        for (T model : models)
        {
            this.afterIntegrity(model, IntegritySecurityAnn.class);
        }
    }

    /**
     * 前置安全加密(数据机密性)
     *
     * @param model    业务模型
     * @param annClazz 业务模型上的属性注解
     * @param <T>      业务模型类型
     * @param <A>      业务模型上的属性注解类型
     */
    private <T extends BaseSecurity, A extends Annotation> void beforeEncryption(T model, Class<A> annClazz)
    {
        Set<Field> fields = ReflectionUtil.getFields(model.getClass(), annClazz);
        for (Field field : fields)
        {
            Object value = ReflectionUtil.getField(model, field.getName());
            if (!(value instanceof String))
            {
                return;
            }

            String newValue = value.toString();
            if (annClazz == FileSecurityAnn.class)
            {
                this.beforeFileEncryption(model, field.getName());
                return;
            }
            else if (annClazz == EncryptionSecurityAnn.class)
            {
                ReflectionUtil.updateField(model, field.getName(), hsm.encrypt(newValue));
            }
            else if (annClazz == HashSecurityAnn.class)
            {
                ReflectionUtil.updateField(model, field.getName(), hsm.hash(newValue));
            }
        }
    }

    /**
     * 前置安全签名(数据完整性)
     *
     * @param model    业务模型
     * @param annClazz 业务模型的方法注解
     * @param <T>      业务模型类型
     * @param <A>      业务模型上的方法注解类型
     */
    private <T extends BaseSecurity, A extends Annotation> void beforeIntegrity(T model, Class<A> annClazz)
    {
        Set<Method> methods = ReflectionUtil.getMethods(model.getClass(), annClazz);
        for (Method method : methods)
        {
            if (INTEGRITY_KEY.equalsIgnoreCase(method.getName()))
            {
                String integrity = model.toIntegrity();
                model.setSecKey(hsm.sign(integrity));
            }
        }
    }

    /**
     * 前置文件加密
     *
     * @param model 业务模型
     * @param name  文件路径属性名称
     * @param <T>   业务模型类型
     */
    private <T extends BaseSecurity> void beforeFileEncryption(T model, String name)
    {
        Class<? extends BaseSecurity> clazz = model.getClass();
        Map<String, String> fields = ReflectionUtil.getFields(clazz, FileSecurityAnn.class, FileDataSecurityAnn.class);
        String pathValue = ReflectionUtil.getField(model, name);
        Object dataValue = ReflectionUtil.getField(model, fields.get(name));
        this.encryptFile(pathValue, dataValue);
    }

    /**
     * 后置文件解密
     *
     * @param model 业务模型
     * @param name  文件路径属性名称
     * @param <T>   业务模型类型
     */
    private <T extends BaseSecurity> void afterFileEncryption(T model, String name)
    {
        Class<? extends BaseSecurity> clazz = model.getClass();
        Map<String, String> fields = ReflectionUtil.getFields(clazz, FileSecurityAnn.class, FileDataSecurityAnn.class);
        //获取文件路径和文件内容对应的类型
        String pathValue = ReflectionUtil.getField(model, name);
        Field dataField = ReflectionUtils.findField(model.getClass(), fields.get(name));
        String dataType = dataField.getGenericType().getTypeName();

        //获取解密后的文件内容并更新至业务模型中
        Object newData = this.decryptFile(pathValue, dataType);
        ReflectionUtil.updateField(model, dataField.getName(), newData);
        //返回文件内容后，把路径置空
        ReflectionUtil.updateField(model, name, null);
    }

    /**
     * 后置安全解密(数据机密性)
     *
     * @param model    业务模型
     * @param annClazz 业务模型上的属性注解
     * @param <T>      业务模型类型
     * @param <A>      业务模型上的属性注解类型
     */
    private <T extends BaseSecurity, A extends Annotation> void afterEncryption(T model, Class<A> annClazz)
    {
        Set<Field> fields = ReflectionUtil.getFields(model.getClass(), annClazz);
        for (Field field : fields)
        {
            Object value = ReflectionUtil.getField(model, field.getName());
            if (!(value instanceof String))
            {
                return;
            }

            String newValue = value.toString();
            if (annClazz == FileSecurityAnn.class)
            {
                this.afterFileEncryption(model, field.getName());
                return;
            }
            else if (annClazz == EncryptionSecurityAnn.class)
            {
                ReflectionUtil.updateField(model, field.getName(), hsm.decrypt(newValue));
            }
        }
    }

    /**
     * 后置安全验签(数据完整性)
     *
     * @param model    业务模型
     * @param annClazz 业务模型上的属性注解
     * @param <T>      业务模型类型
     * @param <A>      业务模型上的属性注解类型
     */
    private <T extends BaseSecurity, A extends Annotation> void afterIntegrity(T model, Class<A> annClazz)
    {
        Set<Method> methods = ReflectionUtil.getMethods(model.getClass(), annClazz);
        for (Method method : methods)
        {
            if (INTEGRITY_KEY.equalsIgnoreCase(method.getName()))
            {
                String integrity = model.toIntegrity();
                boolean result = hsm.verify(integrity, model.getSecKey());
                if (!result)
                {
                    throw new CommonException(ErrCodeEnum.SIGNATURE_ERROR.getCode());
                }
                //签名成功后,删除签名值
                model.setSecKey(null);
            }
        }
    }

    /**
     * 加密文件
     *
     * @param path 文件路径
     * @param data 文件内容
     */
    private void encryptFile(String path, Object data)
    {
        if (data == null)
        {
            log.error("No data to encrypt:{}.", path);
            return;
        }

        byte[] dataBytes = null;
        if (data instanceof byte[])
        {
            dataBytes = (byte[])data;
        }
        else if (data instanceof String)
        {
            dataBytes = data.toString().getBytes(StandardCharsets.UTF_8);
        }

        if (dataBytes == null)
        {
            log.error("encrypt file error:{}.", path);
            return;
        }

        byte[] encryptBytes = hsm.getEncryptHsm().encrypt(dataBytes);
        FileUtil.write(encryptBytes, path);
    }

    /**
     * 解密文件
     *
     * @param path     文件路径
     * @param dataType 文件字段的类型
     */
    private Object decryptFile(String path, String dataType)
    {
        byte[] encryptBytes = FileUtil.read(path);
        byte[] data = hsm.getEncryptHsm().decrypt(encryptBytes);
        if (STRING_TYPE.equalsIgnoreCase(dataType))
        {
            return new String(data);
        }
        else if (BYTE_ARRAY_TYPE.equalsIgnoreCase(dataType))
        {
            return data;
        }
        return null;
    }

    /**
     * String类型
     */
    private static final String STRING_TYPE = "java.lang.String";

    /**
     * 数组类型
     */
    private static final String BYTE_ARRAY_TYPE = "byte[]";

    /**
     * 完整性方法名
     */
    private static final String INTEGRITY_KEY = "toIntegrity";

    /**
     * 加密机
     */
    private final HsmFacade hsm;
}
