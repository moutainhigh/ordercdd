package com.liyang.domain.ordercddloan;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.base.WorkflowEntityRepository;

public interface OrdercddloanRepository extends WorkflowEntityRepository<Ordercddloan>,SpecificationQueryRepository<Ordercddloan> {
    @Query("select m from Ordercddloan m JOIN m.state s where s.stateCode = :stateCode")
    public Page<Ordercddloan> getByStateCode(@Param("stateCode") String stateCode, Pageable pageable);
}
