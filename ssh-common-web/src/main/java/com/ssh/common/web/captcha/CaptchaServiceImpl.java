package com.ssh.common.web.captcha;

import com.google.code.kaptcha.Producer;
import com.ssh.common.cache.CacheService;
import com.ssh.common.exception.BusinessException;
import com.ssh.common.util.XXTEAUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CaptchaServiceImpl implements CaptchaService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaServiceImpl.class);

    private static final String DEFAULT_CHAR_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final String DEFAULT_SEC_KEY = "Gavin Software";
    private static final String DEFAULT_CACHE_PREFIX = "captcha";
    private static final int DEFAULT_MAX_AGE = 600; // 验证码有效期(缓存有效期一定要比验证码有效期长一些)
    private static final int DEFAULT_CHAR_LENGTH = 4;

    private static Random random = new Random();

    private String secKey = DEFAULT_SEC_KEY;

    private int maxAge = DEFAULT_MAX_AGE;

    private String cachePrefix = DEFAULT_CACHE_PREFIX;

    private int charLength = DEFAULT_CHAR_LENGTH;

    private char[] chars = DEFAULT_CHAR_STRING.toCharArray();

    private CacheService cacheService;

    private Producer captchaProducer;

    @Override
    public String genToken() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charLength; i++) {
            int randInt = Math.abs(random.nextInt());
            sb.append(chars[randInt % chars.length]);
        }
        long timestamp = System.currentTimeMillis();
        String token = String.format("%s_%d", sb.toString(), timestamp);
        LOGGER.info("Unencrypted token: {}", token);
        return XXTEAUtils.encrypt(token, secKey);
    }

    @Override
    public String getCaptcha(String token) throws BusinessException {
        try {
            if (cacheService.get(genBlacklistCacheKey(token)) != null) {
                throw new BusinessException("此token已列入黑名单");
            }
            String plainText = XXTEAUtils.decrypt(token, secKey);
            if (StringUtils.isBlank(plainText)) {
                throw new BusinessException("解密失败,token可能遭到篡改");
            }
            String[] plainTextArr = plainText.split("_");
            if (plainTextArr.length != 2) {
                throw new BusinessException("token数据格式错误");
            }
            long timestamp;
            try {
                timestamp = Long.parseLong(plainTextArr[1]);
            } catch (NumberFormatException e) {
                throw new BusinessException("时间戳无效");
            }
            if ((System.currentTimeMillis() - timestamp) > TimeUnit.MILLISECONDS.convert(maxAge, TimeUnit.SECONDS)) {
                throw new BusinessException("验证码已过期");
            }
            return plainTextArr[0];
        } catch (Exception e) {
            cacheService.set(genBlacklistCacheKey(token), Boolean.TRUE);
            throw new BusinessException("校验token出错,token可能遭到篡改", e);
        }
    }

    @Override
    public BufferedImage getCaptchaImage(String captcha) {
        return captchaProducer.createImage(captcha);
    }

    @Override
    public boolean doVerify(String token, String captcha) throws BusinessException {
        String cacheKey = genVerifiedCacheKey(token);
        // 判断缓存中有没有此token
        if (cacheService.get(cacheKey) != null) { // 如果有则说明已经被验证过了
            throw new BusinessException("该验证码已经被验证过");
        } else { // 如果没有则将其放入到已验证缓存库中(防止重放攻击)
            cacheService.set(cacheKey, Boolean.TRUE);
        }
        return getCaptcha(token).equals(captcha);
    }

    @Override
    public String getSecKey() {
        return secKey;
    }

    public void setSecKey(String secKey) {
        this.secKey = secKey;
    }

    @Override
    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public String getCachePrefix() {
        return cachePrefix;
    }

    public void setCachePrefix(String cachePrefix) {
        this.cachePrefix = cachePrefix;
    }

    @Override
    public int getCharLength() {
        return charLength;
    }

    public void setCharLength(int charLength) {
        this.charLength = charLength;
    }

    public void setCharString(String charString) {
        this.chars = charString.toCharArray();
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setCaptchaProducer(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    /**
     * 生成token黑名单缓存key
     */
    private String genBlacklistCacheKey(String token) {
        return String.format("%s_token_black_%s", cachePrefix, token);
    }

    /**
     * 生成token已验证key
     */
    private String genVerifiedCacheKey(String token) {
        return String.format("%s_token_verified_%s", cachePrefix, token);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cacheService, "Property 'cacheService' is required.");
        Assert.notNull(captchaProducer, "Property 'captchaProducer' is required.");
    }

}
