package com.ssh.common.web.captcha;

import java.awt.image.BufferedImage;

public interface CaptchaService {

    /**
     * 生成32位随机数
     *
     * @return 32位随机数
     */
    String genToken();

    /**
     * 生成验证码
     *
     * @return 验证码
     */
    String genCaptcha();

    /**
     * 根据验证码生成验证码图片
     *
     * @param captcha 验证码
     * @return 验证码图片
     */
    BufferedImage getCaptchaImage(String captcha);

    /**
     * 根据token获取验证码
     *
     * @param token 验证码token
     * @return 验证码
     */
    String getCaptcha(String token);

    /**
     * 校验图片验证码是否正确
     *
     * @param token   图片验证码token
     * @param captcha 图片验证码
     * @return 图片验证码是否正确
     * @throws Exception
     */
    boolean verify(String token, String captcha) throws Exception;

}
