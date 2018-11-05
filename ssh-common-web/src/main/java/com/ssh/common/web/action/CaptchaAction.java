package com.ssh.common.web.action;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.web.base.AbstractAction;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.Date;

@Controller
@Scope("prototype")
public class CaptchaAction extends AbstractAction {

    public static final String FORM_VALIDATION_VALID = "valid";

    @Autowired
    private Producer kaptchaProducer;

    private String captcha;

    /**
     * @see com.google.code.kaptcha.servlet.KaptchaServlet#doGet
     */
    public String getCaptchaImage() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        // Set to expire far in the past.
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");
        // create the text for the image
        String capText = kaptchaProducer.createText();
        // store the text in the session
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        // store the date in the session so that it can be compared
        // against to make sure someone hasn't taken too long to enter
        // their kaptcha
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_DATE, new Date());
        // create the image with the text
        BufferedImage bi = kaptchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        // write the data out
        ImageIO.write(bi, "jpg", out);
        return NONE;
    }

    public String validateCaptcha() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        String kaptcha = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        ModelMapDTO dto = new ModelMapDTO();
        dto.put(FORM_VALIDATION_VALID, captcha.equals(kaptcha));
        return writeJSON(dto);
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

}
