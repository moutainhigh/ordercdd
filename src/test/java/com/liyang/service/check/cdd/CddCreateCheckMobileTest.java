package com.liyang.service.check.cdd;

import com.liyang.Application;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.person.Person;
import com.liyang.domain.person.PersonRepository;
import com.liyang.util.FailReturnObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@Transactional
public class CddCreateCheckMobileTest extends BaseTest{

    CddCreateCheckMobile cddCreateCheckMobile;
    @Override
    public void setUp() throws Exception {
        super.setUp();
        cddCreateCheckMobile =new CddCreateCheckMobile(personRepository);
    }

    @Test//都不同
    public void handler() {
        cddCreateCheckMobile.check(createOrdercdd("张","15489543","134612798"));
    }
    @Test(expected = FailReturnObject.class)//
    public void handler2() {
        cddCreateCheckMobile.check(createOrdercdd("abc",personLi.getTelephone(),""));
    }
    @Test//都相同
    public void handler3() {
        cddCreateCheckMobile.check(createOrdercdd(personLi.getName(),personLi.getTelephone(),null));
    }
}