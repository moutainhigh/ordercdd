package com.liyang.domain.loan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.base.WorkflowEntityRepository;

//@RepositoryRestResource(excerptProjection = UserProjection.class)
public interface LoanRepository extends WorkflowEntityRepository<Loan>,SpecificationQueryRepository<Loan>{
	//显示账户下的贷款
	@Query("select l from Loan l inner join l.state s where s.stateCode=:stateCode and l.creditcard in (select a from Creditcard a where a.id=:creditcardId)")
	public Page<Loan> listOwnLoan(@Param("creditcardId") Integer creditcardId,@Param("stateCode")String stateCode,Pageable pageable);
	
	
		
	//select m from #{#entityName} m JOIN m.state s where s.stateCode = :stateCode
	//业务员费配到自己下的业务      这个可以改成业务人员登陆的id
	//@Query("select o from Loan o  inner join o.debtorUser u  where o.state.stateCode=:stateCode and u.id  in  (select u.id from User u where u.serviceUserId=?#{principal.id})")
	//public Page<Loan> getLoanByServiceUserId(@Param("stateCode")String stateCode, Pageable pageable);

	@Query("select o from Loan o  where o.serviceUser.id =?#{principal.id}")
	public Page<Loan> getLoanByServiceUserId(@Param("stateCode")String stateCode, Pageable pageable);


	//根据ordercdd的id查找
	@Query("select o from Loan o where o.ordercdd.id=?1")
	public Loan getLoanByOrdercddId(Integer ordercddId);
}
