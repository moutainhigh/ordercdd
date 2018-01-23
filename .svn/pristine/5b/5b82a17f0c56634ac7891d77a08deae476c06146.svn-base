package com.liyang.service.check.cdd;

import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.person.Person;
import com.liyang.domain.person.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseTest {
    protected Person personLi;
    protected Ordercdd ordercddheSame, ordercdd1, ordercdd2;
    @Autowired
    PersonRepository personRepository;

    @Before
    public void setUp() throws Exception {
        personLi = new Person();
        personLi.setIdentity("342225199509104999");
        personLi.setName("李某人");
        personLi.setTelephone("15222958467");
        personRepository.save(personLi);
    }

    protected Ordercdd createOrdercdd(String realName,String applyMobile,String applyIdentityNo) {
        Ordercdd ordercdd = new Ordercdd();
        ordercdd.setRealName(realName);
        ordercdd.setApplyMobile(applyMobile);
        ordercdd.setApplyIdentityNo(applyIdentityNo);
        return ordercdd;
    }
}
