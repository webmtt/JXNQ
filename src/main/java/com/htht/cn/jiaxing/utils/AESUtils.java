package com.htht.cn.jiaxing.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @version V1.0
 * @desc AES 加密工具类
 */
@Slf4j
public class AESUtils {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法
    private static final String PASSWORD_KEY = "zw_make_this__________";//解密秘钥

    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(PASSWORD_KEY));// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密

            return Base64.encodeBase64String(result).replace("\r","").replace("\n","");//通过Base64转码返回
        } catch (Exception ex) {
            log.error("加密错误",ex);
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @return
     */
    public static String decrypt(String content) {

        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(PASSWORD_KEY));

            //执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));

            return new String(result, "utf-8");
        } catch (Exception ex) {
            log.error("解密错误",ex);
        }

        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;

        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
            secureRandom.setSeed(password.getBytes());
            //AES 要求密钥长度为 128
            kg.init(128, secureRandom);

            //生成一个密钥
            SecretKey secretKey = kg.generateKey();

            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            log.error("生成密钥错误",ex);
        }

        return null;
    }

    public static void main(String[] args) {
        String s = "123456";
        System.out.println("s:" + s);
        String s1 = AESUtils.encrypt(s);
        System.out.println("s1:" + s1);
        System.out.println("s2:"+AESUtils.decrypt(s1));
    }

}