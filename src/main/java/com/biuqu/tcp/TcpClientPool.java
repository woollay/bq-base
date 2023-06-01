package com.biuqu.tcp;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tcp client连接池
 *
 * @author BiuQu
 * @date 2023/1/6 22:23
 */
public class TcpClientPool extends GenericObjectPool<TcpClient>
{
    public TcpClientPool(PooledObjectFactory<TcpClient> factory)
    {
        super(factory);
    }

    @Override
    public TcpClient borrowObject() throws Exception
    {
        TcpClient client = super.borrowObject();
        LOGGER.info("use one tcp client[{}] now.", client.getClientId());
        return client;
    }

    @Override
    public void returnObject(TcpClient obj)
    {
        super.returnObject(obj);
        LOGGER.info("recycle one tcp client[{}] now.", obj.getClientId());
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("tcp client size:").append(this.getMaxTotal());
        builder.append(",active size:").append(this.getNumActive());
        builder.append(",idle size:").append(this.getNumIdle());
        return builder.toString();
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClientPool.class);
}
