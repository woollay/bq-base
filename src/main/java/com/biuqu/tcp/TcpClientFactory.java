package com.biuqu.tcp;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * tcp client连接工厂
 *
 * @author BiuQu
 * @date 2023/1/6 22:15
 */
public class TcpClientFactory extends BasePooledObjectFactory<TcpClient>
{
    public TcpClientFactory(TcpConfig config)
    {
        this.config = config;
    }

    @Override
    public TcpClient create() throws Exception
    {
        return new TcpClient(this.config);
    }

    @Override
    public PooledObject<TcpClient> wrap(TcpClient obj)
    {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public void destroyObject(PooledObject<TcpClient> p) throws Exception
    {
        p.invalidate();
        p.getObject().disconnect();
        LOGGER.info("destroy tcp client[{}] now.", p.getObject().getClientId());
    }

    @Override
    public boolean validateObject(PooledObject<TcpClient> p)
    {
        return super.validateObject(p);
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClientFactory.class);

    /**
     * 连接配置
     */
    private TcpConfig config;
}
