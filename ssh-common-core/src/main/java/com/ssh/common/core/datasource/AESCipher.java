package com.ssh.common.core.datasource;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AESCipher {

    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String ALGORITHM_NAME = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String IV_STRING = "16-Bytes--String";

    /**
     * @param content Original message content
     * @param key     16 Bytes Length Key
     * @return Encrypt message content
     */
    public static String encrypt(String content, String key)
            throws UnsupportedEncodingException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] byteContent = content.getBytes(DEFAULT_ENCODING);
        // byte[] enCodeFormat = key.getBytes();
        byte[] enCodeFormat = getMd5Hash(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, ALGORITHM_NAME);
        byte[] initParam = IV_STRING.getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(byteContent);
        return DatatypeConverter.printBase64Binary(encryptedBytes);

    }

    /**
     * @param content Encrypt message content
     * @param key     16 Bytes Length Key
     * @return Original message content
     */
    public static String decrypt(String content, String key)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        byte[] encryptedBytes = DatatypeConverter.parseBase64Binary(content);
        // byte[] enCodeFormat = key.getBytes();
        byte[] enCodeFormat = getMd5Hash(key);
        SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, ALGORITHM_NAME);
        byte[] initParam = IV_STRING.getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] result = cipher.doFinal(encryptedBytes);
        return new String(result, DEFAULT_ENCODING);
    }

    public static byte[] getMd5Hash(String source) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(source.getBytes());
        return md5.digest();
    }

    // public static void main(String[] args) throws Exception {
    //     String encryptPass = AESCipher.encrypt("root", "CHINESE SOFTWARE");
    //     String decryptPass = AESCipher.decrypt(encryptPass, "CHINESE SOFTWARE");
    //     System.out.println(encryptPass);
    //     System.out.println(decryptPass);
    // }

}
