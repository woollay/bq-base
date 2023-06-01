package com.biuqu.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Xml处理工具类
 *
 * @author BiuQu
 * @date 2023/1/10 09:43
 */
public final class XmlUtil
{
    /**
     * 把xml实例对象转换成xml
     *
     * @param bean xml实体对象
     * @return xml格式的内容
     */
    public static String toXml(Object bean)
    {
        return new String(toXmlBytes(bean), StandardCharsets.UTF_8);
    }

    /**
     * 转换成xml的二进制
     *
     * @param bean xml实例对象
     * @return xml内容的二进制数组
     */
    public static byte[] toXmlBytes(Object bean)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            JAXBContext jaxb = JAXBContext.newInstance(bean.getClass());
            Marshaller marshaller = jaxb.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8);
            marshaller.marshal(bean, out);
            return out.toByteArray();
        }
        catch (JAXBException e)
        {
            LOGGER.error("failed to get xml from bean.", e);
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
        return null;
    }

    /**
     * 把xml文件转换成xml bean对象
     *
     * @param path  xml文件路径
     * @param clazz xml bean对象的class类型
     * @param <T>   xml bean类型
     * @return xml bean对象
     */
    public static <T> T fileToBean(String path, Class<T> clazz)
    {
        return toBean(IoUtil.readInputStream(path), clazz);
    }

    /**
     * 把xml内容转换成xml bean对象
     *
     * @param xml   xml格式内容
     * @param clazz xml bean对象的class类型
     * @param <T>   xml bean类型
     * @return xml bean对象
     */
    public static <T> T toBean(String xml, Class<T> clazz)
    {
        ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        return toBean(in, clazz);
    }

    /**
     * 把xml InputStream转换成xml bean对象
     *
     * @param in    xml输入流
     * @param clazz xml bean对象的class类型
     * @param <T>   xml bean类型
     * @return xml bean对象
     */
    public static <T> T toBean(InputStream in, Class<T> clazz)
    {
        try
        {
            JAXBContext jaxb = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jaxb.createUnmarshaller();
            Object bean = unmarshaller.unmarshal(in);
            return (T)bean;
        }
        catch (JAXBException e)
        {
            LOGGER.error("failed to get xml bean.", e);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
        return null;
    }

    private XmlUtil()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtil.class);
}
