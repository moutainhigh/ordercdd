package com.liyang.controller;

import com.liyang.service.CaptchaService;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CaptchaController {
    @Autowired
    CaptchaService captchaService;

    @GetMapping(value = "/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response){

        try {
            captchaService.generateCaptcha(request, response);
        } catch (IOException e) {
            throw new FailReturnObject(1532,"获取图形验证码失败！！", ReturnObject.Level.ERROR);
        }

    }
}
