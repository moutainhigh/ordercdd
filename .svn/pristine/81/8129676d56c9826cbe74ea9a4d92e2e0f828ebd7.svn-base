package com.liyang.domain.role;

import java.util.Collection;
import java.util.List;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.liyang.domain.base.AuditorEntityRepository;
import com.liyang.domain.base.EntityRepository;
import org.springframework.data.rest.core.annotation.RestResource;

//@RepositoryRestResource(excerptProjection = RoleProjection.class)
public interface RoleRepository extends AuditorEntityRepository<Role>,SpecificationQueryRepository<Role> {
	public Role findByRoleCode(String roleCode);
	@RestResource(exported = false)
	List<Role> findAllByIdIn(Collection<Integer> ids);
}
