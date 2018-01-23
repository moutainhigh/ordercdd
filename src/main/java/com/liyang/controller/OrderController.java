package com.liyang.controller;

import com.liyang.domain.orderwdsjsh.OrderwdsjshRepository;
import com.liyang.domain.orderwdxyd.OrderwdxydRepository;
import com.liyang.domain.user.User;
import com.liyang.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class OrderController {
    @Autowired
    OrderwdxydRepository orderwdxydRepository;
    @Autowired
    OrderwdsjshRepository orderwdsjshRepository;

    @GetMapping(value = "order/query")
    @ResponseBody
    public Map<String ,Object> orderQuery()
    {
        Map<String ,Object> rsp=new HashMap<>();
        rsp.put("is_order",false);
        User user =CommonUtil.getPrincipal();
        if(orderwdsjshRepository.findFirstByCreatedBy(user)!=null){
            rsp.put("is_order",true);
        }else if(orderwdxydRepository.findFirstByCreatedBy(user)!=null){
            rsp.put("is_order",true);
        }

        return  rsp;
    }
}
