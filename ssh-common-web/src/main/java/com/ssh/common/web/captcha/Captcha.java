package com.ssh.common.web.captcha;

import java.io.Serializable;
import java.util.Date;

public class Captcha implements Serializable {

    private static final long serialVersionUID = 1L;

    private String captcha;         // 验证码
    private Date createTime;        // 创建时间
    private int verifyCount = 0;    // 验证次数

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getVerifyCount() {
        return verifyCount;
    }

    public void setVerifyCount(int verifyCount) {
        this.verifyCount = verifyCount;
    }

}
