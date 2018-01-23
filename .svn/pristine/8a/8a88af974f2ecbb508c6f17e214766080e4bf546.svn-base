package com.liyang.domain.person;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.base.WorkflowEntityRepository;
import org.springframework.data.rest.core.annotation.RestResource;

//@RepositoryRestResource(excerptProjection = RoleProjection.class)
public interface PersonRepository extends WorkflowEntityRepository<Person>,SpecificationQueryRepository<Person> {
	@Query("select p from Person p inner join p.state state where p.telephone=:telephone and state.stateCode=:stateCode")
	public Person getByTelephoneAndStateCode(@Param("telephone")String telephone,@Param("stateCode")String stateCode);
	
	public Person findByIdentity(String identity);

	public Person findByTelephone(String telephone);
//	@RestResource(exported = false)
//	Person findByIdentityAndTelephone(String identity,String telephone);
	
	@Modifying
	@Transactional
	@Query("delete from PersonFile f where f.entity.id = ?1")
	public void deleteFilesByEntityId(Integer entityId);

}
