package com.liyang.domain.creditrepayment;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.base.WorkflowEntityRepository;

public interface CreditrepaymentRepository extends WorkflowEntityRepository<Creditrepayment>,SpecificationQueryRepository<Creditrepayment> {
	//根据creditcardId找到信用实际还款记录
	@Query("select r from Creditrepayment r inner join r.creditcard a where a.id=:creditcardId")
	public Page<Creditrepayment> getCreditRepaymentByCreditcard(@Param("creditcardId")Integer creditcardId,Pageable pageable);
	

	//根据LoanId找到某笔贷款的实际还款记录
	@Query("select r from Creditrepayment r inner join r.loan a where a.id=:loanId")
	public Page<Creditrepayment> getCreditRepaymentByLoan(@Param("loanId")Integer loanId,Pageable pageable);
	
	@Query("select r from Creditrepayment r inner join r.loan a where a.id=?1")
	public List<Creditrepayment> findByLoanId(Integer loanId);

	//根据ordercddsn来查找
	@Query(value = "select c from  Creditrepayment c where c.orderSn=?1")
	public Set<Creditrepayment> getCreditrepaymentByOrderSn(String orderSn);
	
	@Query("select m from Creditrepayment m where m.loan.id=(select c.loan.id from Creditrepayment c where c.id=?1) order by m.lastModifiedAt asc")
	public Set<Creditrepayment> findMentList(@Param("id")Integer id);
}