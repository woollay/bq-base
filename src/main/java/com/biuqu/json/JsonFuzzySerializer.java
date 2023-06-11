package com.biuqu.json;

import com.biuqu.encryption.factory.EncryptionFactory;
import com.biuqu.encryption.impl.ShaHash;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;

/**
 * 脱敏序列化
 *
 * @author BiuQu
 * @date 2023/6/11 09:30
 */
public class JsonFuzzySerializer extends BaseJsonSerializer<String>
{
    @Override
    protected Object getNewValue(Object object, String key, String value)
    {
        ShaHash sha = EncryptionFactory.SHA512.createAlgorithm();
        return Hex.toHexString(sha.digest(value.getBytes(StandardCharsets.UTF_8)));
    }
}
