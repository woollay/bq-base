package com.biuqu.jwt;

import com.nimbusds.jose.jwk.JWK;
import org.junit.Test;

import java.text.ParseException;

public class JwkMgrTest
{

    @Test
    public void isPubMgr() throws ParseException
    {
        String jwkJson =
            "{\"kty\":\"RSA\",\"e\":\"AQAB\",\"kid\":\"6e3c6f31b6894254ae0cd887deaf3318\",\"n\":\"m9brla-BBWx4yU0FbOhoAIoRWmra3yswAjEPzyYrWl2ucFCJCG1rb0-IUVmVMEXbD-hjcaMJfJtGnu1-VKBT1WvjEp1kUyKoFnJOE1Mz4iSbdlbPhoeZwODOUd9qt-esKuOPCiL0zbltPFxjIzmpqPdNHppBLvbYxvyIO5DtINKxcCJW77h4aE3TFqusktD0xU0xLt8YPR2wJyQwEtl4j84_h9U6Zz2_UrazZbyBLuy5gHxi_uHdKhATz2tG_R11frUlExeKXzRJokXksSjXf4zVBhY3iexOTN4uxJcfeSG9JqQIA6I1unffw3NEUQWPHoWvf-Rhn2LdlAj2wgFsXQ\"}";
        JWK jwk = JWK.parse(jwkJson);

        System.out.println(jwk.getKeyID());
    }
}