package com.liyang.service.check.cdd;

import com.liyang.util.FailReturnObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@SpringBootTest(classes = com.liyang.Application.class)
@RunWith(SpringRunner.class)
@Transactional
public class CddApplicationCheckTwoTest extends BaseTest {
    protected CddApplicationCheckTwo cddApplicationCheckTwo;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        cddApplicationCheckTwo =new CddApplicationCheckTwo(personRepository);
    }

    @Test(expected = FailReturnObject.class)//客户的手机号不存在，身份证存在
    public void handler() {
        cddApplicationCheckTwo.check(createOrdercdd(null,personLi.getTelephone()+1,personLi.getIdentity()));
    }
    @Test//客户的手机号存在，身份证不存在
    public void handler2() {
        cddApplicationCheckTwo.check(createOrdercdd(null,personLi.getTelephone(),"16735"));
    }

    @Test//客户的手机号不存在，身份证不存在
    public void handler3() {
        cddApplicationCheckTwo.check(createOrdercdd(null,personLi.getTelephone()+1,"16735"));
    }
}