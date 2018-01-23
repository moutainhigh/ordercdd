package com.liyang.domain.productcompanyfee;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductcompanyfeeRepository extends JpaRepository<Productcompanyfee, Integer>{
	
	@Modifying
	@Transactional
	@Query("delete from Productcompanyfee c where c.product.id = ?1")
    public void deleteByProduct(Integer productId);
}
