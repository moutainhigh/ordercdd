package com.liyang.domain.bankcard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.bank.Bank;
import com.liyang.domain.base.AuditorEntityRepository;

public interface BankcardRepository extends AuditorEntityRepository<Bankcard>,SpecificationQueryRepository<Bankcard> {
	
	@Query("select b from Bankcard b join b.user u join u.role r join u.department d join d.departmenttype dt where d.id =?1 and r.roleCode ='CREDITOR_OWNER' and dt.departmenttypeCode='CREDITOR'")
	public List<Bankcard> getAccountIdentityByDepartment(@Param(value = "departmentId") Integer departmentId);


	Bankcard findBankcardByAccountIdentityAndBank(String accountIdentity, Bank bank);
	Bankcard findBankcardByAccountIdentity(String accountIdentity);

	@Query("select b from Bankcard b join b.user u where b.createdByDepartment in ?#{principal?.department} and u.role.roleCode!='USER' and b.state.stateCode='ENABLED'")
	List<Bankcard> getSelectBankcardsBySelf();
}
