package com.liyang.domain.loanexception;

import org.springframework.data.jpa.repository.Query;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.base.WorkflowEntityRepository;

public interface LoanexceptionRepository extends WorkflowEntityRepository<Loanexception>,SpecificationQueryRepository<Loanexception> {
	
	@Query("select l from Loanexception l join l.state s where l.loan.id=?1 and s.stateCode='ACCOUNT'")
	public Loanexception findByEnable(Integer loanId);
}
