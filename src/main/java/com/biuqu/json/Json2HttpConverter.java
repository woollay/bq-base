package com.biuqu.json;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.TypeUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * 覆写集成在spring-web的http请求的jackson转换器
 * <p>
 * {@link AbstractJackson2HttpMessageConverter}源码
 *
 * @author BiuQu
 * @date 2023/6/9 09:43
 */
public class Json2HttpConverter extends MappingJackson2HttpMessageConverter
{
    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
    throws IOException, HttpMessageNotWritableException
    {
        MediaType contentType = outputMessage.getHeaders().getContentType();
        JsonEncoding encoding = getJsonEncoding(contentType);

        Class<?> clazz = (object instanceof MappingJacksonValue ? ((MappingJacksonValue)object).getValue().getClass() :
            object.getClass());
        ObjectMapper objectMapper = this.defaultObjectMapper;
        Assert.state(objectMapper != null, "No ObjectMapper for " + clazz.getName());

        OutputStream outputStream = StreamUtils.nonClosing(outputMessage.getBody());
        try (JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, encoding))
        {
            writePrefix(generator, object);

            Object value = object;
            Class<?> serializationView = null;
            FilterProvider filters = null;
            JavaType javaType = null;

            if (object instanceof MappingJacksonValue)
            {
                MappingJacksonValue container = (MappingJacksonValue)object;
                value = container.getValue();
                serializationView = container.getSerializationView();
                filters = container.getFilters();
            }
            if (type != null && TypeUtils.isAssignable(type, value.getClass()))
            {
                javaType = getJavaType(type, null);
            }

            //更新了objectWriter的逻辑
            ObjectWriter objectWriter = getObjectWriter(this.defaultObjectMapper, serializationView, object);
            if (filters != null)
            {
                objectWriter = objectWriter.with(filters);
            }
            if (javaType != null && javaType.isContainerType())
            {
                objectWriter = objectWriter.forType(javaType);
            }
            SerializationConfig config = objectWriter.getConfig();
            if (contentType != null && contentType.isCompatibleWith(MediaType.TEXT_EVENT_STREAM) && config.isEnabled(
                SerializationFeature.INDENT_OUTPUT))
            {
                DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
                prettyPrinter.indentObjectsWith(new DefaultIndenter("  ", "\ndata:"));
                objectWriter = objectWriter.with(prettyPrinter);
            }
            objectWriter.writeValue(generator, value);

            writeSuffix(generator, object);
            generator.flush();
        }
        catch (InvalidDefinitionException ex)
        {
            throw new HttpMessageConversionException("Type definition error: " + ex.getType(), ex);
        }
        catch (JsonProcessingException ex)
        {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getOriginalMessage(), ex);
        }
    }

    /**
     * 定制Json转换器ObjectMapper的写对象ObjectWriter
     *
     * @param objectMapper      Json转换器
     * @param serializationView Jackson特殊视图对象
     * @param object            原始数据对象
     * @return Json转换器的定制写对象(支持配置忽略属性列表)
     */
    private ObjectWriter getObjectWriter(ObjectMapper objectMapper, Class<?> serializationView, Object object)
    {
        ObjectWriter objectWriter;
        if (null != serializationView)
        {
            objectWriter = objectMapper.writerWithView(serializationView);
        }
        else
        {
            Set<String> ignoreFields = ignoreMgr.getIgnores(object);
            objectWriter = JsonMappers.getIgnoreWriter(objectMapper, ignoreFields);
        }
        return objectWriter;
    }

    public Json2HttpConverter(JsonIgnoreMgr ignoreMgr)
    {
        super();
        this.ignoreMgr = ignoreMgr;
    }

    /**
     * json属性忽略管理器
     */
    private final JsonIgnoreMgr ignoreMgr;
}
