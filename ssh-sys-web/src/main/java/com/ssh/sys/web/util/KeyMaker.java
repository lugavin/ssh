package com.ssh.sys.web.util;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * 生成唯一ID的工具类
 */
public class KeyMaker {

    private static SecureRandom random = new SecureRandom();

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static Long randomLong() {
        return Math.abs(random.nextLong());
    }

    public static String randomBase62(int length) {
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return Encodes.encodeBase62(randomBytes);
    }

}
