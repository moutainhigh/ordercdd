package com.liyang.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class CaptchaService {
    @Autowired
    DefaultKaptcha captchaProducer;

    public String generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control",
                "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();
        request.getSession().setAttribute("captcha", capText);
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return capText;
    }

    public boolean validate(HttpServletRequest request) {
        String trueCaptcha = (String) request.getSession().getAttribute("captcha");
        if (trueCaptcha == null) {
            return false;
        }
        String reqCaptcha = request.getParameter("captcha");
        boolean result = reqCaptcha != null && trueCaptcha.equalsIgnoreCase(reqCaptcha);
        if(result){
            request.getSession().removeAttribute("captcha");
        }
        return result;
    }
}
