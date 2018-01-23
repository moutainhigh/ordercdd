package com.liyang.domain.creditcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.base.WorkflowEntityRepository;

//@RepositoryRestResource(excerptProjection = UserProjection.class)
public interface CreditcardRepository extends WorkflowEntityRepository<Creditcard>,SpecificationQueryRepository<Creditcard> {
	//根据persondid找到账户
	public Creditcard findByPerson_id(@Param("personId")Integer personId);
	
	public Creditcard findByCreditcardIdentity(@Param("creditcardIdentity") String creditcardIdentity);
	
	@Query("select a from Creditcard a inner join a.state s where s.stateCode= :stateCode")
	public Page<Creditcard> getAllCreditcardByStateCode(@Param("stateCode")String stateCode,Pageable pageable);
	@Query("select a from Creditcard a inner join a.state s where s.stateCode=:stateCode")
	public Page<Creditcard> listOwnByStateCode(@Param("stateCode")String stateCode,Pageable pageable);
	//select m from #{#entityName} m JOIN m.state s where s.stateCode = :stateCode
	//业务员费配到自己下的业务      这个可以改成业务人员登陆的id
//	@Query("select a from Orderwdsjsh o  inner join o.debtorCreditcard a  where a.state.stateCode=:stateCode and o.isDistribution=1 and o.createdBy.id in  (select u.id from User u where u.serviceUserId=?#{principal.id})")
//	public Page<Creditcard> getCreditcardByServiceUserId(@Param("stateCode")String stateCode, Pageable pageable);
    @Query("select a from Orderwdsjsh o  inner join o.debtorCreditcard a  where a.state.stateCode=:stateCode and o.isDistribution=1 and o.serviceUser in(?#{principal.id})")
    public Page<Creditcard> getCreditcardByServiceUserId(@Param("stateCode")String stateCode, Pageable pageable);
}