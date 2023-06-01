package com.biuqu.utils;

import com.biuqu.model.FileType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 文件处理工具类
 *
 * @author BiuQu
 * @date 2023/1/10 09:40
 */
public final class FileUtil
{
    /**
     * 读取文件的二进制内容
     *
     * @param path 文件路径
     * @return 文件二进制内容
     */
    public static byte[] read(String path)
    {
        return read(IoUtil.readInputStream(path));
    }

    /**
     * 读取输入流报文
     *
     * @param in 文件输入流
     * @return 二进制报文
     */
    public static byte[] read(InputStream in)
    {
        return read(in, Integer.MAX_VALUE);
    }

    /**
     * 读取限定文件大小的二进制流
     *
     * @param in   二进制流
     * @param size 二进制流的大小上限
     * @return 文件二进制报文
     */
    public static byte[] read(InputStream in, int size)
    {
        try
        {
            if (size <= 0)
            {
                size = Integer.MAX_VALUE;
            }

            byte[] data = IOUtils.toByteArray(in);
            if (null != data && data.length > size)
            {
                LOGGER.error("failed to read limit data size:{}.", data.length);
                return null;
            }

            FileType fileType = FileType.getType(data);
            LOGGER.info("current read stream file type:{}", fileType);
            return data;
        }
        catch (IOException e)
        {
            LOGGER.error("failed to read input stream.", e);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
        return null;
    }

    /**
     * 读取指定编码的文件内容(未指定编码格式时，则默认读取UTF-8编码)
     *
     * @param path    文件路径
     * @param charset 文件编码格式
     * @return 文件内容
     */
    public static String read(String path, Charset charset)
    {
        byte[] data = read(path);
        if (null == data)
        {
            LOGGER.error("failed to read input stream:{}.", path);
            return null;
        }
        if (null == charset)
        {
            charset = StandardCharsets.UTF_8;
        }
        return new String(data, charset);
    }

    /**
     * 把内容写入文件
     *
     * @param data 二进制文件内容
     * @param path 目标文件路径
     */
    public static void write(byte[] data, String path)
    {
        OutputStream out = null;
        try
        {
            File file = new File(path).getCanonicalFile();
            if (!file.isFile() || !file.exists())
            {
                FileUtils.forceMkdirParent(file);
                LOGGER.info("current file path[{}] force created.", file.getParentFile().getCanonicalPath());
            }
            FileType fileType = FileType.getType(data);
            LOGGER.info("current write stream file type:{}", fileType);
            out = new FileOutputStream(file);
            IOUtils.write(data, out);
        }
        catch (IOException e)
        {
            LOGGER.error("failed to write file.", e);
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
    }

    private FileUtil()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
}
