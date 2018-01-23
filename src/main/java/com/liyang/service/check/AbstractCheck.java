package com.liyang.service.check;

import com.liyang.domain.base.BaseEntity;
import com.liyang.util.Assert;

public abstract class AbstractCheck<T extends BaseEntity> implements ICheck<T> {
    private ICheck nextCheck;

    @Override
    public void handler(T t) {
    }

    @Override
    public void check(T entity) {
        beforCheck(entity);
        handler(entity);
        if (nextCheck != null) {
            nextCheck.check(entity);
        }
    }

    @Override
    final public void setNext(ICheck nextCheck) {
        if (nextCheck != null &&this== nextCheck.getNext()) {
            throw new IllegalArgumentException(nextCheck.getClass().getName() + "已经把" + this.getClass().getName() + "设置为nextCheck");
        }
        this.nextCheck = nextCheck;
    }

    @Override
    public ICheck getNext() {
        return nextCheck;
    }

    public void beforCheck(T entity) {
        Assert.notNull(entity, entity.getClass().getName() + "must be not null");
    }
}
