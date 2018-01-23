package com.liyang.service;

import com.liyang.aliyunsms.AliyunSmsService;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.regex.Pattern;

@Service
public class SmsService {

    @Autowired
    private AliyunSmsService aliyunSmsService;
    @Autowired
    private CacheManager cacheManager;
    @Value(value = "${spring.aliyun.sms.template}")
    private String smsTemplate;
    /**
     *
     * @param mobile 手机号
     * @return  验证码
     */
    public String  sendSms(String mobile)
    {
        if (mobile == null || !Pattern.matches("^1[\\d]{10}$",mobile)) {
            throw new FailReturnObject(1971, "请输入正确的手机号", ReturnObject.Level.INFO);
        }
        Long millis = System.currentTimeMillis();
        Long lastTime = cacheManager.getCache("smsCode").get("lastTime"+mobile,Long.class);
        if(lastTime!=null&&(millis-lastTime<60000)){
            throw new FailReturnObject(1895,"请一分钟后再获取验证码", ReturnObject.Level.INFO);
        }
        Random random = new Random();
        String code = Integer.toString(random.nextInt(9999) % (9999 - 1111 + 1) + 1111);
//        aliyunSmsService.sendMessage(mobile,code,"SMS_105125051");
        aliyunSmsService.sendMessage(mobile,code,smsTemplate);
        cacheManager.getCache("smsCode").put("lastTime"+mobile,millis);
        cacheManager.getCache("smsCode").put(mobile,code);
        return code;
    }
}
