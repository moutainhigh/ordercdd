package com.liyang.domain.storebonus;


import org.springframework.data.jpa.repository.Query;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.base.WorkflowEntityRepository;
import com.liyang.domain.product.Product;

public interface StoreBonusRepository extends WorkflowEntityRepository<StoreBonus>,SpecificationQueryRepository<StoreBonus> {
	
	@Query("select s from StoreBonus s join s.ordercdd o where o.id=?1")
	public StoreBonus findByOrdercddId(Integer ordercddId);
}
