package top.youm.test;

import top.youm.maple.utils.math.RsaUtil;

import java.util.Map;

/**
 * @author YouM
 * Created on 2023/8/27
 */
public class Test {
    public static void main(String[] args) {
        Map<String, String> keys = RsaUtil.generateKey();
        String publicKeyStr = keys.get("publicKeyStr");
        String privateKeyStr = keys.get("privateKeyStr");
        try {
            String crypt = RsaUtil.encryptByPublicKey("1", publicKeyStr);
            System.out.println(crypt);
            System.out.println(RsaUtil.decryptByPrivateKey(crypt, privateKeyStr));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
