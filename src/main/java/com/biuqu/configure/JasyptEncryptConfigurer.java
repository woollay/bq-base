package com.biuqu.configure;

import com.biuqu.encryption.BaseSecureSingleEncryption;
import com.biuqu.encryption.factory.EncryptionFactory;
import com.biuqu.handler.JasyptEncryptor;
import com.biuqu.hsm.facade.HsmFacade;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 加密配置
 *
 * @author BiuQu
 * @date 2023/1/3 20:01
 */
@Configuration
public class JasyptEncryptConfigurer
{
    /**
     * 配置自动加解密的处理器
     *
     * @return 加解密处理器
     */
    @Bean("jasyptStringEncryptor")
    public StringEncryptor getEncryptor()
    {
        String confKey = this.key;
        //兼容有加密机的场景(加密机会对配置文件的加密key进行加密)
        if (null != this.hsmFacade)
        {
            //解密出真实的配置key
            confKey = this.hsmFacade.decrypt(this.key);
        }

        BaseSecureSingleEncryption encryption;
        if (this.gm)
        {
            encryption = EncryptionFactory.SecureSM4.createAlgorithm();
        }
        else
        {
            encryption = EncryptionFactory.SecureAES.createAlgorithm();
        }
        return new JasyptEncryptor(encryption, confKey);
    }

    /**
     * 注入加密机(有才注入，否则忽略)
     */
    @Autowired(required = false)
    private HsmFacade hsmFacade;

    /**
     * 对配置文件是否为国密
     */
    @Value("${bq.encrypt.gm:true}")
    private boolean gm;

    /**
     * 对配置文件加密的sm4 key
     */
    @Value("${bq.encrypt.enc}")
    private String key;
}
