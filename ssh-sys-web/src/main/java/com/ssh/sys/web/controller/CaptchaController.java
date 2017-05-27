package com.ssh.sys.web.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.ssh.common.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.Date;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private Producer kaptchaProducer;

    /**
     * Ref: com.google.code.kaptcha.servlet.KaptchaServlet
     */
    @RequestMapping(value = "/getCaptcha", method = RequestMethod.GET)
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String capText = kaptchaProducer.createText();
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_DATE, new Date());
        BufferedImage bi = kaptchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/validateCaptcha")
    public ModelMap validateCaptcha(@RequestParam String captcha, HttpSession session) {
        return new ModelMap(Constant.REMOTE_VALIDATION_KEY, captcha.equalsIgnoreCase((String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY)));
    }

}
