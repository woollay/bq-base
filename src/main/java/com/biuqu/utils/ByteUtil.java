package com.biuqu.utils;

import com.biuqu.constants.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * 二进制工具类
 *
 * @author BiuQu
 * @date 2023/1/8 15:35
 */
public final class ByteUtil
{
    /**
     * 把int转换成4个字节的数组
     *
     * @param num 数字
     * @return 二进制数组
     */
    public static byte[] from(int num)
    {
        ByteBuffer bf = ByteBuffer.allocate(Const.FOUR);
        bf.putInt(num);
        return bf.array();
    }

    /**
     * 把字符数组转换成等长的二进制数组
     *
     * @param chars 字符数组
     * @return 二进制数组
     */
    public static byte[] from(char[] chars)
    {
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();

        ByteBuffer bf = StandardCharsets.UTF_8.encode(cb);
        //该字节数组的长度可能会比字符数组的长度多1(多了个0)
        byte[] bytes = bf.array();
        return Arrays.copyOfRange(bytes, 0, chars.length);
    }

    /**
     * 按照10进制从最高位到最低位累加的整数值
     *
     * @param bytes 二进制数组
     * @return 二进制数组对应的10进制整数值
     */
    public static int to(byte[] bytes)
    {
        int sum = 0;
        int last = bytes.length;
        for (byte b : bytes)
        {
            last--;
            if (last >= 0)
            {
                //从最高位i依次位移:i乘以2的8次方(一个byte的长度)，即把所有位位移后拼接在一起
                int t = (b << (last * (Const.TWO * Const.FOUR)));
                sum += t;
            }
        }
        return sum;
    }

    /**
     * 从指定的长度生成随机二进制数组
     *
     * @param len 二进制数组的长度
     * @return 指定长度的二进制数组
     */
    public static byte[] fromRandom(int len)
    {
        if (len <= 0)
        {
            LOGGER.error("invalid byte array len:{}", len);
            return null;
        }

        SecureRandom random = RandomUtil.create();
        byte[] bytes = new byte[len];
        random.nextBytes(bytes);

        return bytes;
    }

    /**
     * 内存擦除
     *
     * @param bytes 二进制数组
     */
    public static void clear(byte[] bytes)
    {
        if (null == bytes)
        {
            return;
        }
        Arrays.fill(bytes, (byte)0);
    }

    private ByteUtil()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ByteUtil.class);
}
