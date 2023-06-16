package com.biuqu.handler;

import com.biuqu.encryption.BaseSecureSingleEncryption;
import com.biuqu.encryption.BaseSingleEncryption;
import com.biuqu.encryption.impl.Sm4SecureEncryption;
import com.biuqu.utils.ByteUtil;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class JasyptEncryptorTest
{

    @Test
    public void encrypt()
    {
        SecretKey secretKey = encryption.createKey(ByteUtil.fromRandom(16));
        byte[] keys = secretKey.getEncoded();
        System.out.println("sm4 key=" + Hex.toHexString(keys));
    }

    @Test
    public void decrypt()
    {
        String sm4EncData =
            "308db58c5e90a20f63c5cc7d1494d389cbaf6ec60cd0502b8364910c9164db89a5d6ecdeda978a161bdfae13008b84edc9ecda5d1b60b96f4ad019b9f3fb1379e228d86e267f77a18e88ef2676ab4167";
        byte[] encBytes = Hex.decode(sm4EncData);
        byte[] secKey = Hex.decode(key);
        byte[] decBytes = encryption.decrypt(encBytes, secKey, null);
        System.out.println("decBytes=" + Arrays.toString(decBytes));

        String test = "hello sm4";
        byte[] testEncBytes = encryption.encrypt(test.getBytes(), secKey, null);
        byte[] testDecBytes = encryption.decrypt(testEncBytes, secKey, null);
        System.out.println("test1 enc&dec[" + secKey.length + "]=" + new String(testDecBytes));
    }

    @Test
    public void testEncKey()
    {
        String key = "e9c9ba0326f00c39254ee7675907514a";
        byte[] encKey = "f056513b001bda32d80d1c6da4e59e0e".getBytes(StandardCharsets.UTF_8);
        BaseSingleEncryption encryption = new Sm4SecureEncryption();
        byte[] encBytes = encryption.encrypt(encKey, Hex.decode(key), null);
        System.out.println("encKey=" + Hex.toHexString(encBytes));
    }

    @Test
    public void testDbPwd()
    {
        String dbPwd = "postgres";
        BaseSecureSingleEncryption handler = new Sm4SecureEncryption();
        String key = "f056513b001bda32d80d1c6da4e59e0e";
        JasyptEncryptor jasyptEncryptor = new JasyptEncryptor(handler, key);
        String encDbPwd = jasyptEncryptor.encrypt(dbPwd);
        String decDbPwd = jasyptEncryptor.decrypt(encDbPwd);
        System.out.println(String.format("Jasypt encrypt db[%s]enc:ENC(%s),dec:%s", dbPwd, encDbPwd, decDbPwd));
    }

    @Test
    public void testEncSecurityByEnc()
    {
        BaseSecureSingleEncryption handler = new Sm4SecureEncryption();
        String key = "f056513b001bda32d80d1c6da4e59e0e";
        JasyptEncryptor jasyptEncryptor = new JasyptEncryptor(handler, key);
        String keyFormat = "[key]%s";

        String sm4Secure = "249ffc39f1dd696d0251e520f84d1650";
        String sm4EncHex = String.format(keyFormat, jasyptEncryptor.encrypt(sm4Secure));
        String sm4DecHex = jasyptEncryptor.decrypt(sm4EncHex);
        System.out.println(String.format("SecureSM4[%s]enc:ENC(%s),dec:%s", sm4Secure, sm4EncHex, sm4DecHex));

        String sm4 = "ad8a8b7dd5d37914372d898b26f79bba";
        String sm4EncHex2 = String.format(keyFormat, jasyptEncryptor.encrypt(sm4));
        String sm4DecHex2 = jasyptEncryptor.decrypt(sm4EncHex2);
        System.out.println(String.format("SM4[%s]enc:ENC(%s),dec:%s", sm4, sm4EncHex2, sm4DecHex2));

        String sm2Pri =
            "308193020100301306072a8648ce3d020106082a811ccf5501822d047930770201010420bda28ccbf4a4d5ca51391925b6e172126c06cac2254c8d9ee115a407be13e1fea00a06082a811ccf5501822da1440342000418dd7b8732c322933f348a3ee2836074022c9490a80dab2509e5f009da70fb33e7037fdde105609b833559d7410ff26e3e1b1f4fe025f291ab1497291ba8dcb1";
        String sm2Pub =
            "3059301306072a8648ce3d020106082a811ccf5501822d0342000418dd7b8732c322933f348a3ee2836074022c9490a80dab2509e5f009da70fb33e7037fdde105609b833559d7410ff26e3e1b1f4fe025f291ab1497291ba8dcb1";
        String sm2PriEncHex = String.format(keyFormat, jasyptEncryptor.encrypt(sm2Pri));
        String sm2PriDecHex = jasyptEncryptor.decrypt(sm2PriEncHex);
        System.out.println(String.format("SM2.pri[%s]enc:ENC(%s),dec:%s", sm2Pri, sm2PriEncHex, sm2PriDecHex));
        String sm2PubEncHex = String.format(keyFormat, jasyptEncryptor.encrypt(sm2Pub));
        String sm2PubDecHex = jasyptEncryptor.decrypt(sm2PubEncHex);
        System.out.println(String.format("SM2.pub[%s]enc:ENC(%s),dec:%s", sm2Pub, sm2PubEncHex, sm2PubDecHex));

        String aesSecure = "a28e26046dc3f4bbbf1971c8ffd41d79647bbd9886f12c73d280c11e89fb189c";
        String aesSecureEncHex = String.format(keyFormat, jasyptEncryptor.encrypt(aesSecure));
        String aesSecureDecHex = jasyptEncryptor.decrypt(aesSecureEncHex);
        System.out.println(
            String.format("SecureAES[%s]enc:ENC(%s),dec:%s", aesSecure, aesSecureEncHex, aesSecureDecHex));

        String aes = "2964898070a1a5edfea7e880db767090a676bcc058b686ea9496b30d88e17a3a";
        String aesEncHex = String.format(keyFormat, jasyptEncryptor.encrypt(aes));
        String aesDecHex = jasyptEncryptor.decrypt(aesEncHex);
        System.out.println(String.format("AES[%s]enc:ENC(%s),dec:%s", aes, aesEncHex, aesDecHex));

        String rsaPri =
            "308204bd020100300d06092a864886f70d0101010500048204a7308204a30201000282010100a245bdd60d5d482505c06ece4355ddb77c3e8f17b42aa68a0cf54adb3aaf599dba8a2e657614e82033614838ecb4ce6bd10434f3f92709747a2372fa249adb5e9988d43680b0a42f60cc6a4ea82a13c3e6d4b47185084cebabed7eb5ea666b71b8ce08daf7142d9bbe05f95d985380b15e035e0cf150d798dbe1fbff2e8988412f39911cb29d37fef276f942d7430510f586411494794b1c63ba49c2c77e5ab9c1d8a72f905aa3799a2be43720c18dc0cacd97e7a20e44e3984b8fb06573d195a06d4e0f6ebed502964d9dc0e5fffd048a949142c993718ea5f2e820058128172f743aa2dde591eff6a9a47f4b75f431c89356817772b14b57783c87b0ce72d90203010001028201000687e74825454cf5257e3d4f6a9d1af56aa81366fd32aebd75604d6d15976f544668cf8d78934694de3083013c8099e2b3b8b73f5ef693de36d8b4a4cde29823de6ac7fcb210f9e8a8bd0d65c145a1424408e942bcdb8cc9fae34df3591dda62eb30caa1326ac2dba6ea1363519141c791acb1c53454a03ab815c82838e9a2dcc951fd0b7b3cf127abcf1fd15bc7ba1a241e513d7b14874983109212e7bb9746a64ae2edcece24d9c4a46a009a617f87d815bcd15de87e433251194a35fdac7804797dea417046e07b94ca98be32ea6323301e6421462b838bd66b0ea74202f06eccfa7a8dadf90ce8c84d49a23e187919a5e4e437534fd39b6f9d28229d01b102818100d6798e809c8397654c214cb87589fca43901328d285926cd4cb2afcc294aa85274980c5a4844aef9403131e7f302ce398f876269fbd1d84add1a5ea8f888a6cea2f22d6632dd331b7d2ca0f1aaad8c87044008fb06ddf84d75d4c46326061adfc7416d50870af406d0c6be6e72f9a3558ea548f6b5017c9eb83749bd5b0d76a902818100c1b0c5f09b8f99304924b562f476f5ec86e6588adc6e873afa25b01207f5439d264e037a66bf924391f9c2ac82a5e5e4a9766207a47a738ac26b22e6c3462fac194656db039c030e4d16a23a4b172d80f368d8922384b600e3d599858b4dda3b02e1297f99db6e7f689179233f455f6fab1c6e20983207f9ac0819b2bc6d28b102818100cee6ef51252c5146189e5b3bebb015387c01aab9c03dd90fdeb8d69cf70c9dbb05dd94b517d8a28fb39c81cf8880a15fa815ec1be021bd2de384c7e2ebf8302b51c89d8164fa3d8e7c402c7756b71bc5389569d478f873cc8e2a96d62b5d625995088a23505381bc7d75ed49f1c2e00dee918704f0f3213ca0ec3d47dde9ec190281800f6fae26c1cf0cef5b34f0bbc52de1e58acebac9be4d94bb6f8a2761187f447736c14ae7ab5ea3227d74c13e0c5f7a55e1e78c627d8a40c1134384918d887f6e20c9894c851a1ee839de8ee94468c178a06d7fad0de59ab7b170a97e8a1e9740d2b6ed1ebd3d4eae1ac0553e28e1b77c67cfa93b31c5f2fac3ea155a38481ed102818052f3c6df1836bba2af6a6d5894414318ff2b6ddfee689e92d18fe53e5e6ef2d64e988bef49aa51d5ad86e7bf93fa8a879abef7a502f6e9a5b5272e291a3357d902b4ba59819edbccd52bb6cfdddc418708ac0cbe3c4a0e90072d803bddaaf01b2219697b73ebdf46aa24131e222005626926a0191e4405ab4929b3d1b9fe39b7";
        String rsaPub =
            "30820122300d06092a864886f70d01010105000382010f003082010a0282010100a245bdd60d5d482505c06ece4355ddb77c3e8f17b42aa68a0cf54adb3aaf599dba8a2e657614e82033614838ecb4ce6bd10434f3f92709747a2372fa249adb5e9988d43680b0a42f60cc6a4ea82a13c3e6d4b47185084cebabed7eb5ea666b71b8ce08daf7142d9bbe05f95d985380b15e035e0cf150d798dbe1fbff2e8988412f39911cb29d37fef276f942d7430510f586411494794b1c63ba49c2c77e5ab9c1d8a72f905aa3799a2be43720c18dc0cacd97e7a20e44e3984b8fb06573d195a06d4e0f6ebed502964d9dc0e5fffd048a949142c993718ea5f2e820058128172f743aa2dde591eff6a9a47f4b75f431c89356817772b14b57783c87b0ce72d90203010001";
        String rsaPriEncHex = String.format(keyFormat, jasyptEncryptor.encrypt(rsaPri));
        String rsaPriDecHex = jasyptEncryptor.decrypt(rsaPriEncHex);
        System.out.println(String.format("RSA.pri[%s]enc:ENC(%s),dec:%s", rsaPri, rsaPriEncHex, rsaPriDecHex));
        String rsaPubEncHex = String.format(keyFormat, jasyptEncryptor.encrypt(rsaPub));
        String rsaPubDecHex = jasyptEncryptor.decrypt(rsaPubEncHex);
        System.out.println(String.format("RSA.pub[%s]enc:ENC(%s),dec:%s", rsaPub, rsaPubEncHex, rsaPubDecHex));

        String rsaPri2 =
            "308204bf020100300d06092a864886f70d0101010500048204a9308204a502010002820101009bd6eb95af81056c78c94d056ce868008a115a6adadf2b3002310fcf262b5a5dae705089086d6b6f4f885159953045db0fe86371a3097c9b469eed7e54a053d56be3129d645322a816724e135333e2249b7656cf868799c0e0ce51df6ab7e7ac2ae38f0a22f4cdb96d3c5c632339a9a8f74d1e9a412ef6d8c6fc883b90ed20d2b1702256efb878684dd316abac92d0f4c54d312edf183d1db027243012d9788fce3f87d53a673dbf52b6b365bc812eecb9807c62fee1dd2a1013cf6b46fd1d757eb52513178a5f3449a245e4b128d77f8cd506163789ec4e4cde2ec4971f7921bd26a40803a235ba77dfc3734451058f1e85af7fe4619f62dd9408f6c2016c5d0203010001028201000adabda67ba6b02ec5bbf9ac24d18c3aeb62f98a034c338920c1f390b15f282869c7c3684408c108ac00b9efd42b1c567d856975c70e185a8560f8f84963306ba75bd5d656204f43e76a574353d283901aaef3ad7938952e40ca461c1a660c522adb964135bd98ddaf3cdacf81ead8851f12854ce7b8e273276afbff3021401da8f0823b45699896feb2aaf95f42cef49bc9c32da2b53c0c4823dca165fc24b88f3f20aecfd72a7688fc55c18d710e2d501d0b48ee797bd2b7c8476af8d80b2b6a9e740b6bca1f801bdbb1a02389df0c64e5e699074cba3b89d0c050bc5057668ee945aaeb0d1e7b404c256a8036b1e0d8746fc226b0b5dc210d8ab8b357605902818100c934b3804e2c21ec535a3cf3199973d68dfef777588cd6047607c34c1339bd3a61b5532e9c1681b1038787e73cf0591cc221b995b0b7c98067c84476159c4b5c1834027f4188ea635d0ca169dde1bfc99cedd68cbd074b5654d05b0e55897ff5f48a86d516248316d472829bd2baec2cd53838bf7096e32715fd61d6d745a5a902818100c64773f446bb1fc1dab8773d27138ab7a0e6ac0baed1932d93011f01fef2d900f039488c9f85baa0e90d651fe7b09955dc10f78c63d4ace5324c8354a130d055ba77654b42df4cd8403322b002831c039d14e0f1319e10a7a82d567424726b5c064f38a786167dbf49001b1aadbcbc04eeecbd166dc5593e6f3531f1208699950281810099dc9737928fe511175c762760781c41022ceb88744a9e8ea2c3a4f0d3f2df6579ba7375bd1ee8e63850b7f8787d4367de7c73b2a884a2ae72ae8ecbce12cafe0df417c4c094b6c86d2b6f73c99d0c505c94f3f083ccc42bac87f859a9c78ff6c19dfd258ddd35f18b5c55cc5b055dfd9abf7785cdcf54bd5aef7c9611e0cca102818100b9cfac32773655046ddc00a2264481f2a3ae87fc4acfcb85220622f0d3e2f0c99855964f720ef85e630852841bb3bb7e62c4e3b784b68170283adbb82b767b465b801844f75e1bbd6c2c7f8d424d6bab574181ab863c028f9b632169a5de340e013bac74118c723b184629204f405752a834e2de69f04f39db2d96a7c93b5a2102818100b0d72fccb5d631dc1619a86f7bf34e6f5d93d57114e602a18ee4f168235e900363a5e4f5e9a23662e10c781864dcb51aed69870cf455fbd042928ac445de347b4071cd6b4207616d6c0d3be6061f6ca9dcf907e5f0c979aad97edc59eb73205878cf22429fd286dea2b8e73c2425f5b1109a4dcb568d2e20a649a5f52fc416a7";
        String rsaPub2 =
            "30820122300d06092a864886f70d01010105000382010f003082010a02820101009bd6eb95af81056c78c94d056ce868008a115a6adadf2b3002310fcf262b5a5dae705089086d6b6f4f885159953045db0fe86371a3097c9b469eed7e54a053d56be3129d645322a816724e135333e2249b7656cf868799c0e0ce51df6ab7e7ac2ae38f0a22f4cdb96d3c5c632339a9a8f74d1e9a412ef6d8c6fc883b90ed20d2b1702256efb878684dd316abac92d0f4c54d312edf183d1db027243012d9788fce3f87d53a673dbf52b6b365bc812eecb9807c62fee1dd2a1013cf6b46fd1d757eb52513178a5f3449a245e4b128d77f8cd506163789ec4e4cde2ec4971f7921bd26a40803a235ba77dfc3734451058f1e85af7fe4619f62dd9408f6c2016c5d0203010001";
        String rsaPriEncHex2 = String.format(keyFormat, jasyptEncryptor.encrypt(rsaPri2));
        String rsaPriDecHex2 = jasyptEncryptor.decrypt(rsaPriEncHex2);
        System.out.println(String.format("RSA.pri[%s]enc:ENC(%s),dec:%s", rsaPri2, rsaPriEncHex2, rsaPriDecHex2));
        String rsaPubEncHex2 = String.format(keyFormat, jasyptEncryptor.encrypt(rsaPub2));
        String rsaPubDecHex2 = jasyptEncryptor.decrypt(rsaPubEncHex2);
        System.out.println(String.format("RSA.pub[%s]enc:ENC(%s),dec:%s", rsaPub2, rsaPubEncHex2, rsaPubDecHex2));

        String pgpPri =
            "9503c60464590d75010800a65d905098f3de057ebee9177a6a54ff24bf07b90c4c41e182ea6cbde227f2cfc90eaa49912c5178b71b19225ff5960e060b05f5f89c272eb2517e85e38de406613f18d1d5b2c058ddc626a45a30a9a8d1eebdcae38ef3e471e3566432a2b5bb1f43d2b767aa12d1a749017ffbb96ac51ebcd5d89502fa40972670a7d9d36c3d453b83139ab3f48911187aeeb2d29d2390601676e1ac63053858e771befd48f83b0326970964b295dd61969df985dbc8db9eed6aa1ae2b5ad1c5c67f46c24f6866e3a75568d6aca4823d094456e33567066437120700c021275a9df909198c1b32d01c8eba76c80a2464b14ca0ac64085e6b583067e94b38ec23b33acee9e5bf0011010001fe090308a5da5479b725c881c04f56fc93eb4ddc5607f2bf9ad5526230eafc26dd2161b97d85fc6c3229cfea439ebef698447a91828ca54f406aae544aa63f0107305b19d0fa76a1c0915400cc2660299b3df754b65889bc8252998e75116b864b51eb1e94f0e06c72a04316297226be1d0147a516fc9ec516ec6a9cd237cbdbb1a2ed994e6f03e1f1a48396f3e560d8e006b95ce8b80edba2bf9cf4e08e17a7a37706cd2362f5ea961f8c8122a183d05ce7a94ca3f9d73c61e29854ee3285c07e00eb016ef2ec7acc93a7f691805792361a14c9b0c76cdd5d58d9357dd7ef0f86b22d722278a7f640599a5b5b388a572f9a1bbb53ef388992c7deef5360d78e8f9368d87341153931a34f48ed902f5a622122f530cac50d95d21bb8383ef1601fb7196170c949a0cd7608113f98307d3449319e881558cf58229bd5768e43088ccd68d2426ac3f66f87da4676bd5743de6ad4f179668d923b364af82792c3a3fa8ac265a300f3894f118656a8fb2e29d22501c11eaf1e416dc73bb6ac62aee1488e4f30e99607849578d3fcc48f6902d9534434e9501841061050bd3d3e212554f128fb67366402756273932227fb2fbc44b4e2b9fb8229e473bc187415419942d1b0596df9d96009d3f8d7b2c64a0d6a53f4b10029277e54a0a3b1752b07d69b1341c6a5f7c9943104cc1144fe943b5e479f43500d2bec639989ba8601d27b7a0a567ae3f98b372405e8ee2332ea953178587025bc8e7312cf83cc5f08b9c758a7d5891ed46e25226a8f89254e1c46765dab13f9f534b25194c427860457d8c311c5e00df199d140327c1cdddf242c5b357a31325375fad850d89556ad9cc6b02257430586c61542f4d3571777cc812c598dda78c66089ba34224f56e03423ae428b4d92319609917c07007f58201cb4d1ee68804b3032783ebe474f985086aca8f5ad1435fb55fcc4224113f4edae04b062a142dbd34d6bb40970677055736572303189013304130102001d050264590d76021b03020b090315080a03160102021e01050987f06d38000a0910923ba0ece8e6a901b88d07fc0d69754f4e6bba3f70db6ad47ae4ac26734e7849e5d4f41b37e8a1f588f36731bb825532a8ec012b6ad251fa8fad0133f7048a84ec2fc078ded2c54198b05f8624b756db55a96a27b2febd2347e5e0ed539b2f5dc14f5c114137bab7edbb7f8397f2f43e74605a24be9f593be05aa9473d93f27decf24664348aa4dd9144fa60f8c6134ec28abc129c68fa53bfcc87f1ddd44bb5885263f5ee7252941583e3b09f7bfa011e168e02109757da4f056d9ceed88c9376ce562391ff6c6bae4c05184998907446bc653551d8a1618481999f12b095f2895423bf062b3ecc6b4d33dfc20c33a1abdcf1f5da83fdf75b719ab3e26c8185d0e63d98e18f7c9e04b1625e";
        String pgpPub =
            "99010d0464590d75010800a65d905098f3de057ebee9177a6a54ff24bf07b90c4c41e182ea6cbde227f2cfc90eaa49912c5178b71b19225ff5960e060b05f5f89c272eb2517e85e38de406613f18d1d5b2c058ddc626a45a30a9a8d1eebdcae38ef3e471e3566432a2b5bb1f43d2b767aa12d1a749017ffbb96ac51ebcd5d89502fa40972670a7d9d36c3d453b83139ab3f48911187aeeb2d29d2390601676e1ac63053858e771befd48f83b0326970964b295dd61969df985dbc8db9eed6aa1ae2b5ad1c5c67f46c24f6866e3a75568d6aca4823d094456e33567066437120700c021275a9df909198c1b32d01c8eba76c80a2464b14ca0ac64085e6b583067e94b38ec23b33acee9e5bf0011010001b40970677055736572303189013304130102001d050264590d76021b03020b090315080a03160102021e01050987f06d38000a0910923ba0ece8e6a901b88d07fc0d69754f4e6bba3f70db6ad47ae4ac26734e7849e5d4f41b37e8a1f588f36731bb825532a8ec012b6ad251fa8fad0133f7048a84ec2fc078ded2c54198b05f8624b756db55a96a27b2febd2347e5e0ed539b2f5dc14f5c114137bab7edbb7f8397f2f43e74605a24be9f593be05aa9473d93f27decf24664348aa4dd9144fa60f8c6134ec28abc129c68fa53bfcc87f1ddd44bb5885263f5ee7252941583e3b09f7bfa011e168e02109757da4f056d9ceed88c9376ce562391ff6c6bae4c05184998907446bc653551d8a1618481999f12b095f2895423bf062b3ecc6b4d33dfc20c33a1abdcf1f5da83fdf75b719ab3e26c8185d0e63d98e18f7c9e04b1625e";
        String pgpPwd = "p0g1p2U4!";
        String pgpPriEncHex = String.format(keyFormat, jasyptEncryptor.encrypt(pgpPri));
        String pgpPriDecHex = jasyptEncryptor.decrypt(pgpPriEncHex);
        System.out.println(String.format("PGP.pri[%s]enc:ENC(%s),dec:%s", pgpPri, pgpPriEncHex, pgpPriDecHex));
        String pgpPubEncHex = String.format(keyFormat, jasyptEncryptor.encrypt(pgpPub));
        String pgpPubDecHex = jasyptEncryptor.decrypt(pgpPubEncHex);
        System.out.println(String.format("PGP.pub[%s]enc:ENC(%s),dec:%s", pgpPub, pgpPubEncHex, pgpPubDecHex));
        String pgpPwdEncHex = jasyptEncryptor.encrypt(pgpPwd);
        String pgpPwdDecHex = jasyptEncryptor.decrypt(pgpPwdEncHex);
        System.out.println(String.format("PGP.pwd[%s]enc:ENC(%s),dec:%s", pgpPwd, pgpPwdEncHex, pgpPwdDecHex));
    }

    /**
     * SM4对称加密算法
     */
    BaseSingleEncryption encryption = new Sm4SecureEncryption();
    String key = "0e8a31e0e582a561a5c4d2c6737ba962";
}