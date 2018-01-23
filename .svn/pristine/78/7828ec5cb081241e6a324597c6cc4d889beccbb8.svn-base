package com.liyang.service.check.cdd;

import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.person.PersonRepository;
import com.liyang.service.check.ICheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 1 填入身份证时，遍历手机号，如果客户的手机号已经存在，身份证和客户表里面的信息不一致，则反馈：该身份证已经绑定了手机号XXX，如果需要修改请联系管理员。
 * <p>
 * 2填入身份证时，遍历手机号，如果客户的手机号不存在，再次遍历身份证，身份证存在，则反馈：该身份证已经绑定了手机号XXX，如果需要修改请联系管理员。
 * <p>
 * <p>
 * 3填入身份证时，遍历客户姓名，如果客户姓名存在，但是身份证和客户表里面的不一致，则反馈：该身份证已经绑定了客户XXX，如果需要修改请联系管理员。
 * <p>
 * 4填入身份证时，遍历客户姓名，如果客户姓名不存在，再次遍历身份证，身份证和客户表里面的一致，则反馈：该身份证已经绑定了客户XXX，如果需要修改请联系管理员。
 * 署名  郑超逸 2017年12月12日12:11:44
 */
@Service
public class CddCheckSRV {
    @Autowired
    PersonRepository personRepository;

    public void checkApplication(Ordercdd ordercdd) {
        ICheck<Ordercdd> cddApplicationCheckOne = new CddApplicationCheckOne(personRepository);
        ICheck<Ordercdd> cddApplicationCheckTwo = new CddApplicationCheckTwo(personRepository);
        ICheck<Ordercdd> cddApplicationCheckFour = new CddApplicationCheckFour(personRepository);
        cddApplicationCheckOne.setNext(cddApplicationCheckTwo);
        cddApplicationCheckTwo.setNext(cddApplicationCheckFour);
        cddApplicationCheckOne.check(ordercdd);
    }

    public void checkCreate(Ordercdd ordercdd) {
        ICheck<Ordercdd> cddCreateCheckMobile =new CddCreateCheckMobile(personRepository);
        cddCreateCheckMobile.check(ordercdd);
    }
}
