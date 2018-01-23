package com.liyang.service.check;

import com.liyang.domain.base.BaseEntity;

 public interface ICheck <T>
 {
    void handler(T t);
    void setNext(ICheck check);
    void check(T t);
    ICheck getNext();
}
