package com.ssh.common.util;

import com.ssh.common.subject.ActiveUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;

public abstract class SecurityHelper {

    private static final int DEFAULT_ITERATIONS = 1;

    public static String generateRandomNumber() {
        return SecurityHelper.generateRandomNumber(new byte[0]);
    }

    public static String generateRandomNumber(byte[] seed) {
        SecureRandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        if (seed != null && seed.length > 0) {
            randomNumberGenerator.setSeed(seed);
        }
        return randomNumberGenerator.nextBytes().toHex();
    }

    public static String generateMd5Hash(Object source, Object salt) {
        return SecurityHelper.generateMd5Hash(source, salt, DEFAULT_ITERATIONS);
    }

    public static String generateMd5Hash(Object source, Object salt, int hashIterations) {
        return new Md5Hash(source, salt, hashIterations).toString();
    }

    public static ActiveUser getActiveUser() {
        return (ActiveUser) SecurityUtils.getSubject().getPrincipal();
    }

}
