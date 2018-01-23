package com.liyang.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @authpr jianger
 * @Date 2018/1/9 下午2:05
 **/
@Component
public class ApplicationContextAwares implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过bean的名字获取bean
    public static Object getBeanByName(String name) {
        return getApplicationContext().getBean(name);
    }
}
