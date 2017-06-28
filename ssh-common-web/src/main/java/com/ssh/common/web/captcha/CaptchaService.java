package com.ssh.common.web.captcha;

import com.ssh.common.exception.BusinessException;

import java.awt.image.BufferedImage;

public interface CaptchaService {

    /**
     * 生成验证码令牌
     */
    String genToken();

    /**
     * 根据token获取验证码
     *
     * @param token 被校验的token
     * @return token中的验证码字符串
     * @throws BusinessException
     */
    String getCaptcha(String token) throws BusinessException;

    /**
     * 根据验证码生成验证码图片
     *
     * @param captcha 验证码
     * @return 验证码图片
     */
    BufferedImage getCaptchaImage(String captcha);

    /**
     * 验证输入的验证码与令牌是否匹配
     *
     * @param token   验证码令牌
     * @param captcha 验证码
     * @throws BusinessException
     */
    boolean doVerify(String token, String captcha) throws BusinessException;

    /**
     * 获取XXTEA加密解密的密钥
     *
     * @return XXTEA加密解密的密钥
     */
    String getSecKey();

    /**
     * 获取验证码失效时限(秒)
     *
     * @return 验证码失效时限(秒)
     */
    int getMaxAge();

    /**
     * 获取缓存前缀
     *
     * @return 缓存前缀
     */
    String getCachePrefix();

    /**
     * 获取验证码字符数
     *
     * @return 验证码字符数
     */
    int getCharLength();

}
