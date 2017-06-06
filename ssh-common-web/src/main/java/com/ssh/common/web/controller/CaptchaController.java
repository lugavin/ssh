package com.ssh.common.web.controller;

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
     * @see com.google.code.kaptcha.servlet.KaptchaServlet#doGet
     */
    @RequestMapping(value = "/getCaptcha", method = RequestMethod.GET)
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
    }

    @ResponseBody
    @RequestMapping(value = "/validateCaptcha")
    public ModelMap validateCaptcha(@RequestParam String captcha, HttpSession session) {
        return new ModelMap(Constant.REMOTE_VALIDATION_KEY, captcha.equalsIgnoreCase((String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY)));
    }

}
