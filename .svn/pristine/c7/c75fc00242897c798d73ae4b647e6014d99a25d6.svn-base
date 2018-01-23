package com.liyang.controller;

import com.liyang.service.AbstractAuditorService;
import com.liyang.service.OrdercddService;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/workflow")
public class WorkflowController {

    @Autowired
    ApplicationContext applicationContext;

    @GetMapping(value = "/query")
    public Object index(@RequestParam("entityname") String entityName) {
        Class c;
        try {
            c = Class.forName("com.liyang.service." + entityName + "Service");
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            throw new FailReturnObject(404, "不存在", ReturnObject.Level.INFO);
        }
        Object service = applicationContext.getBean(c);
        if (!(service instanceof AbstractAuditorService)) {
            throw new FailReturnObject(1000, "不是实体服务对象", ReturnObject.Level.INFO);
        }
        return ((AbstractAuditorService) service).workflowTree();
    }
}
