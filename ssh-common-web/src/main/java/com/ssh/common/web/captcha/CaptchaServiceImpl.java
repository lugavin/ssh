package com.ssh.common.web.captcha;

import com.google.code.kaptcha.Producer;
import com.ssh.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.awt.image.BufferedImage;
import java.util.Calendar;

public class CaptchaServiceImpl extends AbstractCaptchaService implements CaptchaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaServiceImpl.class);

    private Producer captchaProducer;

    @Override
    public String getCaptcha(String token) {
        String capText = genCaptcha();
        Captcha captcha = new Captcha();
        captcha.setVerifyCount(0);
        captcha.setCaptcha(capText);
        captcha.setCreateTime(Calendar.getInstance().getTime());
        cacheService.set(token, captcha, maxAge);
        return capText;
    }

    @Override
    public BufferedImage getCaptchaImage(String captcha) {
        return captchaProducer.createImage(captcha);
    }

    @Override
    public boolean verify(String token, String captcha) throws Exception {
        Captcha cap = cacheService.get(token, Captcha.class);
        if (cap == null) {
            throw new BusinessException("该图片验证码已失效，请重新获取");
        }
        if (cap.getVerifyCount() > MAX_VERIFY_COUNT) {
            cacheService.delete(token);
            throw new BusinessException("该图片验证码已超过限制验证次数" + MAX_VERIFY_COUNT + "次，请重新获取");
        }
        cap.setVerifyCount(cap.getVerifyCount() + 1);
        cacheService.set(token, cap, maxAge);
        return cap.getCaptcha().equalsIgnoreCase(captcha);
    }

    public void setCaptchaProducer(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    @Override
    protected void checkCaptchaConfig() throws IllegalArgumentException {
        Assert.notNull(captchaProducer);
    }

}
