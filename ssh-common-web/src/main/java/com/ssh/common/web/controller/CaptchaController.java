package com.ssh.common.web.controller;

import com.google.code.kaptcha.Producer;
import com.ssh.common.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaController.class);

    private static final int CAPTCHA_MAX_AGE = 300;

    @Autowired
    private Producer captchaProducer;

    /**
     * @see com.google.code.kaptcha.servlet.KaptchaServlet#doGet
     */
    @RequestMapping(value = "/getCapImage", method = RequestMethod.GET)
    public void getCapImage(final HttpSession session, final HttpServletResponse response) throws Exception {
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
        final String capText = captchaProducer.createText();
        // store the text in the session
        session.setAttribute(Constant.CAPTCHA_SESSION_KEY, capText);
        LOGGER.debug("Store captcha [{}] in the session.", capText);
        // store the date in the session so that it can be compared
        // against to make sure someone hasn't taken too long to enter
        // their kaptcha
        session.setAttribute(Constant.CAPTCHA_SESSION_DATE, new Date());
        // Remove the checkCode from the session after 5 minutes
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                session.removeAttribute(Constant.CAPTCHA_SESSION_KEY);
                session.removeAttribute(Constant.CAPTCHA_SESSION_DATE);
                LOGGER.debug("Remove captcha [{}] from session.", capText);
                timer.cancel();
            }
        }, TimeUnit.MILLISECONDS.convert(CAPTCHA_MAX_AGE, TimeUnit.SECONDS));
        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        // write the data out
        ImageIO.write(bi, "jpg", out);
    }

    @ResponseBody
    @RequestMapping("/verify")
    public ModelMap verify(@RequestParam String captcha, HttpSession session) {
        return new ModelMap(Constant.REMOTE_VALIDATION_KEY, captcha.equalsIgnoreCase((String) session.getAttribute(Constant.CAPTCHA_SESSION_KEY)));
    }

}
