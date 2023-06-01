package com.biuqu.tcp;

import com.biuqu.context.ApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpClientPoolTest
{
    public void useTcpSample()
    {
        //1.get tcp config from yaml
        TcpConfig tcpConfig = ApplicationContextHolder.getBean(TcpConfig.class);
        //2.create tcp client factory by config
        TcpClientFactory clientFactory = new TcpClientFactory(tcpConfig);
        //3.create tcp client pool by tcp client factory
        TcpClientPool clientPool = new TcpClientPool(clientFactory);
        //4.get tcp client from tcp client pool
        TcpClient tcpClient = null;
        try
        {
            tcpClient = clientPool.borrowObject();
            //5.get tcp response from server
            String result = tcpClient.send("xxx command.");
            LOGGER.info("current tcp client[{}] result is:{}", tcpClient.getClientId(), result);
        }
        catch (Exception e)
        {
            LOGGER.error("failed to get tcp response.", e);
        }
        finally
        {
            //6.after used,return this tcp client to tcp client pool for reusing.
            if (null != tcpClient)
            {
                clientPool.returnObject(tcpClient);
            }
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClientPoolTest.class);
}