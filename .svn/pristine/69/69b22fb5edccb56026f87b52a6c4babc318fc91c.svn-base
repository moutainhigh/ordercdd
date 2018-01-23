package com.liyang.domain.storeadvance;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.base.WorkflowEntityRepository;
import com.liyang.domain.storeadvance.Storeadvance.StoreadvanceType;

public interface StoreadvanceRepository extends WorkflowEntityRepository<Storeadvance>,SpecificationQueryRepository<Storeadvance> {
	
	@Query("select a from Storeadvance a join a.loan l where l.id=?1 and a.type = ?2")
	public List<Storeadvance> findByLoanAndType(Integer loanId, StoreadvanceType type);
}