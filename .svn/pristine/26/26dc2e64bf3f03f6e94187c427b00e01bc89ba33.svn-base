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
public class CddApplicationCheckFourTest extends BaseTest {
    protected CddApplicationCheckFour cddApplicationCheckFour;
    @Override
    public void setUp() throws Exception {
        super.setUp();
        cddApplicationCheckFour=new CddApplicationCheckFour(personRepository);
    }

    @Test(expected = FailReturnObject.class)
    public void handler() {
        cddApplicationCheckFour.check(createOrdercdd("",personLi.getTelephone(),personLi.getIdentity()));
    }

    @Test
    public void handler2() {
        cddApplicationCheckFour.check(createOrdercdd("",personLi.getTelephone(),"1922468704547"));
    }
}