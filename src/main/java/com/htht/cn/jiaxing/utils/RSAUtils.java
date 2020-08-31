package com.htht.cn.jiaxing.utils;

import javax.crypto.Cipher;
import java.io.File;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class RSAUtils {

    /**
     * 缺省的2048位密钥对,可处理245个字节(81个汉字)的数据
     */
    //用与页面传输的私钥
    public static String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALZvROs/EFRGD2NPtFzABfs8m" +
            "+SthGHnxoNXev5s9Wg0f9nSFuL4RXCjs4IlvlPtjxiD48mPJ+Vu55x9JDWNAfdPS7EW6V7/Wz87WlDfmA4nTbUgVAzipCAl+3Uc83dx" +
            "YTnjeBYIYau6pJ4GVPl4cTY3oi8AvEYYZVS3IYj4Dg3jAgMBAAECgYAxXmURICzEEX2Ci4QsgU5dEabCOG6VrAodauP+y++RNnvOrzr" +
            "ASx1HgRVHx/z8a1II+VAAMHxEa0HEKF1PHIyGMmpdqbApeXWkgFWZssGNXsh9KyDdv+uko3HwM4iw7fMRVc9WEygAQKJoA9IFSYQHO68VU" +
            "vzTQrm4Blv0VxUr8QJBAN8Gu+4OCA2iKsl4TJmHOw0A2xB3fEVcqtpH+Uqt4S1Me+5qjID4DSvIsjzsZGjfSkXED0HwqZEXb3stm3YX/yk" +
            "CQQDRaDET0EBb/WK11xMfNCxn1MjNtOoZLmQ0JlTeoSd8C2YNLZWAr1VOmb4rct9oCTMRokq7HMKKqvll/LGpJOIrAkACTOt7VSG4IkgWs" +
            "+r2WnK9e5jG3rSlcoNApSVpI8oC0Zslpmu0mNpp7TQJdNbETilR9ufBYl4dZQR9XMcRvMWhAkEAjhovW6uxUPtk1dLqhf1IuPkbfwRq68W" +
            "geAcfBqjDe5jcmsq5LkdNE3afJyrf0eCUSD4iilEc7y7Q6VWOViLSWwJAT5aEcOWTybwzywZgOyDhfVO5tCxTO8ygcBtSkbAKNLpBy4LAQ" +
            "hfSGtHq51D7EmhbyOCjKIPCWcnhUKL5kgcCzg==";

    //用与页面传输的公钥
    public static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2b0TrPxBURg9jT7RcwAX7PJvkrYRh58aDV3r+bPVoNH/Z0hbi+EVwo7OCJb5T7Y8Yg+PJjyflbuecfSQ1jQH3T0uxFule/1s/O1pQ35gOJ021IFQM4qQgJft1HPN3cWE543gWCGGruqSeBlT5eHE2N6IvALxGGGVUtyGI+A4N4wIDAQAB";    /**
     * 字符集
     */
    public static String CHARSET = "utf-8";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_INSTANCE = "SHA1WithRSA";

    /**
     * 生成密钥对
     * @param keyLength
     * @return
     * @throws Exception
     */
    private static KeyPair getKeyPair(int keyLength) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        // 加密随机数生成器 (RNG)
        SecureRandom random = new SecureRandom();
        // 重新设置此随机对象的种子
        String currentDateTime = new SimpleDateFormat("yyyyHHSSS")
                .format(new Date());
        random.setSeed(currentDateTime.getBytes());

        keyPairGenerator.initialize(keyLength,random);
        return keyPairGenerator.generateKeyPair();
    }

    public static void main(String[] args) throws Exception {
//
        String s = encryptByPublicKey("13389937224");
        System.out.println(s);
        String s1 = decryptByPrivateKey(s);
        System.out.println(s1);
    }

    /**
     * 公钥字符串转PublicKey实例
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 私钥字符串转PrivateKey实例
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 公钥加密
     * @param content
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    public static byte[] encryptByPublicKey(byte[] content) throws Exception {
        return encryptByPublicKey(content, getPublicKey(PUBLIC_KEY));
    }

    public static String encryptByPublicKey(String content, String publicKey) throws Exception {
        return new String(Base64.getEncoder().encode(encryptByPublicKey(content.getBytes(CHARSET), getPublicKey(publicKey))));

    }

    public static String encryptByPublicKey(String content) throws Exception {
        return new String(Base64.getEncoder().encode(encryptByPublicKey(content.getBytes(CHARSET))));
    }

    /**
     * 私钥解密
     * @param content
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    public static byte[] decryptByPrivateKey(byte[] content) throws Exception {
        return decryptByPrivateKey(content, getPrivateKey(PRIVATE_KEY));
    }

    public static String decryptByPrivateKey(String content, String privateKey) throws Exception {
        return new String(decryptByPrivateKey(Base64.getDecoder().decode(content), getPrivateKey(privateKey)), CHARSET);

    }

    public static String decryptByPrivateKey(String content) throws Exception {
        content = content.replaceAll(" +","+");
        return new String(decryptByPrivateKey(Base64.getDecoder().decode(content)), CHARSET);
    }

    /**
     * 私钥加密
     * @param content
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    public static byte[] encryptByPrivateKey(byte[] content) throws Exception {
        return encryptByPrivateKey(content, getPrivateKey(PRIVATE_KEY));
    }

    public static String encryptByPrivateKey(String content, String privateKey) throws Exception {
        return new String(encryptByPrivateKey(content.getBytes(CHARSET), getPrivateKey(privateKey)), CHARSET);
    }

    public static String encryptByPrivateKey(String content) throws Exception {
        return new String(encryptByPrivateKey(content.getBytes(CHARSET)), CHARSET);
    }

    /**
     * 公钥解密
     * @param content
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] decrypByPublicKey(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    public static byte[] decrypByPublicKey(byte[] content) throws Exception {
        return decrypByPublicKey(content, getPublicKey(PUBLIC_KEY));
    }

    public static String decrypByPublicKey(String content, String publicKey) throws Exception {
        return new String(decrypByPublicKey(Base64.getDecoder().decode(content), getPublicKey(publicKey)), CHARSET);

    }

    public static String decrypByPublicKey(String content) throws Exception {
        return new String(decrypByPublicKey(Base64.getDecoder().decode(content)), CHARSET);
    }

    /**
     * 签名
     * @param content
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] content, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_INSTANCE);
        signature.initSign(privateKey);
        signature.update(content);
        return signature.sign();
    }

    public static byte[] sign(byte[] content) throws Exception {
        return sign(content, getPrivateKey(PRIVATE_KEY));
    }

    public static String sign(String content, String privateKey) throws Exception {
        return new String(Base64.getEncoder().encode(sign(content.getBytes(CHARSET), getPrivateKey(privateKey))), CHARSET);
    }

    public static String sign(String content) throws Exception {
        return new String(Base64.getEncoder().encode(sign(content.getBytes(CHARSET))), CHARSET);
    }

    /**
     * 验签
     * @param content
     * @param sign
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] content, byte[] sign,  PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_INSTANCE);
        signature.initVerify(publicKey);
        signature.update(content);
        return signature.verify(sign);
    }

    public static boolean verify(byte[] content, byte[] sign) throws Exception {
        return verify(content, sign, getPublicKey(PUBLIC_KEY));
    }

    public static boolean verify(String content, String sign, String publicKey) throws Exception {
        return verify(content.getBytes(CHARSET), Base64.getDecoder().decode(sign), getPublicKey(publicKey));
    }

    public static boolean verify(String content, String sign) throws Exception {
        return verify(content.getBytes(CHARSET), Base64.getDecoder().decode(sign), getPublicKey(PUBLIC_KEY));
    }
}

