package com.ssh.common.web.controller;

import com.ssh.common.util.Constant;
import com.ssh.common.web.base.ResponseData;
import com.ssh.common.web.captcha.CaptchaService;
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
import java.awt.image.BufferedImage;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @ResponseBody
    @RequestMapping("/genToken")
    public ResponseData genToken() {
        return new ResponseData().setData(captchaService.genToken());
    }

    /**
     * @see com.google.code.kaptcha.servlet.KaptchaServlet#doGet
     */
    @RequestMapping(value = "/getCaptcha", method = RequestMethod.GET)
    public void getCaptcha(@RequestParam("token") String token, HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String capText = captchaService.getCaptcha(token);
        BufferedImage bi = captchaService.getCaptchaImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
    }

    @ResponseBody
    @RequestMapping("/verify")
    public ModelMap verify(@RequestParam("token") String token, @RequestParam("captcha") String captcha) {
        try {
            return new ModelMap(Constant.REMOTE_VALIDATION_KEY, captchaService.verify(token, captcha));
        } catch (Exception e) {
            return new ModelMap(Constant.REMOTE_VALIDATION_KEY, Boolean.FALSE);
        }
    }

}
