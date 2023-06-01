package com.biuqu.handler;

import com.biuqu.encryption.BaseSecureSingleEncryption;
import org.bouncycastle.util.encoders.Hex;
import org.jasypt.encryption.StringEncryptor;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * 启动时生效的加密控制器
 *
 * @author BiuQu
 * @date 2023/1/3 20:08
 */
public class JasyptEncryptor implements StringEncryptor
{
    public JasyptEncryptor(BaseSecureSingleEncryption encryption, String key)
    {
        this.encryption = encryption;
        this.key = Hex.decode(key);
    }

    @Override
    public String encrypt(String s)
    {
        byte[] encryptBytes = this.encryption.encrypt(s.getBytes(StandardCharsets.UTF_8), key, null);
        return Hex.toHexString(encryptBytes);
    }

    @Override
    public String decrypt(String s)
    {
        String enc = s;
        boolean keyExists = s.toLowerCase(Locale.US).startsWith(KEY_PREFIX);
        if (keyExists)
        {
            enc = s.substring(KEY_PREFIX.length());
        }
        byte[] decryptBytes = this.encryption.decrypt(Hex.decode(enc), key, null);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    /**
     * 秘钥key的前缀
     */
    private static final String KEY_PREFIX = "[key]";

    /**
     * 对称加密算法
     */
    private final BaseSecureSingleEncryption encryption;

    /**
     * 全局的秘钥KEY
     */
    private final byte[] key;
}
