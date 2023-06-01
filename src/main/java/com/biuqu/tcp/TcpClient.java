package com.biuqu.tcp;

import com.biuqu.constants.Const;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.discard.DiscardTCPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tcp客户端
 *
 * @author BiuQu
 * @date 2023/1/6 21:02
 */
public class TcpClient extends DiscardTCPClient
{
    /**
     * 构建TCP客户端对象
     *
     * @param config tcp连接配置
     * @throws IOException 网络IO异常
     */
    public TcpClient(TcpConfig config) throws IOException
    {
        this.clientId = PREFIX + NUM.incrementAndGet();
        LOGGER.debug("create tcp client[{}] now.", this.clientId);

        this.setDefaultPort(config.getPort());
        this.connect(config.getHost(), config.getPort());
        Charset charset = Charsets.toCharset(config.getEncoding());
        if (null == charset)
        {
            charset = StandardCharsets.UTF_8;
        }
        this.setCharset(charset);
        this.setKeepAlive(true);
        this.setConnectTimeout(config.getConnTimeout());
        this.setSoTimeout(config.getSoTimeout());
        if (config.getSoLinger() > 0)
        {
            this.setSoLinger(true, config.getSoLinger());
        }
    }

    @Override
    public void disconnect() throws IOException
    {
        LOGGER.debug("recycle tcp client[{}] now.", this.clientId);
        super.disconnect();
    }

    /**
     * 获取Tcp client的唯一标识
     *
     * @return client的唯一标识
     */
    public String getClientId()
    {
        return this.clientId;
    }

    /**
     * 发送报文
     *
     * @param msg 报文
     * @return 接收到的响应报文
     * @throws IOException 网络IO异常
     */
    public String send(String msg) throws IOException
    {
        return this.send(msg, this.getCharset());
    }

    /**
     * 发送报文
     *
     * @param msg     报文
     * @param charset 报文编码
     * @return 接收到的响应报文
     * @throws IOException 网络IO异常
     */
    public String send(String msg, Charset charset) throws IOException
    {
        if (null == msg)
        {
            LOGGER.warn("no data to send.");
            return null;
        }

        if (null == charset)
        {
            charset = StandardCharsets.UTF_8;
        }
        byte[] data = msg.getBytes(charset);

        byte[] result = this.send(data);
        if (null == result)
        {
            LOGGER.warn("no data to receive.");
            return null;
        }
        return new String(result, charset);
    }

    /**
     * 发送报文
     *
     * @param data 报文
     * @return 接收到tcp server收到的报文
     * @throws IOException 网络IO异常
     */
    public byte[] send(byte[] data) throws IOException
    {
        return this.receive(asyncSend(data));
    }

    /**
     * 发送报文并获取服务端的响应流(适用于异步多线程处理的场景)
     *
     * @param data 报文
     * @return 接收tcp server数据的的响应流(可在异步线程中处理它)
     * @throws IOException 网络IO异常
     */
    public InputStream asyncSend(byte[] data) throws IOException
    {
        if (null == data)
        {
            LOGGER.warn("no data to send.");
            return null;
        }

        this.getOutputStream().write(data);

        return this.getInputStream();
    }

    /**
     * 接收tcp server响应的报文
     *
     * @param in tcp server响应流对象
     * @return 返回的二进制报文
     * @throws IOException 网络IO异常
     */
    private byte[] receive(InputStream in) throws IOException
    {
        byte[] buffers = new byte[Const.KB];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            int len;
            while ((len = in.read(buffers)) > 0)
            {
                out.write(buffers, 0, len);
            }
            return out.toByteArray();
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 获取响应流
     *
     * @return 响应流
     */
    private InputStream getInputStream()
    {
        return this._input_;
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClient.class);

    /**
     * 计数器
     */
    private static final AtomicInteger NUM = new AtomicInteger();

    /**
     * 默认的前缀
     */
    private static final String PREFIX = "CommonTcp-";

    /**
     * 客户id
     */
    private String clientId;
}
