//package com.liyang.service;
//
//import com.liyang.Application;
//import com.liyang.domain.bankcard.Bankcard;
//import com.liyang.domain.ordercdd.Ordercdd;
//import org.aspectj.weaver.ast.Or;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.Assert.*;
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {Application.class})
//@Transactional
//public class BankcardServiceTest {
//    @Autowired
//    BankcardService bankcardService;
//    @Test
//    public void testSaveByOrdercdd()
//    {
//
//        Ordercdd ordercdd = new Ordercdd();
//
//        Bankcard bankcard = bankcardService.saveByOrdercdd(ordercdd);
//        assertNotNull(bankcard);
//    }
//}