package com.liyang.service.check.person;

import com.liyang.domain.person.Person;
import com.liyang.domain.person.PersonRepository;
import com.liyang.service.check.AbstractCheck;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 1.身份证对应的手机号唯一
 * 2.手机号对应的身份证唯一
 * 3.手机号对应的客户姓名唯一
 * 4.身份证对应的客户姓名唯一
 */
@Component
public class PersonUpdateCheck extends AbstractCheck<Person> {
    @Autowired
    PersonRepository personRepository;

    @Override
    public void handler(Person person) {

        if (person == null) {
            return;
        }
        if (person.getIdentity() != null) {
            Person identityPerson = personRepository.findByIdentity(person.getIdentity());
            if (identityPerson != null && !identityPerson.getId().equals(person.getId())) {
                throw new FailReturnObject(1563,"身份证已经存在", ReturnObject.Level.INFO);
            }
        }
        if(person.getTelephone()!=null){
            Person telephonePerson=personRepository.findByTelephone(person.getTelephone());
            if(telephonePerson!=null&&!telephonePerson.getId().equals(person.getId())){
                throw new FailReturnObject(1563,"手机号已经存在", ReturnObject.Level.INFO);
            }
        }
    }
}
