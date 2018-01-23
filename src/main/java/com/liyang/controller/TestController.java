package com.liyang.controller;

import com.liyang.controller.domain.LoanController;
import com.liyang.domain.user.UserRepository;
import com.liyang.service.AccountService;
import com.liyang.service.OrdercddService;
import com.liyang.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class TestController {

    private final static Log log = LogFactory.getLog(TestController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    LoanController loanController;
    @Autowired
    OrdercddService ordercddService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    UserService userService;
    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    @ResponseBody
    public String test(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException {

//        Class.forName("")
//                return applicationContext.getBean(Class.forName("com.liyang.service.OrdercddService"))
//        return ordercddService.workflowTree();
        return "test";
    }

//    /**
//     * @return
//     */
//    private Map<String, Object> 迁移() {
//        List<User> users = userRepository.findAll();
//        HashMap<String, Object> hashMap = new HashMap<>();
//        for (User entity : users) {
//            try {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                entity.setSig(TIMSignature.generate(entity.getId().toString()).urlSig);
//                userRepository.save(entity);
//
//                hashMap.put(entity.getId().toString(), entity.getSig());
//
//                timService.addUser(entity.getId().toString(), entity.getUsername(), "");
//                //创建默认account
//                //begin 新建完user创建默认的account --Jax
//                accountService.createdDefaultAccountByUser(entity);
//                //end
//            } catch (Exception e) {
//            }
//        }
//        return hashMap;
//    }

    //

}
