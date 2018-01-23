package com.liyang.service.check.cdd;

import com.liyang.domain.application.Application;
import com.liyang.util.FailReturnObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest(classes = com.liyang.Application.class)
@RunWith(SpringRunner.class)
@Transactional
public class CddApplicationCheckOneTest extends BaseTest {

    CddApplicationCheckOne cddApplicationCheckOne;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        cddApplicationCheckOne = new CddApplicationCheckOne(personRepository);
    }

    @Test(expected = FailReturnObject.class)//手机号已经存在，身份证和客户表里面的信息不一致
    public void handler() {
        cddApplicationCheckOne.check(createOrdercdd(null, personLi.getTelephone(), "1134654352135"));
    }

    @Test//手机号已经存在，身份证和客户表里面的信息一致
    public void handler2() {
        cddApplicationCheckOne.check(createOrdercdd(null, personLi.getTelephone(), personLi.getIdentity()));
    }

    @Test//手机号不存在，身份证和客户表里面的信息不一致
    public void handler3() {
        cddApplicationCheckOne.check(createOrdercdd(null, "19157", "156789"));
    }
}