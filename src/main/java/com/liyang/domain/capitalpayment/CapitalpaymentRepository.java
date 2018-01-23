package com.liyang.domain.capitalpayment;


import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.base.WorkflowEntityRepository;

public interface CapitalpaymentRepository extends WorkflowEntityRepository<Capitalpayment>,SpecificationQueryRepository<Capitalpayment> {
}
