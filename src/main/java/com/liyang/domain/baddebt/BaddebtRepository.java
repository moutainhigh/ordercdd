package com.liyang.domain.baddebt;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.base.WorkflowEntityRepository;

public interface BaddebtRepository extends WorkflowEntityRepository<Baddebt>,SpecificationQueryRepository<Baddebt> {
}