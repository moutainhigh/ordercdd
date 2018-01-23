package com.liyang.service.check.cdd;

import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.person.Person;
import com.liyang.domain.person.PersonRepository;
import com.liyang.service.check.AbstractCheck;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject;

/**
 * 1 填入身份证时，遍历手机号，如果客户的手机号已经存在，身份证和客户表里面的信息不一致，则反馈：该身份证已经绑定了手机号XXX，如果需要修改请联系管理员。
 */
public class CddApplicationCheckOne extends AbstractCheck<Ordercdd> {
    PersonRepository personRepository;

    public CddApplicationCheckOne(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void handler(Ordercdd ordercdd) {
        Person person = personRepository.findByTelephone(ordercdd.getApplyMobile());
        if (person == null) {
            return;
        }

        if (!person.getIdentity().equals(ordercdd.getApplyIdentityNo())) {
            throw new FailReturnObject(1663, "该手机号已绑定其他用户的身份证，请重新填写", ReturnObject.Level.INFO);
        }
    }
}
