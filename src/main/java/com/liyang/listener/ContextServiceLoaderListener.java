package com.liyang.listener;

import com.liyang.config.ApplicationContextAwares;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 加载所有的由spring注入的service
 * @authpr jianger
 * @Date 2018/1/9 下午3:18
 **/
@Component
public class ContextServiceLoaderListener implements ApplicationListener<ContextRefreshedEvent> {
    private static Map<String, Object> map = new HashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("加载所有的Spring注入的service");
        Map<String, Object> services = ApplicationContextAwares.getApplicationContext().getBeansWithAnnotation(Service.class);
        //设置值
        setAllServices(services);

        System.out.println("加载的service个数为：" + services.size());
    }

    public void setAllServices(Map<String, Object> map) {
        this.map = map;
    }

    public static Map<String, Object> getAllServices() {
        if (map.isEmpty()) {
            return ApplicationContextAwares.getApplicationContext().getBeansWithAnnotation(Service.class);
        }
        return map;
    }
}
