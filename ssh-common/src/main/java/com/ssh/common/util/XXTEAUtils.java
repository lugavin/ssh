package com.ssh.common.util;

import org.apache.commons.codec.binary.Base64;

/**
 * XXTEA加密解密工具类
 *
 * @see <a href="https://en.wikipedia.org/wiki/XXTEA" target="_top">XXTEA</a>
 */
public abstract class XXTEAUtils {

    /**
     * 使用密钥加密数据
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        if (data.length == 0) {
            return data;
        }
        return toByteArray(encrypt(toIntArray(data, true), toIntArray(key, false)), false);
    }

    /**
     * 使用密钥解密
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        if (data.length == 0) {
            return data;
        }
        return toByteArray(decrypt(toIntArray(data, false), toIntArray(key, false)), true);
    }

    /**
     * 使用密钥加密数据
     */
    public static int[] encrypt(int[] data, int[] value) {
        int n = data.length - 1;
        if (n < 1) {
            return data;
        }
        if (value.length < 4) {
            int[] key = new int[4];

            System.arraycopy(value, 0, key, 0, value.length);
            value = key;
        }
        int z = data[n], y, delta = 0x9E3779B9, sum = 0, e;
        int p, q = 6 + 52 / (n + 1);
        while (q-- > 0) {
            sum = sum + delta;
            e = sum >>> 2 & 3;
            for (p = 0; p < n; p++) {
                y = data[p + 1];
                z = data[p] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (value[p & 3 ^ e] ^ z);
            }
            y = data[0];
            z = data[n] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (value[p & 3 ^ e] ^ z);
        }
        return data;
    }

    /**
     * 使用密钥解密数据
     */
    public static int[] decrypt(int[] data, int[] key) {
        int n = data.length - 1;
        if (n < 1) {
            return data;
        }
        if (key.length < 4) {
            int[] k = new int[4];

            System.arraycopy(key, 0, k, 0, key.length);
            key = k;
        }
        int z, y = data[0], delta = 0x9E3779B9, sum, e;
        int p, q = 6 + 52 / (n + 1);
        sum = q * delta;
        while (sum != 0) {
            e = sum >>> 2 & 3;
            for (p = n; p > 0; p--) {
                z = data[p - 1];
                y = data[p] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (key[p & 3 ^ e] ^ z);
            }
            z = data[n];
            y = data[0] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (key[p & 3 ^ e] ^ z);
            sum = sum - delta;
        }
        return data;
    }

    /**
     * 字节数组转换为整型数组
     */
    private static int[] toIntArray(byte[] data, boolean includeLength) {
        int n = (((data.length & 3) == 0) ? (data.length >>> 2) : ((data.length >>> 2) + 1));
        int[] result;
        if (includeLength) {
            result = new int[n + 1];
            result[n] = data.length;
        } else {
            result = new int[n];
        }
        n = data.length;
        for (int i = 0; i < n; i++) {
            result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
        }
        return result;
    }

    /**
     * 整型数组转换为字节数组
     */
    private static byte[] toByteArray(int[] data, boolean includeLength) {
        int n = data.length << 2;
        if (includeLength) {
            int m = data[data.length - 1];
            if (m > n) {
                return null;
            } else {
                n = m;
            }
        }
        byte[] result = new byte[n];
        for (int i = 0; i < n; i++) {
            result[i] = (byte) ((data[i >>> 2] >>> ((i & 3) << 3)) & 0xff);
        }
        return result;
    }

    /**
     * 先XXXTEA加密，后Base64加密
     */
    public static String encrypt(String data, String key) {
        byte[] binaryData = encrypt(data.getBytes(), key.getBytes());
        String cipher = new String(Base64.encodeBase64(binaryData));
        cipher = cipher.replace('+', '-');
        cipher = cipher.replace('/', '_');
        cipher = cipher.replace('=', '.');
        return cipher;
    }

    /**
     * 先Base64解密，后XXXTEA解密
     */
    public static String decrypt(String data, String key) {
        data = data.replace('-', '+');
        data = data.replace('_', '/');
        data = data.replace('.', '=');
        return new String(decrypt(Base64.decodeBase64(data), key.getBytes()));
    }

}
